package com.codenjoy.dojo.japanese.model.portable;

import java.util.Arrays;

import static com.codenjoy.dojo.japanese.model.portable.Solver.*;

public class LineSolver {

    private boolean[] combinations;
    private int combinationCount;

    private Dot[] dots;
    private double[] probability;
    private int len;

    private int[] numbers;
    private int countNumbers;

    private Blocks blocks;

    private int from;
    private int to;
    private int range;

    public boolean calculate(int[] inputNumbers, Dot[] inputDots) {
        dots = inputDots;
        numbers = inputNumbers;
        len = dots.length - 1;
        countNumbers = numbers.length - 1;

        resetArrays();

        // если цифер нет вообще, то в этом ряде не может быть никаких BLACK
        // если кто-то в ходе проверки гипотезы все же сделал это, то
        // мы все равно поставим все белые и скажем что calculate этого сценария невозможен
        if (countNumbers == 0) {
            boolean foundBlack = false;
            for (int i = 1; i <= len; i++) {
                if (dots[i] == Dot.BLACK) {
                    foundBlack = true;
                }
                dots[i] = Dot.WHITE;
                probability[i] = EXACTLY_NOT;
            }
            return !foundBlack;
        }

        // TODO почему-то чистим все вероятности и ставим их в WHITE
        for (int i = 1; i <= len; i++) {
            probability[i] = EXACTLY_NOT;
        }

        // обрезаем слева и справа уже отгаданные числа
        boolean result = true;
        if (cut()) {
            // дальше работаем в диапазоне from...to
            result = generateCombinations();
        }

        // и превращаем явные (1.0, 0.0) probabilities в dots
        getDotsFromProbabilities();

        // возвращаем возможность этой комбинации случиться для этого ряда чисел
        return result;
    }

    private void resetArrays() {
        // инициализация всех массивов
        // TODO тут в некоторых тестах case_b15 случается переполнение, потому тут массив большой
        combinations = new boolean[len + 1 + 100];
        probability = new double[len + 1 + 100];

        blocks = new Blocks(len);
    }

    private boolean generateCombinations() {
        blocks.packTightToTheLeft(numbers, countNumbers, range);

        // пошли генерить комбинации
        combinationCount = 0;
        do {
            blocks.saveCombinations(from, to, combinations);
            if (testCombination()) {
                combinationCount++;

                for (int i = from; i <= to; i++) {
                    if (combinations[i]) {
                        probability[i]++;
                    }
                }
            }
            blocks.loadCombination(from, to, combinations);
        } while (blocks.next());

        for (var i = from; i <= to; i++) {
            if (combinationCount != 0) {
                probability[i] = probability[i] / combinationCount;
            } else {
                probability[i] = UNKNOWN;
            }
        }
        return combinationCount != 0;
    }

    // после того, как у нас есть массив вероятностей для всех возможных комбинаций
    // мы в праве утверждать, что ячейки с вероятностями 1.0 и 0.0 могут быть
    // заполнены BLACK и WHITE соответственно
    private void getDotsFromProbabilities() {
        for (int i = 1; i <= len; i++) {
            if (probability[i] == EXACTLY) {
                dots[i] = Dot.BLACK;
            }
            if (probability[i] == EXACTLY_NOT) {
                dots[i] = Dot.WHITE;
            }
        }
    }

    private boolean testCombination() {
        for (int i = from; i <= to; i++) {
            switch (dots[i]) {
                case UNSET:
                    break;
                case BLACK:
                    if (!combinations[i]) {
                        return false;
                    }
                    break;
                case WHITE:
                    if (combinations[i]) {
                        return false;
                    }
                    break;
                case ASSUMPTION:
                    break;
            }
        }
        return true;
    }

    private void SHLNumbers() {
        for (int j = 2; j <= countNumbers; j++) {
            numbers[j - 1] = numbers[j];
        }
        countNumbers--;
    }

