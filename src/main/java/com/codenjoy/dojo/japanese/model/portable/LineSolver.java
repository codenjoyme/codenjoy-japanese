package com.codenjoy.dojo.japanese.model.portable;

import static com.codenjoy.dojo.japanese.model.portable.Solver.*;

public class LineSolver {

    public static class Info {
        public int c;
        public boolean b;
    }

    private boolean[] combinations;
    private int combinationCount;

    private Dot[] dots;
    private double[] probability;
    private int len;

    private int[] numbers;
    private int countNumbers;
    private int cr;
    private Info[] numbers10;

    private int cutFrom;
    private int cutTo;
    private int cutLen;

    public boolean calculate(int[] inputNumbers, Dot[] inputDots) {
        dots = inputDots;
        numbers = inputNumbers;
        len = dots.length - 1;
        countNumbers = numbers.length - 1;

        // инициализация всех массивов
        // TODO тут в некоторых тестах case_b15 случается переполнение, потому тут массив большой
        combinations = new boolean[len + 1 + 100];
        probability = new double[len + 1 + 100];
        numbers10 = new Info[len + 1];
        for (int i = 1; i < numbers10.length; i++) {
            numbers10[i] = new Info();
        }

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

        // TODO дальше мы если не cut что бы это не значило, делаем вторую попытку cut2
        boolean result = true;
        if (!cut()) {
            result = cut2();
        }

        // и превращаем явные (1.0, 0.0) probabilities в dots
        getDotsFromProbabilities();

        // возвращаем возможность этой комбинации случиться для этого ряда чисел
        return result;
    }

    private boolean cut2() {
        boolean b1;
        boolean result;
        cr = 1;
        int j = 0;
        for (int i = 1; i <= countNumbers; i++) {
            numbers10[cr].b = true;
            numbers10[cr].c = numbers[i];
            numbers10[cr + 1].b = false;
            numbers10[cr + 1].c = 1;
            j = j + numbers[i] + 1;
            cr = cr + 2;
        }
        cr = cr - 1;
        if (j > cutLen) cr--;
        if (j < cutLen) numbers10[cr].c = numbers10[cr].c + cutLen - j;
        //-------
        b1 = true;
        combinationCount = 0;
        while (b1) {
            getCombinationsFromNumbers();
            if (testCombination()) {
                combinationCount++;

                for (int i = cutFrom; i <= cutTo; i++) {
                    if (combinations[i]) {
                        probability[i]++;
                    }
                }
            }
            getNumbersFromCombination();
            b1 = manipuleNumbers();
        }

        for (var i = cutFrom; i <= cutTo; i++) {
            if (combinationCount != 0) {
                probability[i] = probability[i] / combinationCount;
            } else {
                probability[i] = UNKNOWN;
            }
        }
        result = (combinationCount != 0);
        return result;
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
        for (int i = cutFrom; i <= cutTo; i++) {
            switch (dots[i]) {
                case UNSET:
                    break;                                         // ничего нет
                case BLACK:
                    if (combinations[i] != true) {
                        return false;
                    }
                    break;
                case WHITE:
                    if (combinations[i] != false) {
                        return false;
                    }
                    break;
                case ASSUMPTION:
                    break;                                         // предполагаемая точка
            }
        }
        return true;
    }

    private void getCombinationsFromNumbers() {
        int x = cutFrom - 1;
        for (int i = 1; i <= cr; i++) {
            for (int j = 1; j <= numbers10[i].c; j++) {
                if (x + j > len || i > len) {
                    break; // TODO этого не должно происходить, но происходит
                }
                combinations[x + j] = numbers10[i].b;
            }
            x = x + numbers10[i].c;
        }
    }

    private void getNumbersFromCombination() {
        int j, cr;
        int leni, i0;
        boolean b = combinations[cutFrom];
        j = 1;
        cr = 1;

        i0 = cutFrom + 1;
        leni = cutTo;
        for (int i = i0; i <= leni; i++) {
            if (i >= combinations.length) break; // TODO этого не должно происходить но происходит
            if (combinations[i] ^ b) {
                numbers10[cr].c = j;
                numbers10[cr].b = b;
                cr++; ;
                j = 0;
            }
            j++;
            b = combinations[i];
        }
        if (j != 0) {
            numbers10[cr].c = j;
            numbers10[cr].b = b;
        }
        this.cr = cr;
    }

