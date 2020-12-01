package com.codenjoy.dojo.japanese.model.portable;

class Unit2 {

    public static final int MaxLen = 150;
    public TData glData = new TData();
    public TVerArray glVer = new TVerArray();
    public TBitArray glCurrComb = new TBitArray();

    public int glCombNum;
    public int glLen;
    public int glCR;

    public TRjad10 glRjad10 = new TRjad10();
    public TRjad glRjad = new TRjad();
    // обрез
    public int glCutFrom;
    public int glCutTo;
    public int glCutLen;
    public int glCountRjad;

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean Calculate() {
        int j, i0, leni;
        boolean b1;
        boolean Result;
        if (glCountRjad == 0) {
            Result = true;
            for (int i = 1; i <= glLen; i++) {
                if (glData.arr[i] == 1) {
                    Result = false;
                } else {
                    glData.arr[i] = 2;
                }
            }
            return Result;
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
//                Result = true;
//                return Result;
//            }
//        }
        //-----------
        for (int i = 1; i <= glLen; i++) {
            glVer.arr[i] = 0;
        }
        //-----------
        Result = true;
        if (!Cut()) {
            Result = false;
            glCR = 1;
            j = 0;
            for (int i = 1; i <= glCountRjad; i++) {
                glRjad10.arr[glCR].b = true;
                glRjad10.arr[glCR].c = glRjad.arr[i];
                glRjad10.arr[glCR + 1].b = false;
                glRjad10.arr[glCR + 1].c = 1;
                j = j + glRjad.arr[i] + 1;
                glCR = glCR + 2;
            }
            glCR = glCR - 1;
            if (j > glCutLen) glCR = glCR - 1;
            if (j < glCutLen) glRjad10.arr[glCR].c = glRjad10.arr[glCR].c + glCutLen - j;
            //-------
            b1 = true;
            glCombNum = 0;
            while (b1) {
                GetCombFromRjad();
                if (TestComb()) {
                    glCombNum = glCombNum + 1;

                    i0 = glCutFrom;
                    leni = glCutTo;
                    for (int i = i0; i <= leni; i++)
                        if (glCurrComb.arr[i])
                            glVer.arr[i] = glVer.arr[i] + 1;
                }
                GetRjadFromComb();
                b1 = ManipuleRjad();
            }

            for (var i = glCutFrom; i <= glCutTo; i++) {
                if (glCombNum != 0) {
                    glVer.arr[i] = glVer.arr[i] / glCombNum;
                } else {
                    glVer.arr[i] = -1;
                }
            }
            if (glCombNum == 0) {
                return Result;
            } else {
                Result = true;
            }
        }
        //-----------
        for (int i = 1; i <= glLen; i++) {
            if (glVer.arr[i] == 1) glData.arr[i] = 1;
            if (glVer.arr[i] == 0) glData.arr[i] = 2;
        }

        return Result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean TestComb() {
        boolean Result = true;
        for (int i = glCutFrom; i <= glCutTo; i++) {
            switch (glData.arr[i]) {
                case 0:
                    break;                                         // ничего нет
                case 1:
                    if (glCurrComb.arr[i] != true) ;
                    Result = false;
                    break;  // точка
                case 2:
                    if (glCurrComb.arr[i] != false) ;
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
    public void GetCombFromRjad() {
        int x;
        x = glCutFrom - 1;
        for (int i = 1; i <= glCR; i++) {
            for (int j = 1; j <= glRjad10.arr[i].c; j++)
                glCurrComb.arr[x + j] = glRjad10.arr[i].b;
            x = x + glRjad10.arr[i].c;
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void GetRjadFromComb() {
        int j, cr;
        int leni, i0;
        boolean b = glCurrComb.arr[glCutFrom];
        j = 1;
        cr = 1;

        i0 = (glCutFrom + 1);
        leni = glCutTo;
        for (int i = i0; i <= leni; i++) {
            if (glCurrComb.arr[i] ^ b) {
                glRjad10.arr[cr].c = j;
                glRjad10.arr[cr].b = b;
                cr++; ;
                j = 0;
            }
            j++;
            b = glCurrComb.arr[i];
        }
        if (j != 0) {
            glRjad10.arr[cr].c = j;
            glRjad10.arr[cr].b = b;
        }
        glCR = cr;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean ManipuleRjad() {
        int a, a2;
        boolean b, b2 = false;
        a = glCR;
        boolean Result = true;
        while (true) {
            if (glRjad10.arr[a].b) {
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

                    if ((a + 2) > glCR) {
                        if ((a + 2) != (glCR + 1)) break;
                        if (!glRjad10.arr[glCR].b) break;

                        b2 = false;
                        if ((glRjad10.arr[a].c != 1) && (glCR == 2)) b2 = true;
                        while (glRjad10.arr[a].c == 1) {
                            a = a - 2;
                            a2 = a2 + 2;
                            if ((a <= 0) || (a2 == glCR)) {
                                b2 = true;
                                break;
                            }
                        }

                        if (b2) break;

                        glCR++;
                        glRjad10.arr[glCR].b = false;
                        glRjad10.arr[glCR].c = 0;
                    }
                    glRjad10.arr[a + a2].c = glRjad10.arr[a + a2].c + glRjad10.arr[a].c - 2;
                    glRjad10.arr[a].c = 2;
                    break;
                }
                if (b2) {
                    Result = false;
                    break;
                }
            } else {
                if (glCR == 1) {
                    Result = false;
                    break;
                }
                if ((a - 2) <= 0) {
                    if (a < 1) break;
                    glCR++;
                    for (int i = (glCR - 1); i >= 1; i++) { // TODO downto
                        glRjad10.arr[i + 1].c = glRjad10.arr[i].c;
                        glRjad10.arr[i + 1].b = glRjad10.arr[i].b;
                    }
                    glRjad10.arr[1].c = 1;
                    glRjad10.arr[1].b = false;
                    if (glCR > 3) glRjad10.arr[3].c = 1;
                    if (glCR == 3) glRjad10.arr[3].c = glRjad10.arr[3].c - 1;
                    break;
                }
                glRjad10.arr[a - 2].c = glRjad10.arr[a - 2].c + 1;
                glRjad10.arr[a].c = glRjad10.arr[a].c - 1;
                if (glRjad10.arr[a].c == 0) {
                    if (a == glCR) {
                        glCR--;
                        break;
                    } else {
                        glRjad10.arr[a - 2].c = glRjad10.arr[a - 2].c - 1;
                        glRjad10.arr[a].c = glRjad10.arr[a].c + 1;
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
        for (int j = 2; j <= glCountRjad; j++) {
            glRjad.arr[j - 1] = glRjad.arr[j];
        } // TODO может нижняя строчка тоже
        glCountRjad = glCountRjad - 1;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean Cut() {
        int i, dr, cd;
        boolean b, bDot;
        b = false; // выход из цикла
        i = 1; // по ряду
        bDot = false; // предидущая - точка, нет
        dr = 0; // первый ряд точек
        cd = 0; // количество точек
        glCutFrom = i;
        boolean Result = true;
        do {
            switch (glData.arr[i]) { //
                case 0: { // ничего
                    if (bDot) { // предидущая точка?
                        // ничего после точки - надо заканчивать ряд
                        if (cd < glRjad.arr[dr]) { // dr - ый ряд закончили?
                            // незакончили
                            cd = cd + 1; // количество точек
                            glVer.arr[i] = 1; // потом тут будет точка
                            bDot = true;  // поставили точку
                        } else { // закончили ряд
                            cd = 0; // новы ряд еще не начали
                            glVer.arr[i] = 0; // потом тут будет пустота
                            SHLRjad(); // сдвигаем ряд (удаляем первый элемент)
                            bDot = false;  // поставили пустоту
                            dr = dr - 1; // из за смещения
                        }
                    } else { // ничего после пустоты - выходим вообшето
                        if (glCountRjad == 0) { // в этом ряде ничего больше делать нечего
                            glVer.arr[i] = 0; // канчаем его:)
                        } else {
                            glCutFrom = i;
                            b = true; // иначе выходим
                        }
                    }
                }
                break;
                case 1: { // точка
                    if (!bDot) {
                        dr = dr + 1;
                        cd = 0;
                        bDot = true; // точка у нас
                    }
                    glVer.arr[i] = 1; // потом тут будет точка
                    cd = cd + 1; // точек стало больше
                }
                break;
                case 2: { // пустота
                    if (bDot) { // предидущая - точка ?
                        // да
                        if (cd != glRjad.arr[dr]) {
                            Result = false;
                            return Result;
                        }
                        bDot = false; // теперь точки нет
                        SHLRjad(); // сдвигаем ряд (удаляем первый элемент)
                        dr = dr - 1; // из за смещения
                    }
                    glVer.arr[i] = 0; // пустота
                }
                break;
            }
            i++;
            if ((!b) && (i > glLen) || (glCountRjad == 0)) { // достигли конца
                Result = true;
                return Result;
            }
        } while (b);

        b = false; // выход из цикла
        i = glLen; // по ряду
        bDot = false; // предидущая - точка, нет
        dr = glCountRjad + 1; // первый ряд точек
        cd = 0; // количество точек
        glCutTo = i;
        do {
            switch (glData.arr[i]) { //
                case 0: { // ничего
                    if (bDot) // предидущая точка?
                    { // ничего после точки - надо заканчивать ряд
                        if (cd < glRjad.arr[dr]) { // dr - ый ряд закончили?
                            // незакончили
                            cd = cd + 1; // количество точек
                            glVer.arr[i] = 1; // потом тут будет точка
                            bDot = true;  // поставили точку
                        } else { // закончили ряд
                            cd = 0; // новы ряд еще не начали
                            glVer.arr[i] = 0; // потом тут будет пустота
                            glCountRjad = glCountRjad - 1;
                            bDot = false; // поставили пустоту
                        }
                    } else { // ничего после пустоты - выходим вообшето
                        if (glCountRjad == 0) { // в этом ряде ничего больше делать нечего
                            glVer.arr[i] = 0; // канчаем его:)
                        } else {
                            glCutTo = i;
                            b = true; // иначе выходим
                        }
                    }
                }
                break;
                case 1: { // точка
                    if (!bDot) {
                        dr = dr - 1;
                        cd = 0;
                        bDot = true; // точка у нас
                    }
                    glVer.arr[i] = 1; // потом тут будет точка
                    cd = cd + 1; // точек стало больше
                }
                break;
                case 2: { // пустота
                    if (bDot) { // предидущая - точка ?
                        // да
                        if (cd != glRjad.arr[dr]) {
                            Result = false;
                            return Result;
                        }
                        bDot = false; // теперь точки нет
                        glCountRjad = glCountRjad - 1;
                    }
                    glVer.arr[i] = 0; // пустота
                }
                break;
            }
            i--;
            if ((!b) && (i < 1) || (glCountRjad == 0)) { // достигли конца
                Result = true;
                return Result;
            }
        } while (b);

        glCutLen = glCutTo - glCutFrom + 1;
        Result = ((glCutFrom > glCutTo) || (glCountRjad == 0));
        return Result;
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
        public int[] arr = new int[MaxLen + 1];
    }

    public static class TRjad10Record {
        public int c;
        public boolean b;
    }

    public static class TRjad10 {
        public TRjad10Record[] arr = new TRjad10Record[MaxLen + 1];
    }

    public static class TRjad {
        public int[] arr = new int[MaxLen + 1];
    }

    public static class TBitArray {
        public boolean[] arr = new boolean[MaxLen + 1];
    }

    public static class TVerArray {
        public double[] arr = new double[MaxLen + 1];
    }
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}