    // метод пытается с начала и конца ряда определить ряды точек, которые уже открыты полностью.
    // возвращает true если есть над чем гонять комбинации и даем возможность перебрать их
    // но перебирать будем только на тех числах, которые остались после оптимизации и в диапазоне точек
    // from ... to
    private boolean cut() {
        // идем по ряду в прямом порядке
        Dot previous = Dot.WHITE;
        int numbersIndex = 0;
        int countDots = 0; // количество точек в ряду
        int i = 1;
        from = i;
        boolean stop = false;
        do {
            switch (dots[i]) {
                case UNSET: {
                    if (previous.isBlack()) {
                        // UNSET после BLACK - надо заканчивать ряд
                        if (countDots < numbers[numbersIndex]) {
                            // незакончили numbersIndex'ный ряд
                            countDots++; // количество точек
                            probability[i] = EXACTLY;
                            previous = Dot.BLACK;
                        } else {
                            // закончили numbersIndex'ный ряд
                            countDots = 0; // новый ряд еще не начали
                            probability[i] = EXACTLY_NOT;
                            previous = Dot.WHITE;

                            SHLNumbers(); // сдвигаем ряд (удаляем первый элемент)
                            numbersIndex--; // из за смещения
                        }
                    } else {
                        // UNSET после WHITE
                        if (countNumbers == 0) {
                            // ряд пустой, тут все WHITE
                            probability[i] = EXACTLY_NOT;
                        } else {
                            from = i;
                            stop = true; // иначе выходим
                        }
                    }
                }
                break;
                case BLACK: {
                    if (previous.isWhite()) {
                        numbersIndex++;
                        countDots = 0;
                        previous = Dot.BLACK;
                    }
                    probability[i] = EXACTLY;
                    countDots++;
                }
                break;
                case WHITE: {
                    if (previous.isBlack()) {
                        if (countDots != numbers[numbersIndex]) {
                            // TODO подумать почему тут можно перебирать комбинации
                            return true;
                        }
                        previous = Dot.WHITE;

                        SHLNumbers(); // сдвигаем ряд (удаляем первый элемент)
                        numbersIndex--; // из за смещения
                    }
                    probability[i] = EXACTLY_NOT;
                }
                break;
            }
            i++;
            // TODO подумать почему тут нельзя перебирать комбинации
            if ((!stop && i > len) || countNumbers == 0) return false; // достигли конца
        } while (!stop);

        // идем по ряду в обратном порядке
        previous = Dot.WHITE;
        numbersIndex = countNumbers + 1;
        countDots = 0;
        i = len;
        to = i;
        stop = false;
        do {
            switch (dots[i]) {
                case UNSET: {
                    if (previous.isBlack()) {
                        // UNSET после BLACK - надо заканчивать ряд
                        if (countDots < numbers[numbersIndex]) {
                            // незакончили numbersIndex'ный ряд
                            countDots++; // количество точек
                            probability[i] = EXACTLY;
                            previous = Dot.BLACK;
                        } else {
                            // закончили numbersIndex'ный ряд
                            countDots = 0; // новый ряд еще не начали
                            probability[i] = EXACTLY_NOT;
                            previous = Dot.WHITE;
                            countNumbers--;
                        }
                    } else {
                        // WHITE после пустоты
                        if (countNumbers == 0) { // в этом ряде ничего больше делать нечего
                            probability[i] = EXACTLY_NOT; // кончаем его:) // TODO тут скорее UNKNOWN
                        } else {
                            to = i;
                            stop = true; // иначе выходим
                        }
                    }
                }
                break;
                case BLACK: {
                    if (previous.isWhite()) {
                        numbersIndex--;
                        countDots = 0;
                        previous = Dot.BLACK;
                    }
                    probability[i] = EXACTLY;
                    countDots++;
                }
                break;
                case WHITE: {
                    if (previous.isBlack()) {
                        if (countDots != numbers[numbersIndex]) {
                            // TODO подумать почему тут можно перебирать комбинации
                            return true;
                        }
                        previous = Dot.WHITE;
                        countNumbers--;
                    }
                    probability[i] = EXACTLY_NOT;
                }
                break;
            }
            i--;
            // TODO подумать почему тут нельзя перебирать комбинации
            if ((!stop && i < 1) || countNumbers == 0) return false; // достигли конца
        } while (!stop);

        range = to - from + 1;
        // если остались ряды точек то надо прогнать генератор, чем сообщаем возвращая true
        return countNumbers != 0 && from <= to;
    }

    public double probability(int x) {
        return probability[x];
    }

    public double[] probability() {
        return Arrays.copyOf(probability, len + 1);
    }

    public Dot dots(int x) {
        return dots[x];
    }

    public Dot[] dots() {
        return Arrays.copyOf(dots, len + 1);
    }

    public int length() {
        return len;
    }
}