    private boolean manipuleNumbers() {
        int a, a2;
        boolean b, b2 = false;
        a = cr;
        boolean Result = true;
        while (true) {
            if (numbers10[a].b) {
                a--;
                if (a <= 0) {
                    Result = false;
                    break;
                }
                b = true;
                while (true) {
                    a2 = 2;
                    if (!b) {
                        continue;
                    }

                    if ((a + 2) > cr) {
                        if ((a + 2) != (cr + 1)) break;
                        if (!numbers10[cr].b) break;

                        b2 = false;
                        if ((numbers10[a].c != 1) && (cr == 2)) {
                            b2 = true;
                        }
                        while (numbers10[a].c == 1) {
                            a -= 2;
                            a2 += 2;
                            if ((a <= 0) || (a2 == cr)) {
                                b2 = true;
                                break;
                            }
                        }

                        if (b2) break;

                        cr++;
                        numbers10[cr].b = false;
                        numbers10[cr].c = 0;
                    }
                    numbers10[a + a2].c = numbers10[a + a2].c + numbers10[a].c - 2;
                    numbers10[a].c = 2;
                    break;
                }
                if (b2) {
                    Result = false;
                    break;
                }
            } else {
                if (cr == 1) {
                    Result = false;
                    break;
                }
                if ((a - 2) <= 0) {
                    if (a < 1) break;
                    cr++;
                    for (int i = (cr - 1); i >= 1; i--) { // TODO downto
                        numbers10[i + 1].c = numbers10[i].c;
                        numbers10[i + 1].b = numbers10[i].b;
                    }
                    numbers10[1].c = 1;
                    numbers10[1].b = false;
                    if (cr > 3) {
                        numbers10[3].c = 1;
                    }
                    if (cr == 3) {
                        numbers10[3].c--;
                    }
                    break;
                }
                numbers10[a - 2].c++;
                numbers10[a].c--;
                if (numbers10[a].c == 0) {
                    if (a == cr) {
                        cr--;
                        break;
                    } else {
                        numbers10[a - 2].c--;
                        numbers10[a].c++;
                        a = a - 2;
                    }
                    continue;
                } else {
//                   cr--;
                    break;
                }
            }
        }
        return Result;
    }

    private void SHLNumbers() {
        for (int j = 2; j <= countNumbers; j++) {
            numbers[j - 1] = numbers[j];
        }
        countNumbers--;
    }

    private boolean cut() {
        // идем по ряду в прямом порядке
        Dot previous = Dot.WHITE;
        int numbersIndex = 0;
        int countDots = 0; // количество точек в ряду
        int i = 1;
        cutFrom = i;
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
                            countDots = 0; // новы ряд еще не начали
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
                            cutFrom = i;
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
                            return false;
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
            if ((!stop && i > len) || countNumbers == 0) return true; // достигли конца
        } while (!stop);

        // идем по ряду в обратном порядке TODO зачем?
        previous = Dot.WHITE;
        numbersIndex = countNumbers + 1;
        countDots = 0;
        i = len;
        cutTo = i;
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
                            countDots = 0; // новы ряд еще не начали
                            probability[i] = EXACTLY_NOT;
                            previous = Dot.WHITE;
                            countNumbers--;
                        }
                    } else {
                        // WHITE после пустоты
                        if (countNumbers == 0) { // в этом ряде ничего больше делать нечего
                            probability[i] = EXACTLY_NOT; // кончаем его:) // TODO тут скорее UNKNOWN
                        } else {
                            cutTo = i;
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
                            return false;
                        }
                        previous = Dot.WHITE;
                        countNumbers--;
                    }
                    probability[i] = EXACTLY_NOT;
                }
                break;
            }
            i--;
            if ((!stop && i < 1) || countNumbers == 0)  return true; // достигли конца
        } while (!stop);

        cutLen = cutTo - cutFrom + 1;
        return cutFrom > cutTo || countNumbers == 0;
    }

    public double probability(int x) {
        return probability[x];
    }

    public Dot array(int x) {
        return dots[x];
    }
}
