package com.codenjoy.dojo.japanese.model.portable;

class Unit2 {

    public static final int MAX = 150;
    public TData data = new TData();
    public TProbability probability = new TProbability();
    public TBitArray combinations = new TBitArray();

    public int combinationCount;
    public int len;
    public int cr;

    public TRjad10 rjad10 = new TRjad10();
    public TRjad rjad = new TRjad();
    // обрез
    public int cutFrom;
    public int cutTo;
    public int cutLen;
    public int countRjad;

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean calculate() {
        int j, i0, leni;
        boolean b1;
        boolean result;
        if (countRjad == 0) {
            result = true;
            for (int i = 1; i <= len; i++) {
                if (data.arr[i] == 1) {
                    result = false;
                } else {
                    data.arr[i] = 2;
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
            probability.arr[i] = 0;
        }
        //-----------
        result = true;
        if (!Cut()) {
            result = false;
            cr = 1;
            j = 0;
            for (int i = 1; i <= countRjad; i++) {
                rjad10.arr[cr].b = true;
                rjad10.arr[cr].c = rjad.arr[i];
                rjad10.arr[cr + 1].b = false;
                rjad10.arr[cr + 1].c = 1;
                j = j + rjad.arr[i] + 1;
                cr = cr + 2;
            }
            cr = cr - 1;
            if (j > cutLen) cr = cr - 1;
            if (j < cutLen) rjad10.arr[cr].c = rjad10.arr[cr].c + cutLen - j;
            //-------
            b1 = true;
            combinationCount = 0;
            while (b1) {
                getCombinationsFromRjad();
                if (testCombination()) {
                    combinationCount = combinationCount + 1;

                    i0 = cutFrom;
                    leni = cutTo;
                    for (int i = i0; i <= leni; i++)
                        if (combinations.arr[i])
                            probability.arr[i] = probability.arr[i] + 1;
                }
                getRjadFromCombination();
                b1 = ManipuleRjad();
            }

            for (var i = cutFrom; i <= cutTo; i++) {
                if (combinationCount != 0) {
                    probability.arr[i] = probability.arr[i] / combinationCount;
                } else {
                    probability.arr[i] = -1;
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
            if (probability.arr[i] == 1) data.arr[i] = 1;
            if (probability.arr[i] == 0) data.arr[i] = 2;
        }

        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean testCombination() {
        boolean Result = true;
        for (int i = cutFrom; i <= cutTo; i++) {
            switch (data.arr[i]) {
                case 0:
                    break;                                         // ничего нет
                case 1:
                    if (combinations.arr[i] != true) ;
                    Result = false;
                    break;  // точка
                case 2:
                    if (combinations.arr[i] != false) ;
                    Result = false;
                    break; // пустота
                case 3:
                    break;                                         // предполагаемая точка
            }
            if (!Result) return Result;
        }
        return Result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void getCombinationsFromRjad() {
        int x;
        x = cutFrom - 1;
        for (int i = 1; i <= cr; i++) {
            for (int j = 1; j <= rjad10.arr[i].c; j++) {
                combinations.arr[x + j] = rjad10.arr[i].b;
            }
            x = x + rjad10.arr[i].c;
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void getRjadFromCombination() {
        int j, cr;
        int leni, i0;
        boolean b = combinations.arr[cutFrom];
        j = 1;
        cr = 1;

        i0 = (cutFrom + 1);
        leni = cutTo;
        for (int i = i0; i <= leni; i++) {
            if (combinations.arr[i] ^ b) {
                rjad10.arr[cr].c = j;
                rjad10.arr[cr].b = b;
                cr++; ;
                j = 0;
            }
            j++;
            b = combinations.arr[i];
        }
        if (j != 0) {
            rjad10.arr[cr].c = j;
            rjad10.arr[cr].b = b;
        }
        this.cr = cr;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean ManipuleRjad() {
        int a, a2;
        boolean b, b2 = false;
        a = cr;
        boolean Result = true;
        while (true) {
            if (rjad10.arr[a].b) {
                a = a - 1;
                if (a <= 0) {
                    Result = false;
                    break;
                }
                b = true;
                while (true) {
                    a2 = 2;
                    if (!b)
                        continue;
                    b = !b;

                    if ((a + 2) > cr) {
                        if ((a + 2) != (cr + 1)) break;
                        if (!rjad10.arr[cr].b) break;

                        b2 = false;
                        if ((rjad10.arr[a].c != 1) && (cr == 2)) b2 = true;
                        while (rjad10.arr[a].c == 1) {
                            a = a - 2;
                            a2 = a2 + 2;
                            if ((a <= 0) || (a2 == cr)) {
                                b2 = true;
                                break;
                            }
                        }

                        if (b2) break;

                        cr++;
                        rjad10.arr[cr].b = false;
                        rjad10.arr[cr].c = 0;
                    }
                    rjad10.arr[a + a2].c = rjad10.arr[a + a2].c + rjad10.arr[a].c - 2;
                    rjad10.arr[a].c = 2;
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
                        rjad10.arr[i + 1].c = rjad10.arr[i].c;
                        rjad10.arr[i + 1].b = rjad10.arr[i].b;
                    }
                    rjad10.arr[1].c = 1;
                    rjad10.arr[1].b = false;
                    if (cr > 3) rjad10.arr[3].c = 1;
                    if (cr == 3) rjad10.arr[3].c = rjad10.arr[3].c - 1;
                    break;
                }
                rjad10.arr[a - 2].c = rjad10.arr[a - 2].c + 1;
                rjad10.arr[a].c = rjad10.arr[a].c - 1;
                if (rjad10.arr[a].c == 0) {
                    if (a == cr) {
                        cr--;
                        break;
                    } else {
                        rjad10.arr[a - 2].c = rjad10.arr[a - 2].c - 1;
                        rjad10.arr[a].c = rjad10.arr[a].c + 1;
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

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void SHLRjad() {
        for (int j = 2; j <= countRjad; j++) {
            rjad.arr[j - 1] = rjad.arr[j];
        } // TODO может нижняя строчка тоже
        countRjad = countRjad - 1;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean Cut() {
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
            switch (data.arr[i]) { //
                case 0: { // ничего
                    if (dot) { // предидущая точка?
                        // ничего после точки - надо заканчивать ряд
                        if (cd < rjad.arr[dr]) { // dr - ый ряд закончили?
                            // незакончили
                            cd = cd + 1; // количество точек
                            probability.arr[i] = 1; // потом тут будет точка
                            dot = true;  // поставили точку
                        } else { // закончили ряд
                            cd = 0; // новы ряд еще не начали
                            probability.arr[i] = 0; // потом тут будет пустота
                            SHLRjad(); // сдвигаем ряд (удаляем первый элемент)
                            dot = false;  // поставили пустоту
                            dr = dr - 1; // из за смещения
                        }
                    } else { // ничего после пустоты - выходим вообшето
                        if (countRjad == 0) { // в этом ряде ничего больше делать нечего
                            probability.arr[i] = 0; // канчаем его:)
                        } else {
                            cutFrom = i;
                            b = true; // иначе выходим
                        }
                    }
                }
                break;
                case 1: { // точка
                    if (!dot) {
                        dr = dr + 1;
                        cd = 0;
                        dot = true; // точка у нас
                    }
                    probability.arr[i] = 1; // потом тут будет точка
                    cd = cd + 1; // точек стало больше
                }
                break;
                case 2: { // пустота
                    if (dot) { // предыдущая - точка ?
                        // да
                        if (cd != rjad.arr[dr]) {
                            result = false;
                            return result;
                        }
                        dot = false; // теперь точки нет
                        SHLRjad(); // сдвигаем ряд (удаляем первый элемент)
                        dr = dr - 1; // из за смещения
                    }
                    probability.arr[i] = 0; // пустота
                }
                break;
            }
            i++;
            if ((!b) & (i > len) | (countRjad == 0)) { // достигли конца
                result = true;
                return result;
            }
        } while (!b);

        b = false; // выход из цикла
        i = len; // по ряду
        dot = false; // предидущая - точка, нет
        dr = countRjad + 1; // первый ряд точек
        cd = 0; // количество точек
        cutTo = i;
        do {
            switch (data.arr[i]) { //
                case 0: { // ничего
                    if (dot) // предидущая точка?
                    { // ничего после точки - надо заканчивать ряд
                        if (cd < rjad.arr[dr]) { // dr - ый ряд закончили?
                            // незакончили
                            cd = cd + 1; // количество точек
                            probability.arr[i] = 1; // потом тут будет точка
                            dot = true;  // поставили точку
                        } else { // закончили ряд
                            cd = 0; // новы ряд еще не начали
                            probability.arr[i] = 0; // потом тут будет пустота
                            countRjad = countRjad - 1;
                            dot = false; // поставили пустоту
                        }
                    } else { // ничего после пустоты - выходим вообшето
                        if (countRjad == 0) { // в этом ряде ничего больше делать нечего
                            probability.arr[i] = 0; // канчаем его:)
                        } else {
                            cutTo = i;
                            b = true; // иначе выходим
                        }
                    }
                }
                break;
                case 1: { // точка
                    if (!dot) {
                        dr = dr - 1;
                        cd = 0;
                        dot = true; // точка у нас
                    }
                    probability.arr[i] = 1; // потом тут будет точка
                    cd = cd + 1; // точек стало больше
                }
                break;
                case 2: { // пустота
                    if (dot) { // предидущая - точка ?
                        // да
                        if (cd != rjad.arr[dr]) {
                            result = false;
                            return result;
                        }
                        dot = false; // теперь точки нет
                        countRjad = countRjad - 1;
                    }
                    probability.arr[i] = 0; // пустота
                }
                break;
            }
            i--;
            if ((!b) && (i < 1) || (countRjad == 0)) { // достигли конца
                result = true;
                return result;
            }
        } while (!b);

        cutLen = cutTo - cutFrom + 1;
        result = ((cutFrom > cutTo) || (countRjad == 0));
        return result;
    }

    public static class TPoint {
        public int x;
        public int y;

        public TPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class TDataPt {
        public TPoint Pos, Ass;
        public byte Check; // 0 - пусто 1 - чек 2 - нечек
    }

    public static class TData {
        public int[] arr = new int[MAX + 1];
    }

    public static class TRjad10Record {
        public int c;
        public boolean b;
    }

    public static class TRjad10 {
        public TRjad10Record[] arr = new TRjad10Record[MAX + 1];

        public TRjad10() {
            for (int i = 1; i < arr.length; i++) {
                arr[i] = new TRjad10Record();
            }
        }
    }

    public static class TRjad {
        public int[] arr = new int[MAX + 1];
    }

    public static class TBitArray {
        public boolean[] arr = new boolean[MAX + 1];
    }

    public static class TProbability {
        public double[] arr = new double[MAX + 1];
    }
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}