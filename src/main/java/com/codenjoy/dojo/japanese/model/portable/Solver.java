package com.codenjoy.dojo.japanese.model.portable;

import com.codenjoy.dojo.japanese.model.items.Color;
import com.codenjoy.dojo.japanese.model.items.Nan;
import com.codenjoy.dojo.japanese.model.items.Number;
import com.codenjoy.dojo.japanese.model.items.Pixel;
import com.codenjoy.dojo.japanese.model.level.Level;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

class Solver implements BoardReader {

    public TPoint previous;
    boolean mode;
    boolean tryAssumption = false; // гадать ли алгоритму, если нет вариантов точных на поле
    TCurrent current = new TCurrent();
    TAllData main = new TAllData(); // тут решение точное
    TAllData assumptionBlack = new TAllData(); // тут предполагаем black
    TAllData assumptionWhite = new TAllData();// тут предполагаем white
    TAssumption assumption = new TAssumption(); //данные предположения
    static int lenX = 15, lenY = 15; // длинна и высота кроссворда
    int[][] numbersX = new int[MAX + 1][MAX + 1]; // тут хранятся цифры рядов
    int[][] numbersY = new int[MAX + 1][MAX + 1];
    int[] countNumbersX = new int[MAX + 1]; // тут хранятся количества цифер рядов
    int[] countNumbersY = new int[MAX + 1];
    static final int MAX = 150;
    int[] array = new int[MAX + 1];
    double[] probability = new double[MAX + 1];
    boolean[] combinations = new boolean[MAX + 1];
    int combinationCount;
    int len;
    int cr;
    TNumbers10 numbers10 = new TNumbers10();
    int[] numbers = new int[MAX + 1];
    int cutFrom;
    int cutTo;
    int cutLen;
    int countRjad;
    int offset;

    public Solver() {
        init();
    }

