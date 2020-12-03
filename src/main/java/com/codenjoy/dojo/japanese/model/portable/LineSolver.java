package com.codenjoy.dojo.japanese.model.portable;

import static com.codenjoy.dojo.japanese.model.portable.Solver.*;

public class LineSolver {

    public static class TRjad10Record {
        public int c;
        public boolean b;
    }

    public static class TNumbers10 {
        public TRjad10Record[] arr = new TRjad10Record[MAX + 1];

        public TNumbers10() {
            for (int i = 1; i < arr.length; i++) {
                arr[i] = new TRjad10Record();
            }
        }
    }

    private boolean[] combinations = new boolean[MAX + 1];
    private int combinationCount;

    private Solver.Dot[] dots;
    private double[] probability = new double[MAX + 1];
    private int len;

    private int[] numbers;
    private int countNumbers;
    private int cr;
    private TNumbers10 numbers10 = new TNumbers10();

    private int cutFrom;
    private int cutTo;
    private int cutLen;

    public boolean calculate(int[] numbers, Dot[] dots) {
        this.len = dots.length - 1;
        this.dots = dots;
        this.countNumbers = numbers.length - 1;
        this.numbers = numbers;

        boolean b1;
        boolean result;
        if (countNumbers == 0) {
            result = true;
            for (int i = 1; i <= len; i++) {
                if (dots[i] == Solver.Dot.BLACK) {
                    result = false;
                } else {
                    dots[i] = Solver.Dot.WHITE;
                }
            }
            return result;
        }
        //-----------
//        b1 = false;
//        for (int i = 1; i <= glLen; i++) {
//            b1 = b1 | (glData.arr[i] != 0);
//            if (b1) break;
//        }
//        if (!b1) {
//            j = 0;
//            for (int i = 1; i <= glCountRjad; i++) j = j + glRjad.arr[i];
//            if (j < (glLen / 2)) {
//                result = true;
//                return result;
//            }
//        }
        //-----------
        for (int i = 1; i <= len; i++) {
            probability[i] = EXACTLY_WHITE;
        }
        //-----------
        result = true;
        if (!cut()) {
            result = false;
            cr = 1;
            int j = 0;
            for (int i = 1; i <= countNumbers; i++) {
                numbers10.arr[cr].b = true;
                numbers10.arr[cr].c = numbers[i];
                numbers10.arr[cr + 1].b = false;
                numbers10.arr[cr + 1].c = 1;
                j = j + numbers[i] + 1;
                cr = cr + 2;
            }
            cr = cr - 1;
            if (j > cutLen) cr = cr - 1;
            if (j < cutLen) numbers10.arr[cr].c = numbers10.arr[cr].c + cutLen - j;
            //-------
            b1 = true;
            combinationCount = 0;
            while (b1) {
                getCombinationsFromNumbers();
                if (testCombination()) {
                    combinationCount = combinationCount + 1;

                    int i0 = cutFrom;
                    int leni = cutTo;
                    for (int i = i0; i <= leni; i++) {
                        if (combinations[i]) {
                            probability[i] = probability[i] + 1;
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
            if (combinationCount == 0) {
                return result;
            } else {
                result = true;
            }
        }
        //-----------
        for (int i = 1; i <= len; i++) {
            if (probability[i] == EXACTLY_BLACK) {
                dots[i] = Solver.Dot.BLACK;
            }
            if (probability[i] == EXACTLY_WHITE) {
                dots[i] = Solver.Dot.WHITE;
            }
        }

        return result;
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
            for (int j = 1; j <= numbers10.arr[i].c; j++) {
                combinations[x + j] = numbers10.arr[i].b;
            }
            x = x + numbers10.arr[i].c;
        }
    }

    private void getNumbersFromCombination() {
        int j, cr;
        int leni, i0;
        boolean b = combinations[cutFrom];
        j = 1;
        cr = 1;

        i0 = (cutFrom + 1);
        leni = cutTo;
        for (int i = i0; i <= leni; i++) {
            if (combinations[i] ^ b) {
                numbers10.arr[cr].c = j;
                numbers10.arr[cr].b = b;
                cr++; ;
                j = 0;
            }
            j++;
            b = combinations[i];
        }
        if (j != 0) {
            numbers10.arr[cr].c = j;
            numbers10.arr[cr].b = b;
        }
        this.cr = cr;
    }

    private boolean manipuleNumbers() {
        int a, a2;
        boolean b, b2 = false;
        a = cr;
        boolean Result = true;
        while (true) {
            if (numbers10.arr[a].b) {
                a = a - 1;
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
                        if (!numbers10.arr[cr].b) break;

                        b2 = false;
                        if ((numbers10.arr[a].c != 1) && (cr == 2)) {
                            b2 = true;
                        }
                        while (numbers10.arr[a].c == 1) {
                            a = a - 2;
                            a2 = a2 + 2;
                            if ((a <= 0) || (a2 == cr)) {
                                b2 = true;
                                break;
                            }
                        }

                        if (b2) break;

                        cr++;
                        numbers10.arr[cr].b = false;
                        numbers10.arr[cr].c = 0;
                    }
                    numbers10.arr[a + a2].c = numbers10.arr[a + a2].c + numbers10.arr[a].c - 2;
                    numbers10.arr[a].c = 2;
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
                        numbers10.arr[i + 1].c = numbers10.arr[i].c;
                        numbers10.arr[i + 1].b = numbers10.arr[i].b;
                    }
                    numbers10.arr[1].c = 1;
                    numbers10.arr[1].b = false;
                    if (cr > 3) {
                        numbers10.arr[3].c = 1;
                    }
                    if (cr == 3) {
                        numbers10.arr[3].c = numbers10.arr[3].c - 1;
                    }
                    break;
                }
                numbers10.arr[a - 2].c = numbers10.arr[a - 2].c + 1;
                numbers10.arr[a].c = numbers10.arr[a].c - 1;
                if (numbers10.arr[a].c == 0) {
                    if (a == cr) {
                        cr--;
                        break;
                    } else {
                        numbers10.arr[a - 2].c = numbers10.arr[a - 2].c - 1;
                        numbers10.arr[a].c = numbers10.arr[a].c + 1;
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
        } // TODO может нижняя строчка тоже
        countNumbers = countNumbers - 1;
    }

    private boolean cut() {
        int i, dr, cd;
        boolean b, dot;
        b = false; // выход из цикла
        i = 1; // по ряду
        dot = false; // предидущая - точка, нет
        dr = 0; // первый ряд точек
        cd = 0; // количество точек
        cutFrom = i;
        boolean result;
        do {
            switch (dots[i]) {
                case UNSET: {
                    if (dot) { // предидущая точка?
                        // ничего после точки - надо заканчивать ряд
                        if (cd < numbers[dr]) { // dr - ый ряд закончили?
                            // незакончили
                            cd = cd + 1; // количество точек
                            probability[i] = EXACTLY_BLACK; // потом тут будет точка
                            dot = true;  // поставили точку
                        } else { // закончили ряд
                            cd = 0; // новы ряд еще не начали
                            probability[i] = EXACTLY_WHITE; // потом тут будет пустота
                            SHLNumbers(); // сдвигаем ряд (удаляем первый элемент)
                            dot = false;  // поставили пустоту
                            dr = dr - 1; // из за смещения
                        }
                    } else { // ничего после пустоты - выходим вообшето
                        if (countNumbers == 0) { // в этом ряде ничего больше делать нечего
                            probability[i] = EXACTLY_WHITE; // канчаем его:) // /TODO тут странно ставить white
                        } else {
                            cutFrom = i;
                            b = true; // иначе выходим
                        }
                    }
                }
                break;
                case BLACK: {
                    if (!dot) {
                        dr = dr + 1;
                        cd = 0;
                        dot = true; // точка у нас
                    }
                    probability[i] = EXACTLY_BLACK; // потом тут будет точка
                    cd = cd + 1; // точек стало больше
                }
                break;
                case WHITE: {
                    if (dot) { // предыдущая - точка ?
                        // да
                        if (cd != numbers[dr]) {
                            result = false;
                            return result;
                        }
                        dot = false; // теперь точки нет
                        SHLNumbers(); // сдвигаем ряд (удаляем первый элемент)
                        dr = dr - 1; // из за смещения
                    }
                    probability[i] = EXACTLY_WHITE; // пустота
                }
                break;
            }
            i++;
            if ((!b) & (i > len) | (countNumbers == 0)) { // достигли конца
                result = true;
                return result;
            }
        } while (!b);

        b = false; // выход из цикла
        i = len; // по ряду
        dot = false; // предидущая - точка, нет
        dr = countNumbers + 1; // первый ряд точек
        cd = 0; // количество точек
        cutTo = i;
        do {
            switch (dots[i]) {
                case UNSET: {
                    if (dot) // предидущая точка?
                    { // ничего после точки - надо заканчивать ряд
                        if (cd < numbers[dr]) { // dr - ый ряд закончили?
                            // незакончили
                            cd = cd + 1; // количество точек
                            probability[i] = EXACTLY_BLACK; // потом тут будет точка
                            dot = true;  // поставили точку
                        } else { // закончили ряд
                            cd = 0; // новы ряд еще не начали
                            probability[i] = EXACTLY_WHITE; // потом тут будет пустота
                            countNumbers = countNumbers - 1;
                            dot = false; // поставили пустоту
                        }
                    } else { // ничего после пустоты - выходим вообшето
                        if (countNumbers == 0) { // в этом ряде ничего больше делать нечего
                            probability[i] = EXACTLY_WHITE; // канчаем его:) // /TODO тут странно ставить white
                        } else {
                            cutTo = i;
                            b = true; // иначе выходим
                        }
                    }
                }
                break;
                case BLACK: {
                    if (!dot) {
                        dr = dr - 1;
                        cd = 0;
                        dot = true; // точка у нас
                    }
                    probability[i] = EXACTLY_BLACK; // потом тут будет точка
                    cd = cd + 1; // точек стало больше
                }
                break;
                case WHITE: {
                    if (dot) { // предидущая - точка ?
                        // да
                        if (cd != numbers[dr]) {
                            result = false;
                            return result;
                        }
                        dot = false; // теперь точки нет
                        countNumbers = countNumbers - 1;
                    }
                    probability[i] = EXACTLY_WHITE; // пустота
                }
                break;
            }
            i--;
            if ((!b) && (i < 1) || (countNumbers == 0)) { // достигли конца
                result = true;
                return result;
            }
        } while (!b);

        cutLen = cutTo - cutFrom + 1;
        result = ((cutFrom > cutTo) || (countNumbers == 0));
        return result;
    }

    public double probability(int x) {
        return probability[x];
    }

    public Dot array(int x) {
        return dots[x];
    }
}
