package com.codenjoy.dojo.japanese.model.portable;

import static com.codenjoy.dojo.japanese.model.portable.Solver.EXACTLY_BLACK;
import static com.codenjoy.dojo.japanese.model.portable.Solver.EXACTLY_NOT_BLACK;

public class LineSolver {

    private Dot[] dots;
    private int length;

    private int[] numbers;
    private int countNumbers;

    private Blocks blocks;
    private Probabilities probabilities;

    private int from;
    private int to;
    private int range;

    public boolean calculate(int[] inputNumbers, Dot[] inputDots) {
        dots = inputDots;
        numbers = inputNumbers;
        length = dots.length - 1;
        countNumbers = numbers.length - 1;

        probabilities = new Probabilities(length);
        blocks = new Blocks(length);

        // если цифер нет вообще, то в этом ряде не может быть никаких BLACK
        // если кто-то в ходе проверки гипотезы все же сделал это, то
        // мы все равно поставим все белые и скажем что calculate этого сценария невозможен
        if (countNumbers == 0) {
            boolean foundBlack = false;
            for (int i = 1; i <= length; i++) {
                if (dots[i] == Dot.BLACK) {
                    foundBlack = true;
                }
                dots[i] = Dot.WHITE;
                probabilities.set(i, EXACTLY_NOT_BLACK);
            }
            return !foundBlack;
        }

        boolean result = true;
        // обрезаем слева и справа уже отгаданные числа
        if (cut()) {
            // дальше работаем в диапазоне from...to
            result = generateCombinations();
        }

        // и превращаем явные (1.0, 0.0) probabilities в dots
        probabilities.updateDots(dots);

        // возвращаем возможность этой комбинации случиться для этого ряда чисел
        return result;
    }

    private boolean generateCombinations() {
        blocks.packTightToTheLeft(numbers, countNumbers, range);

        // пошли генерить комбинации
        do {
            blocks.saveCombinations(from, to, probabilities.combinations());
            if (probabilities.isApplicable(from, to, dots)) {
                probabilities.addCombination(from, to);
            }
            blocks.loadCombination(from, to, probabilities.combinations());
        } while (blocks.hasNext());

        probabilities.calculate(from, to);
        return probabilities.isAny();
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
    // TODO продолжить рефакторить
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
                            probabilities.set(i, EXACTLY_BLACK);
                            previous = Dot.BLACK;
                        } else {
                            // закончили numbersIndex'ный ряд
                            countDots = 0; // новый ряд еще не начали
                            probabilities.set(i, EXACTLY_NOT_BLACK);
                            previous = Dot.WHITE;

                            SHLNumbers(); // сдвигаем ряд (удаляем первый элемент)
                            numbersIndex--; // из за смещения
                        }
                    } else {
                        // UNSET после WHITE
                        if (countNumbers == 0) {
                            // ряд пустой, тут все WHITE
                            probabilities.set(i, EXACTLY_NOT_BLACK);
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
                    probabilities.set(i, EXACTLY_BLACK);
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
                    probabilities.set(i, EXACTLY_NOT_BLACK);
                }
                break;
            }
            i++;
            // TODO подумать почему тут нельзя перебирать комбинации
            if ((!stop && i > length) || countNumbers == 0) return false; // достигли конца
        } while (!stop);

        // идем по ряду в обратном порядке
        previous = Dot.WHITE;
        numbersIndex = countNumbers + 1;
        countDots = 0;
        i = length;
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
                            probabilities.set(i, EXACTLY_BLACK);
                            previous = Dot.BLACK;
                        } else {
                            // закончили numbersIndex'ный ряд
                            countDots = 0; // новый ряд еще не начали
                            probabilities.set(i, EXACTLY_NOT_BLACK);
                            previous = Dot.WHITE;
                            countNumbers--;
                        }
                    } else {
                        // WHITE после пустоты
                        if (countNumbers == 0) { // в этом ряде ничего больше делать нечего
                            probabilities.set(i, EXACTLY_NOT_BLACK); // кончаем его:) // TODO тут скорее UNKNOWN
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
                    probabilities.set(i, EXACTLY_BLACK);
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
                    probabilities.set(i, EXACTLY_NOT_BLACK);
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
        return probabilities.get(x);
    }

    public Dot dots(int x) {
        return dots[x];
    }

    public int length() {
        return length;
    }
}