    public boolean calculate() {
        int j, i0, leni;
        boolean b1;
        boolean result;
        if (countRjad == 0) {
            result = true;
            for (int i = 1; i <= len; i++) {
                if (array[i] == 1) {
                    result = false;
                } else {
                    array[i] = 2;
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
            probability[i] = 0;
        }
        //-----------
        result = true;
        if (!cut()) {
            result = false;
            cr = 1;
            j = 0;
            for (int i = 1; i <= countRjad; i++) {
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

                    i0 = cutFrom;
                    leni = cutTo;
                    for (int i = i0; i <= leni; i++)
                        if (combinations[i])
                            probability[i] = probability[i] + 1;
                }
                getNumbersFromCombination();
                b1 = manipuleNumbers();
            }

            for (var i = cutFrom; i <= cutTo; i++) {
                if (combinationCount != 0) {
                    probability[i] = probability[i] / combinationCount;
                } else {
                    probability[i] = -1;
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
            if (probability[i] == 1) array[i] = 1;
            if (probability[i] == 0) array[i] = 2;
        }

        return result;
    }

    public boolean testCombination() {
        boolean Result = true;
        for (int i = cutFrom; i <= cutTo; i++) {
            switch (array[i]) {
                case 0:
                    break;                                         // ничего нет
                case 1:
                    if (combinations[i] != true) ;
                    Result = false;
                    break;  // точка
                case 2:
                    if (combinations[i] != false) ;
                    Result = false;
                    break; // пустота
                case 3:
                    break;                                         // предполагаемая точка
            }
            if (!Result) return Result;
        }
        return Result;
    }

    public void getCombinationsFromNumbers() {
        int x;
        x = cutFrom - 1;
        for (int i = 1; i <= cr; i++) {
            for (int j = 1; j <= numbers10.arr[i].c; j++) {
                combinations[x + j] = numbers10.arr[i].b;
            }
            x = x + numbers10.arr[i].c;
        }
    }

    public void getNumbersFromCombination() {
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

    public boolean manipuleNumbers() {
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
                    if (!b)
                        continue;
                    b = !b;

                    if ((a + 2) > cr) {
                        if ((a + 2) != (cr + 1)) break;
                        if (!numbers10.arr[cr].b) break;

                        b2 = false;
                        if ((numbers10.arr[a].c != 1) && (cr == 2)) b2 = true;
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
                    if (cr > 3) numbers10.arr[3].c = 1;
                    if (cr == 3) numbers10.arr[3].c = numbers10.arr[3].c - 1;
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

    public void SHLNumbers() {
        for (int j = 2; j <= countRjad; j++) {
            numbers[j - 1] = numbers[j];
        } // TODO может нижняя строчка тоже
        countRjad = countRjad - 1;
    }

    public boolean cut() {
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
            switch (array[i]) { //
                case 0: { // ничего
                    if (dot) { // предидущая точка?
                        // ничего после точки - надо заканчивать ряд
                        if (cd < numbers[dr]) { // dr - ый ряд закончили?
                            // незакончили
                            cd = cd + 1; // количество точек
                            probability[i] = 1; // потом тут будет точка
                            dot = true;  // поставили точку
                        } else { // закончили ряд
                            cd = 0; // новы ряд еще не начали
                            probability[i] = 0; // потом тут будет пустота
                            SHLNumbers(); // сдвигаем ряд (удаляем первый элемент)
                            dot = false;  // поставили пустоту
                            dr = dr - 1; // из за смещения
                        }
                    } else { // ничего после пустоты - выходим вообшето
                        if (countRjad == 0) { // в этом ряде ничего больше делать нечего
                            probability[i] = 0; // канчаем его:)
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
                    probability[i] = 1; // потом тут будет точка
                    cd = cd + 1; // точек стало больше
                }
                break;
                case 2: { // пустота
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
                    probability[i] = 0; // пустота
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
            switch (array[i]) { //
                case 0: { // ничего
                    if (dot) // предидущая точка?
                    { // ничего после точки - надо заканчивать ряд
                        if (cd < numbers[dr]) { // dr - ый ряд закончили?
                            // незакончили
                            cd = cd + 1; // количество точек
                            probability[i] = 1; // потом тут будет точка
                            dot = true;  // поставили точку
                        } else { // закончили ряд
                            cd = 0; // новы ряд еще не начали
                            probability[i] = 0; // потом тут будет пустота
                            countRjad = countRjad - 1;
                            dot = false; // поставили пустоту
                        }
                    } else { // ничего после пустоты - выходим вообшето
                        if (countRjad == 0) { // в этом ряде ничего больше делать нечего
                            probability[i] = 0; // канчаем его:)
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
                    probability[i] = 1; // потом тут будет точка
                    cd = cd + 1; // точек стало больше
                }
                break;
                case 2: { // пустота
                    if (dot) { // предидущая - точка ?
                        // да
                        if (cd != numbers[dr]) {
                            result = false;
                            return result;
                        }
                        dot = false; // теперь точки нет
                        countRjad = countRjad - 1;
                    }
                    probability[i] = 0; // пустота
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

    public String printData() {
        return (String)new PrinterFactoryImpl<>().getPrinter(
                main, null).print();
    }
    
    public String printAll() {
        return (String)new PrinterFactoryImpl<>().getPrinter(
                this, null).print();
    }
    
    public void clear(boolean all) {
        for (int x = 1; x <= MAX; x++) {
            for (int y = 1; y <= MAX; y++) {
                main.data[x][y] = 0;
                main.ver[x][y][1] = -1;
                main.ver[x][y][2] = -1;
            }
            if (all) {
                countNumbersX[x] = 0;
                countNumbersY[x] = 0;
            }
            main.finY[x] = false;
            main.finX[x] = false;
            main.chY[x] = true;
            main.chX[x] = true;
        }
    }
    
    private void init() {
        main = new TAllData();
        assumptionBlack = new TAllData();
        assumptionWhite = new TAllData();
        previous = new TPoint(-1, -1);
        current.xy = true;
        current.pt = new TPoint(1, 1);
        lenX = 15;
        lenY = 15;
        assumption.dot = false;
        assumption.b = false;
    }
    
    public void GetRjadX() {
        int a;
        for (int y = 1; y <= lenY; y++) {
            a = 0;
            countNumbersX[y] = 1;
            for (int x = 1; x <= lenX; x++) {
                if (main.data[x][y] == 1) {
                    a++;
                } else {
                    if (a != 0) {
                        numbersX[countNumbersX[y]][y] = a;
                        a = 0;
                        countNumbersX[y] = countNumbersX[y] + 1;
                    }
                }
            }
            if (a != 0) {
                numbersX[countNumbersX[y]][y] = a;
                countNumbersX[y] = countNumbersX[y] + 1;
            }
            countNumbersX[y] = countNumbersX[y] - 1;
        }
    }
    
    public void GetRjadY() {
        int a;
        for (int x = 1; x <= lenX; x++) {
            a = 0;
            countNumbersY[x] = 1;
            for (int y = 1; y <= lenY; y++) {
                if (main.data[x][y] == 1) {
                    a++;
                } else {
                    if (a != 0) {
                        numbersY[x][countNumbersY[x]] = a;
                        a = 0;
                        countNumbersY[x] = countNumbersY[x] + 1;
                    }
                }
            }
            if (a != 0) {
                numbersY[x][countNumbersY[x]] = a;
                countNumbersY[x] = countNumbersY[x] + 1;
            }
            countNumbersY[x] = countNumbersY[x] - 1;
        }
    }
    
    public void dataFromRjadX(int y) {
        int k, tx, j;
        for (tx = 1; tx <= lenX; tx++) {
            main.data[tx][y] = 0;
        }
        k = 1;
        tx = 1;
        while (tx <= countNumbersX[y]) {
            for (j = 1; j <= numbersX[tx][y]; j++) {
                main.data[k + j - 1][y] = 1;
            }
            main.data[k + j][y] = 0;
            k = k + j;
            tx++;
        }
        GetRjadY();
    }
    
    public void DataFromRjadY(int x) {
        int k, j, ty;
        for (ty = 1; ty <= lenY; ty++) {
            main.data[x][ty] = 0;
        }
        k = 1;
        ty = 1;
        while (ty <= countNumbersY[x]) {
            for (j = 1; j <= numbersY[x][ty]; j++) {
                main.data[x][k + j - 1] = 1;
            }
            main.data[x][k + j] = 0;
            k = k + j;
            ty++;
        }
        GetRjadX();
    }

    public TPoint Check() {
        int a1, a2;
        a1 = 0;
        for (int x = 1; x <= lenX; x++) {
            for (int y = 1; y <= countNumbersY[x]; y++) {
                a1 = a1 + numbersY[x][y];
            }
        }
        a2 = 0;
        for (int y = 1; y <= lenY; y++) {
            for (int x = 1; x <= countNumbersX[y]; x++) {
                a2 = a2 + numbersX[x][y];
            }
        }
        return new TPoint(a1, a2);  // разница рядов
    }

    
    public void RefreshPole() {
        System.out.println();
    }
    
    public void solve() {
        boolean b, b2, b5, b6, b7, b8, b9, b11; // b - произошли ли изменения, b2 - была ли ошибка, b3 - , b4 - , b5 - предполагать максимальной вероятности с учетом массива NoSet, b6 - если точка с максимальной вероятностью была найдена, b7 - последний прогон для нормального отображения вероятностей, b8 - если нажали остановить, b9 - если остановка по ошибке, b11 - нудно для пропуска прогона по у если LenX больше LenY
        int h; 
        double MaxVer1, MaxVer2; 
        double a1, a2; 
        TPoint pt;
        TAllData data;
        boolean bErrT, bErrP;

        // проверка на совпадение рядов
        pt = Check();
        int x0 = Math.abs(pt.x - pt.y);
        if (x0 > 0) {
            System.out.println("Ошибка! Несовпадение на " + x0);
            return;
        }
        //-----------------------------
        // сам рачсет
        for (int x = 1; x <= lenX; x++) {
            h = 0;
            for (int y = 1; y <= countNumbersX[x]; y++) {
                h = h + numbersX[x][y];
            }
            if (h < (lenX / 2)) {
                main.chY[x] = false;
            }
        }
        for (int y = 1; y <= lenY; y++) {
            h = 0;
            for (int x = 1; x < countNumbersY[y]; x++) {
                h = h + numbersY[x][y];

            }
            if (h < (lenY / 2)) {
                main.chX[y] = false;
            }
        }
        //----------
        for (int x = 1; x <= lenX; x++) {
            main.tchY[x] = true;
            assumptionBlack.tchY[x] = true;
            assumptionWhite.tchY[x] = true;
        }
        for (int y = 1; y <= lenY; y++) {
            main.tchX[y] = true;
            assumptionBlack.tchX[y] = true;
            assumptionWhite.tchX[y] = true;
        }
        for (int x = 1; x <= lenX; x++) {
            for (int y = 1; y <= lenY; y++) {
                main.noSet[x][y] = false;
                assumptionBlack.noSet[x][y] = false;
                assumptionWhite.noSet[x][y] = false;
            }
        }
        b5 = false;
        b7 = false;
        b8 = false;
        b9 = false;
        // для пропуска прогона по у если в группе x и чисел меньше (значения больше) и длинна строки (группы) меньше, это все для ускорения
        b11 = (lenX > lenY); // нужно для пропуска прогона
        a1 = 0;
        for (int x = 1; x <= lenX; x++) {
            a1 = a1 + countNumbersY[x];
        }
        a2 = 0;
        for (int y = 1; y <= lenY; y++) {
            a2 = a2 + countNumbersX[y];
        }
        b11 = (a1 / lenY > a2 / lenX);
        //----------------------------------------------------------
        assumption.b = false;
        b = false; // для b11
        bErrT = false;
        bErrP = false;
        do {
            if (b && b11) b11 = false;
            b = false;
            b2 = false;
            // с каким указателем работаем
            if (assumption.b) {
                if (assumption.dot) {
                    data = assumptionBlack;
                } else {
                    data = assumptionWhite;
                }
            } else {
                data = main;
            }

            if (!(b5 || b11)) { // при поиску другой точки, или если LenX больше LenY (в начале) пропускаем этот шаг
                for (int y = 1; y <= lenY; y++) {
                    if (data.finX[y]) continue;
                    if (!data.chX[y]) continue;
                    countRjad = PrepRjadX(data, y, array, numbers); // подготовка строки
                    len = lenX; // длинна строки
                    if (!calculate()) { // расчет ... если нет ни одной комбины - ошибка
                        if (!tryAssumption) {
                            System.out.println("Ошибка в кроссворде (строка " + y + ").");
                            b9 = true;
                            break;
                        }
                        b = false; // изменений нету
                        b2 = true; // ошибка была
                        break;
                    }
                    for (int x = 1; x <= lenX; x++) {
                        data.ver[x][y][1] = probability[x];

                        if (data.data[x][y] != array[x]) {
                            data.data[x][y] = array[x];
                            if (!b) {
                                b = true; // b = true;
                            }
                            if (!data.chY[x]) {
                                data.chY[x] = true; // work.data.ChY.arr[x] = true;
                            }
                            if (assumption.b) {
                                if (!data.tchY[x]) {
                                    data.tchY[x] = true; // work.data.tChY.arr[x] = true;
                                }
                            }
                        }
                    }
                    data.chX[y] = false;
                }
                if (!assumption.b) RefreshPole(); // прорисовка поля только в случае точного расчета
            }
            if (!b9) {
                b9 = GetFin(data); // если небыло ошибки, то если сложили все b9 = GetFin; выходим как если была бы ошибка
            }

            if ((!b2) && (!b5) && (!b8) && (!b9)) { // если была ошибка (b2) или надо найти другую точку (b5) или принудительно заканчиваем (b8) или была ошибка (b9) то пропускаем этот шаг
                for (int x = 1; x <= lenX; x++) { // дальше то же только для столбцов
                    if (data.finY[x]) continue;
                    if (!data.chY[x]) continue;
                    countRjad = PrepRjadY(data, x, array, numbers);
                    len = lenY;
                    if (!calculate()) {
                        if (!tryAssumption) {
                            System.out.println("Ошибка в кроссворде (столбец " + x + ").");
                            b9 = true;
                            break;
                        }
                        b = false; // изменений нету
                        b2 = true; // ошибка была
                        break;
                    }

                    for (int y = 1; y <= lenY; y++) {
                        data.ver[x][y][2] = probability[y];

                        if (data.data[x][y] != array[y]) {
                            data.data[x][y] = array[y];
                            if (!b) {
                                b = true; // b = true;
                            }
                            if (!data.chX[y]) {
                                data.chX[y] = true; // work.data.ChX.arr[y] = true;
                            }
                            if (assumption.b) {
                                if (!data.tchY[x]) {
                                    data.tchY[x] = true; // work.data.tChY.arr[x] = true;
                                }
                            }
                        }
                    }
                    data.chY[x] = false;
                }
                if (!assumption.b) RefreshPole();// прорисовка поля
                if (b11) b = true; // чтобы после прогона по х пошел прогон по у
            }
            if (!b9)
                b9 = GetFin(data); // если небыло ошибки, то если сложили все b9 = GetFin; выходим как если была бы ошибка

            if (b7 || b8) b = false; // все конец
            if ((tryAssumption) && (!b) && (!b7) && (!b8) && (!b9)) { // если ничего не получается решить точно (b) и включено предположение (cbVerEnable.Checked) и последнего прогона нет (b7) и принудительно незавершали (b8) и ошибки нету
                if (b11) b11 = false;

                if (assumption.b) { // предполагали?
                    // да
                    if (assumption.dot) { // запоминаем ошибки
                        bErrT = b2;
                    } else {
                        bErrP = b2;
                    }
                    if (b2) b2 = false; // была
                    if (assumption.dot) { // что было
                        SavePustot(assumption.pt, false); //была точка, теперь пустота
                        b = true; // произошли изменения
                    } else { // путота, значит будем определять что нам записывать
                        if (bErrT) {
                            // ошибка на точке
                            if (bErrP) {
                                // ошибка на точке и на пустоте - ошибка в кроссворде
                                System.out.println("Ошибка в кроссворде.");
                                b9 = true;
                            } else { // ошибка на точке и нет ее на пустоте - значит пустота
                                ChangeDataArr(false);
                                RefreshPole();
                                b5 = true; // дальше предполагаем
                                b = true; // продолжаем дальше
                            }
                        } else {  // нет ошибки на точке
                            if (bErrP) { // нету на точке и есть на пустоте - значит точка
                                ChangeDataArr(true);
                                RefreshPole();
                                b5 = true; // дальше предполагаем
                                b = true; // продолжаем дальше
                            } else { // нет ни там ни там - значит неизвестно, это потом сохранять будем
                                pt = assumption.pt;
                                main.noSet[pt.x][pt.y] = true;
                                main.data[pt.x][pt.y] = 0;
                                draw(pt);
                                b5 = true; // дальше предполагаем
                                b = true; // продолжаем дальше
                            }
                        }
                        assumption.b = false;
                    }
                } else {
                    if (b2) { // ошибка была?
                        // если была ошибка без предположений то в кросворде ошибка
                        System.out.println("Ошибка в кроссворде.");
                        b9 = true;
                    } else { // еще не предполагали
                        MaxVer1 = 0; // пока вероятности такие
                        MaxVer2 = 0;
                        b6 = false;
                        for (int x = 1; x <= lenX; x++) {  // по всему полю
                            for (int y = 1; y <= lenY; y++) {
                                if ((MaxVer1 <= main.ver[x][y][1])
                                        && (MaxVer2 <= main.ver[x][y][2])
                                        && (main.ver[x][y][1] < 1)
                                        && (main.ver[x][y][2] < 1)) { // ищем наиболее вероятную точку, но не с вероятностью 1 и 0
                                    if (main.noSet[x][y]) continue;
                                    MaxVer1 = main.ver[x][y][1];
                                    MaxVer2 = main.ver[x][y][2];
                                    pt = new TPoint(x, y);
                                    b6 = true;
                                }
                            }
                        }

                        b6 = b6 && ((MaxVer1 > 0) || (MaxVer2 > 0)); // критерий отбора
                        if (b6) { // нашли точку?
                            // да
                            if (!b5) {
                                assumption.b = true;
                                SavePustot(pt, true); // сохраняемся только если искали макс вероятность без учета массива NoSet
                            }
                            main.data[pt.x][pt.y] = 3;
                            draw(pt);
                            b = true; // произошли изменения
                        } else { // нет
                            if (b5) {
                                assumption.b = false;
                                RefreshPole(); //
                            }
                            b = true; // изменений нету
                            for (int x = 1; x <= lenX; x++) {
                                main.finY[x] = false;
                                main.chY[x] = true;
                            }
                            for (int y = 1; y <= lenY; y++) {
                                main.finX[y] = false;
                                main.chX[y] = true;
                            }
                            b7 = true; // последний прогон для нормального отображения вероятностей
                        }
                        b5 = false;
                    }
                }
            }
            if (b9) b = false; // все конец
        } while (b);
        // очистка массивв флагов заполнености
        for (int x = 1; x <= lenX; x++) {
            main.chY[x] = true;
            main.finY[x] = false;
        }
        for (int y = 1; y <= lenY; y++) {
            main.chX[y] = true;
            main.finX[y] = false;
        }
        for (int x = 1; x <= lenX; x++) {
            for (int y = 1; y <= lenY; y++) {
                switch (main.data[x][y]) {
                    case 1: {
                        main.ver[x][y][1] = 1;
                        main.ver[x][y][2] = 1;
                    }
                    break;
                    case 2: {
                        main.ver[x][y][1] = 0;
                        main.ver[x][y][2] = 0;
                    }
                    break;
                }
            }
        }
        if (b8) { // если нажали остановить
            if (assumption.b) {
                main.data[assumption.pt.x][assumption.pt.y] = 0;
            }
            assumption.b = false;
            RefreshPole(); // прорисовка поля
            printOpened();
        } else {
            // решили кроссворд!
        }

        RefreshPole(); // прорисовка поля
        printOpened();
    }

    private void draw(TPoint pt) {

    }
    
    public void SetPredplDot(boolean bDot) {
        TPoint pt;
        TAllData data;
        if (bDot) { // что предполагаем?
            data = assumptionBlack; // точку
        } else {
            data = assumptionWhite; // путоту
        }
        pt = assumption.pt;
        if (bDot) {
            data.data[pt.x][pt.y] = 1;
            // меняем вероятности
            data.ver[pt.x][pt.y][1] = 1;
            data.ver[pt.x][pt.y][2] = 1;
        } else {
            data.data[pt.x][pt.y] = 2;
            // меняем вероятности
            data.ver[pt.x][pt.y][1] = 0;
            data.ver[pt.x][pt.y][2] = 0;
        }
        // строка и солбец, содержащие эту точку пересчитать
        data.chX[pt.y] = true;
        data.chY[pt.x] = true;
        data.finX[pt.y] = false;
        data.finY[pt.x] = false;
        draw(pt);
    }
    
    public void ChangeDataArr(boolean bDot) {
        TAllData data;
        if (bDot) {
            data = main;
            main = assumptionBlack;
            assumptionBlack = data;
        } else {
            data = main;
            main = assumptionWhite;
            assumptionWhite = data;
        }
    }
    
    public void SavePustot(TPoint pt, boolean bDot) {
        boolean b;
        TAllData data;
        if (bDot) { // что предполагаем?
            data = assumptionBlack; // точку
        } else {
            data = assumptionWhite; // путоту
        }
        for (int x = 1; x <= lenX; x++) { // по всему полю
            for (int y = 1; y <= lenY; y++) {
                data.data[x][y] = main.data[x][y];
                data.ver[x][y][1] = main.ver[x][y][1];
                data.ver[x][y][2] = main.ver[x][y][2];
                b = (main.data[x][y] == 0); // пусто?
                if (b) {
                    // пусто
                    if (data.tchY[x] && data.tchX[y]) { //если производились изменения
                        data.noSet[x][y] = false; // ставить можна
                    }
                } else {
                    data.noSet[x][y] = true; // непусто - сюда ставить нельзя
                }
            }
        }
        for (int x = 1; x <= lenX; x++) {
            data.chY[x] = main.chY[x];
            data.finY[x] = main.finY[x];
            data.tchY[x] = false;
        }
        for (int y = 1; y <= lenY; y++) {
            data.chX[y] = main.chX[y];
            data.finX[y] = main.finX[y];
            data.tchX[y] = false;
        }
        assumption.pt = pt;
        assumption.dot = bDot;
        SetPredplDot(bDot);
    }
    
    public boolean GetFin(TAllData data) {
        boolean c, c2;
        // заполнение поля
        c2 = true;
        for (int y = 1; y <= lenY; y++) {
            c = false; // флаг закончености строки
            for (int x = 1; x <= lenX; x++) {  // по строке
                c = c || (data.data[x][y] == 0); // если заполнено
            }
            c2 = c2 && (!c);
            data.finX[y] = !c;
            //        if (Predpl.B)  // массив флагов заполнености
            //            pDT.data.FinX.arr[y] = !c
            //            else pDM.data.FinX.arr[y] = !c;
        }
        for (int x = 1; x <= lenX; x++) {
            c = false; // флаг закончености строки
            for (int y = 1; y <= lenY; y++) {  // по строке
                c = c || (data.data[x][y] == 0); // если заполнено
            }
            c2 = c2 && (!c);
            data.finY[x] = !c;
            //        if (Predpl.B) // массив флагов заполнености
            //            pDT.data.FinY.arr[x] = !c
            //            else pDM.data.FinY.arr[x] = !c;
        }
        boolean Result = c2;
        if (Result) {
            if (data == main) return Result;
            ChangeDataArr((data == assumptionBlack));
        }
        return Result;
    }
    
    public int PrepRjadX(TAllData data, int y, int[] arr, int[] numb) {
        // подготовка строки
        for (int x = 1; x <= lenX; x++) {
            arr[x] = data.data[x][y]; // данные
        }
        int result = countNumbersX[y]; // длинна ряда
        for (int x = 1; x <= countNumbersX[y]; x++) {
            numb[x] = numbersX[x][y];  // сам ряд
        }
        return result;
    }
    
    public int PrepRjadY(TAllData data, int x, int[] arr, int[] numb) {
        // подготовка столбца
        for (int y = 1; y <= lenY; y++) {
            arr[y] = data.data[x][y];  // данные
        }
        int result = countNumbersY[x]; // длинна ряда
        for (int y = 1; y <= countNumbersY[x]; y++) {
            numb[y] = numbersY[x][y]; // сам ряд
        }
        return result;
    }

    public void loadRjadFromFile(String fileName) {
        TextFile file = new TextFile();
        file.open(fileName);
        file.loadData();
        String line = file.readLine();
        lenX = Integer.valueOf(line); // ширина
        line = file.readLine();
        lenY = Integer.valueOf(line); // высота
        for (int y = 1; y <= lenY; y++) {
            line = file.readLine();
            countNumbersX[y] = Integer.valueOf(line); // длинна y строки
            for (int x = 1; x <= countNumbersX[y]; x++) {
                line = file.readLine();
                numbersX[x][y] = Integer.valueOf(line); // числа y строки
            }
        }

        for (int x = 1; x <= lenX; x++) {
            line = file.readLine();
            countNumbersY[x] = Integer.valueOf(line);       // длинна х столбца
            for (int y = 1; y <= countNumbersY[x]; y++) {
                line = file.readLine();
                numbersY[x][y] = Integer.valueOf(line);   // числа х столбца
            }
        }
        file.close();
    }
    
    public void SaveRjadToFile(String fileName) {
        TextFile file = new TextFile();
        file.open(fileName);
        file.openWrite();
        // ширина
        file.writeLine(Integer.toString(lenX));
        // высота
        file.writeLine(Integer.toString(lenY));
        for (int y = 1; y <= lenY; y++) {
            // длинна y строки
            file.writeLine(Integer.toString(countNumbersX[y]));
            for (int x = 1; x <= countNumbersX[y]; x++) {
                // числа y строки
                file.writeLine(Integer.toString(numbersX[x][y]));
            }
        }

        for (int x = 1; x <= lenX; x++) {
            // длинна х столбца
            file.writeLine(Integer.toString(countNumbersY[x]));
            for (int y = 1; y <= countNumbersY[x]; y++) {
                // числа х столбца
                file.writeLine(Integer.toString(numbersY[x][y]));
            }
        }
        file.close();
    }
    
    private String changeFileExt(String fileName, String ext) {
        return fileName.substring(0, fileName.lastIndexOf(".")) + ext;
    }
    
    private String extractfileName(String fileName) {
        return new File(fileName).getName();
    }
    
    private boolean fileExists(String fileName) {
        return new File(fileName).exists();
    }
    
    public void saveFile(int FilterIndex, String fileName) {
        switch (FilterIndex) {
            case 1: SaveRjadToFile(fileName); break;
            case 2: saveDataToFile(fileName); break;
        }
        System.out.println("Японские головоломки - " + extractfileName(fileName));
    }
    
    public void loadFile(boolean loadNaklad, String fileName, int FilterIndex) {
        String tstr, tstr2;
        boolean b; // флаг накладывания

        if (loadNaklad) {
            tstr = changeFileExt(fileName, ".jap");
            tstr2 = changeFileExt(fileName, ".jdt");
            if (fileExists(tstr) && fileExists(tstr2)) {
                mode = false;
                loadRjadFromFile(tstr); // грузим файл
                loadDataFromFile(tstr2);  // грузим файл
                draw(new TPoint(0, 0));
                System.out.println("Японские головоломки - " + extractfileName(tstr) + ", " + extractfileName(tstr2));
                printOpened();
                return;
            }
        }
        b = (loadNaklad && (mode ^ (FilterIndex == 2)));
        switch (FilterIndex) {
            case 1: { // файл расшифровщика
                if (!b) {
                    // перекл режим
                    mode = false;
                    clear(true); // очищаем поле
                }
                loadRjadFromFile(fileName); // грузим файл
                draw(new TPoint(0, 0));
                System.out.println("Японские головоломки - " + extractfileName(fileName));
                printOpened();
            }
            break;
            case 2: { // файл редактора
                if (!b) {
                    // перекл режим
                    mode = true;
                }
                loadDataFromFile(fileName);  // грузим файл
                if (!b) {
                    GetRjadX(); // получаем ряды
                    GetRjadY();
                }
                draw(new TPoint(0, 0));
                System.out.println("Японские головоломки - " + extractfileName(fileName));
                printOpened();
            }
            break;
        }
    }
    
    public void loadDataFromFile(String fileName) {
        TextFile file = new TextFile();
        file.open(fileName);
        file.loadData();
        String line = file.readLine();
        lenX = Integer.valueOf(line); // ширина
        line = file.readLine();
        lenY = Integer.valueOf(line); // высота
        for (int x = 1; x <= lenX; x++) {
            for (int y = 1; y <= lenY; y++) {
                line = file.readLine();
                main.data[x][y] = Integer.valueOf(line); // поле
            }
        }
        file.close();
    }
    
    public void saveDataToFile(String fileName) {
        TextFile file = new TextFile();
        file.open(fileName);
        file.openWrite();
        // ширина
        file.writeLine(Integer.toString(lenX));
        // высора
        file.writeLine(Integer.toString(lenY));
        for (int x = 1; x <= lenX; x++) {
            for (int y = 1; y <= lenY; y++) {
                // поле
                file.writeLine(Integer.toString(main.data[x][y]));
            }
        }
        file.close();
    }
    
    public int parseSplitted(String str, char ch, int i) {
        return 0; // TODO надо реализовать
    }

    private boolean fill(String input) {
        int a;// убираем дубл. точки
        int i = 2;
        do {
            if ((input.charAt(i - 1) == '.') && (input.charAt(i) == '.')) {
                input = input.substring(1, i - 1) + input.substring(i + 1, input.length() - i);
            } else {
                i++;
            }
            a = input.length();
        } while (i <= a);
        // убираем точку спереди, добавляем в зад
        if (input.charAt(input.length() - 1) != '.') input = input + '.';
        if (input.charAt(1) == '.') input = input.substring(2, input.length() - 1);
        if (input.equals("")) return true;
        a = parseSplitted(input, '.', 0); // количество цифер
        if (!current.xy) { // столбцы
            // заполнение
            countNumbersY[current.pt.x] = a;
            for (int j = 1; j <= a; j++) {
                numbersY[current.pt.x][j] = parseSplitted(input, '.', j);
            }
            int cx = current.pt.x;
            // проверка на ввод нулей - они не нужны
            a = calcCountNumbersY(a, cx);
            if (countNumbersY[current.pt.x] == 0) return true;
            // проверка на ввод чила большего чем ширина
            int j = 0;
            for (i = 1; i <= a; i++) {
                j = j + numbersY[current.pt.x][i];
            }
            if ((j + a - 1) > lenY) {
                countNumbersY[current.pt.x] = 0;
                return true;
            }
            // прорисовать на поле если надо
            if (mode) DataFromRjadY(current.pt.x);
        } else { // строки
            // заполнение
            countNumbersX[current.pt.x] = a;
            for (i = 1; i <= a; i++) {
                numbersX[i][current.pt.x] = parseSplitted(input, '.', i);
            }
            int cx = current.pt.x;
            // проверка на ввод нулей - они не нужны
            a = calcCountNumbersX(a, cx);
            if (countNumbersX[current.pt.x] == 0) return true;
            // проверка на ввод чила большего чем ширина
            int j = 0;
            for (i = 1; i <= a; i++) {
                j = j + numbersX[i][current.pt.x];
            }
            if ((j + a - 1) > lenX) {
                countNumbersY[current.pt.x] = 0;
                return true;
            }
            // прорисовать на поле если надо
            if (mode) dataFromRjadX(current.pt.x);
        }
        return false;
    }

    private int calcCountNumbersX(int len, int y) {
        int i = 1;
        while (i <= len) {
            if (numbersX[i][y] == 0) {
                if (i != len) {
                    for (int j = i + 1; j <= len; j++) {
                        numbersX[j - 1][y] = numbersX[j][y];
                    }
                }
                countNumbersX[y] = countNumbersX[y] - 1;
                len--;
            } else {
                i++;
            }
        }
        return len;
    }

    private int calcCountNumbersY(int len, int x) {
        int i = 1;
        while (i <= len) {
            if (numbersY[x][i] == 0) {
                if (i != len) {
                    for (int j = i + 1; j <= len; j++) {
                        numbersY[x][j - 1] = numbersY[x][j];
                    }
                }
                countNumbersY[x] = countNumbersY[x] - 1;
                len--;
            } else {
                i++;
            }
        }
        return len;
    }

    private void printOpened() {
        int a;
        a = 0;
        for (int x = 1; x <= lenX; x++) {
            for (int y = 1; y <= lenY; y++) {
                if (main.data[x][y] > 0) {
                    a = a + 1;
                }
            }
        }
        System.out.println("Открыто: " + Double.toString(Math.round(1000 * a / (lenX * lenY)) / 10) + "%(" + a + ")");
    }

    @Override
    public int size() {
        return lenX + offset;
    }

    @Override
    public Iterable<? extends Point> elements() {
        return new LinkedList<>(){{
            List<Point> pixels = (List<Point>) main.elements();
            pixels.forEach(pixel -> pixel.change(pt(offset, 0)));
            addAll(pixels);

            int dx = offset; // горизонтальные циферки вверху, должны быть
            int dy = lenY;     // потому мы их смещаем туда
            for (int x = 1; x <= lenX; x++) {
                for (int y = 1; y <= countNumbersY[x]; y++) {
                    add(new Number(pt(x - 1 + dx, y - 1 + dy), numbersY[x][y]));
                }
            }
            for (int y = 1; y <= lenY; y++) {
                for (int x = 1; x <= countNumbersX[y]; x++) {
                    int dxx = offset - countNumbersX[y]; // атут надо смещать хитрее
                    add(new Number(pt(x - 1 + dxx, y - 1), numbersX[x][y]));
                }
            }
        }};
    }

    public void load(Level level) {
        offset = getOffset(level);

        // X рядки чисел
        int end = level.size() - offset;
        List<Numbers> linesX = lines(level, Number::getY)
                .subList(0, end);
        linesX.forEach(numbers -> numbers.fill(offset, false));

        // Y стобики чисел
        List<Numbers> lines = lines(level, Number::getX);
        List<Numbers> linesY = lines
                .subList(offset, lines.size());
        linesY.forEach(numbers -> numbers.fill(offset, false));


        for (int y = 0; y < offset; y++) {
            for (int x = 0; x < lines.size() - offset; x++) {
                Numbers numbers = linesX.get(x);
                int num = numbers.line.get(y);

                numbersX[y + 1][x + 1] = num;
            }
        }

        for (int x = 0; x < lines.size() - offset; x++) {
            for (int y = 0; y < offset; y++) {
                if (numbersX[y + 1][x + 1] != 0) {
                    countNumbersX[x + 1]++;
                }
            }
        }

        lenX = end;

        for (int x = 0; x < lines.size() - offset; x++) {
            Numbers numbers = linesY.get(x);
            for (int y = 0; y < offset; y++) {
                int num = numbers.line.get(y);

                numbersY[x + 1][y + 1] = num;
            }
        }

        for (int y = 0; y < offset; y++) {
            for (int x = 0; x < lines.size() - offset; x++) {
                if (numbersY[x + 1][y + 1] != 0) {
                    countNumbersY[x + 1]++;
                }
            }
        }

        lenY = end;
    }

    // только для квадратных полей с одинаковой шириной зоны с цифрами
    private int getOffset(Level level) {
        List<Nan> nans = level.nans();
        Point pt = pt(0, level.size() - 1);
        while (nans.contains(pt)) {
            pt.change(pt(1, -1));
        }
        return pt.getX();
    }

    static class Numbers {
        int pos;
        LinkedList<Integer> line;

        public Numbers(Map.Entry<Integer, List<Number>> entry) {
            pos = entry.getKey();
            line = new LinkedList<>(entry.getValue().stream()
                    .map(Number::number)
                    .collect(toList()));
        }

        public void fill(int max, boolean before) {
            range(0, max - line.size()).forEach(i -> {
                if (before) {
                    line.addFirst(0);
                } else {
                    line.addLast(0);
                }
            });
        }
    }

    private List<Numbers> lines(Level level, Function<Number, Integer> grouping) {
        return level.numbers().stream()
                .sorted()
                .collect(groupingBy(grouping))
                .entrySet().stream()
                .map(Solver.Numbers::new)
                .collect(toList());
    }
    
    static class TAllData implements BoardReader {
        public double[][][] ver = new double[MAX + 1][MAX + 1][3];
        public boolean[] finX = new boolean[MAX + 1];
        public boolean[] finY = new boolean[MAX + 1];
        public boolean[] chX = new boolean[MAX + 1];
        public boolean[] chY = new boolean[MAX + 1];
        public int[][] data = new int[MAX + 1][MAX + 1];
        public boolean[] tchX = new boolean[MAX + 1];
        public boolean[] tchY = new boolean[MAX + 1];
        public boolean[][] noSet = new boolean[MAX + 1][MAX + 1];

        @Override
        public int size() {
            if (lenX != lenY) {
                throw new RuntimeException("Кроссворд не прямоугольный");
            }

            return Solver.lenX;
        }

        @Override
        public Iterable<? extends Point> elements() {
            List<Pixel> result = new LinkedList<>();
            for (int x = 1; x <= lenX; x++) {
                for (int y = 1; y <= lenY; y++) {
                    // инвертирование потому что в этом коде черный и белый отличаются от codenjoyного
                    int inverted = Math.abs(data[x][y] - 1);
                    // так же надо отступить, потому что в этом коде индексы начинаются с 0
                    result.add(new Pixel(pt(x - 1, y - 1), Color.get(inverted)));
                }
            }
            return result;
        }
    }

    static class TAssumption {
        public TPoint pt;
        public boolean dot;
        public boolean b;
    }

    static class TCurrent { // текущий ряд (для редактирования)
        public TPoint pt; // координаты
        public boolean xy; // ряд / столбец
    }

    static class TextFile {
        File file;
        List<String> lines;
        int index;
        Writer writer;

        public void open(String fileName) {
            file = new File(fileName);
            lines = new LinkedList<>();
        }

        public void loadData() {
            try (Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()))) {
                stream.forEach(lines::add);
                index = 0;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String readLine() {
            return lines.get(index++);
        }

        public void close() {
            lines.clear();
            index = 0;
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                writer = null;
            }
        }

        public void openWrite() {
            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void writeLine(String text) {
            try {
                writer.write(text + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
