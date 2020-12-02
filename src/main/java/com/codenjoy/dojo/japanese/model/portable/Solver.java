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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

class Solver implements BoardReader {

    public static final String JAP_EXT = ".jap";
    public static final String JDT_EXT = ".jdt";


    enum Dot {

        UNSET(0), BLACK(1), WHITE(2), ASSUMPTION(3);

        private int code;

        Dot(int code) {
            this.code = code;
        }

        public static Dot get(String data) {
            return Arrays.stream(Dot.values())
                    .filter(dot -> dot.code == Integer.valueOf(data))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Верный код для Dot"));
        }
    }

    public static final double EXACTLY_BLACK = 1.0;
    public static final double EXACTLY_WHITE = 0.0;
    public static final double UNKNOWN = -1.0;
    public static final int MAX = 150;

    private TPoint previous;
    private boolean mode;
    public boolean tryAssumption = false; // гадать ли алгоритму, если нет вариантов точных на поле
    private TCurrent current = new TCurrent();
    private TAllData main = new TAllData(); // тут решение точное
    private TAllData assumptionBlack = new TAllData(); // тут предполагаем black
    private TAllData assumptionWhite = new TAllData();// тут предполагаем white
    private TAssumption assumption = new TAssumption(); //данные предположения
    private static int lenX = 15, lenY = 15; // длинна и высота кроссворда
    private int[][] numbersX = new int[MAX + 1][MAX + 1]; // тут хранятся цифры рядов
    private int[][] numbersY = new int[MAX + 1][MAX + 1];
    private int[] countNumbersX = new int[MAX + 1]; // тут хранятся количества цифер рядов
    private int[] countNumbersY = new int[MAX + 1];
    private LineSolver lineSolver;

    int offsetX;
    int offsetY;

    public Solver() {
        init();
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
                main.data[x][y] = Dot.UNSET;
                main.probability[x][y][Dot.BLACK.code] = UNKNOWN;
                main.probability[x][y][Dot.WHITE.code] = UNKNOWN;
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
        lineSolver = new LineSolver();
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
    
    public void getNumbersX() {
        int a;
        for (int y = 1; y <= lenY; y++) {
            a = 0;
            countNumbersX[y] = 1;
            for (int x = 1; x <= lenX; x++) {
                if (main.data[x][y] == Dot.BLACK) {
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
    
    public void getNumbersY() {
        int a;
        for (int x = 1; x <= lenX; x++) {
            a = 0;
            countNumbersY[x] = 1;
            for (int y = 1; y <= lenY; y++) {
                if (main.data[x][y] == Dot.BLACK) {
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
    
    public void dataFromNumbersX(int y) {
        int k, tx, j;
        for (tx = 1; tx <= lenX; tx++) {
            main.data[tx][y] = Dot.UNSET;
        }
        k = 1;
        tx = 1;
        while (tx <= countNumbersX[y]) {
            for (j = 1; j <= numbersX[tx][y]; j++) {
                main.data[k + j - 1][y] = Dot.BLACK;
            }
            main.data[k + j][y] = Dot.UNSET;
            k = k + j;
            tx++;
        }
        getNumbersY();
    }
    
    public void dataFromNumbersY(int x) {
        int k, j, ty;
        for (ty = 1; ty <= lenY; ty++) {
            main.data[x][ty] = Dot.UNSET;
        }
        k = 1;
        ty = 1;
        while (ty <= countNumbersY[x]) {
            for (j = 1; j <= numbersY[x][ty]; j++) {
                main.data[x][k + j - 1] = Dot.BLACK;
            }
            main.data[x][k + j] = Dot.UNSET;
            k = k + j;
            ty++;
        }
        getNumbersX();
    }

    public TPoint check() {
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
        double max1, max2;
        double a1, a2; 
        TPoint pt;
        TAllData data;
        boolean bErrT, bErrP;

        // проверка на совпадение рядов
        pt = check();
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

            if (!(b5 || b11)) { // при поиске другой точки, или если LenX больше LenY (в начале) пропускаем этот шаг
                for (int y = 1; y <= lenY; y++) {
                    if (data.finX[y]) continue;
                    if (!data.chX[y]) continue;
                    lineSolver.prepareNumbersX(data, y, lenX, countNumbersX, numbersX); // подготовка строки
                    if (!lineSolver.calculate()) { // расчет ... если нет ни одной комбины - ошибка
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
                        data.probability[x][y][Dot.BLACK.code] = lineSolver.probability(x);

                        if (data.data[x][y] != lineSolver.array(x)) {
                            data.data[x][y] = lineSolver.array(x);
                            if (!b) {
                                b = true;
                            }
                            if (!data.chY[x]) {
                                data.chY[x] = true;
                            }
                            if (assumption.b) {
                                if (!data.tchY[x]) {
                                    data.tchY[x] = true;
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
                    lineSolver.prepareNumbersY(data, x, lenY, countNumbersY, numbersY);
                    if (!lineSolver.calculate()) {
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
                        data.probability[x][y][Dot.WHITE.code] = lineSolver.probability(y);

                        if (data.data[x][y] != lineSolver.array(y)) {
                            data.data[x][y] = lineSolver.array(y);
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
                        savePustot(assumption.pt, false); //была точка, теперь пустота
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
                                main.data[pt.x][pt.y] = Dot.UNSET;
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
                        max1 = 0; // пока вероятности такие
                        max2 = 0;
                        b6 = false;
                        for (int x = 1; x <= lenX; x++) {  // по всему полю
                            for (int y = 1; y <= lenY; y++) {
                                if ((max1 <= main.probability[x][y][Dot.BLACK.code])
                                        && (max2 <= main.probability[x][y][Dot.WHITE.code])
                                        && (main.probability[x][y][Dot.BLACK.code] < 1.0) // TODO тут странно, вероятность белой точки вроде как EXACLTY_WHITE
                                        && (main.probability[x][y][Dot.WHITE.code] < 1.0)) { // ищем наиболее вероятную точку, но не с вероятностью 1 и 0
                                    if (main.noSet[x][y]) continue;
                                    max1 = main.probability[x][y][Dot.BLACK.code];
                                    max2 = main.probability[x][y][Dot.WHITE.code];
                                    pt = new TPoint(x, y);
                                    b6 = true;
                                }
                            }
                        }

                        b6 = b6 && ((max1 > 0) || (max2 > 0)); // критерий отбора
                        if (b6) { // нашли точку?
                            // да
                            if (!b5) {
                                assumption.b = true;
                                savePustot(pt, true); // сохраняемся только если искали макс вероятность без учета массива NoSet
                            }
                            main.data[pt.x][pt.y] = Dot.ASSUMPTION;
                            draw(pt);
                            b = true; // произошли изменения
                        } else { // нет
                            if (b5) {
                                assumption.b = false;
                                RefreshPole(); //
                            }
                            b = true; // изменений нету
                            updateAllFinCh(false);
                            b7 = true; // последний прогон для нормального отображения вероятностей
                        }
                        b5 = false;
                    }
                }
            }
            if (b9) b = false; // все конец
        } while (b);

        updateAllFinCh(false);
        for (int x = 1; x <= lenX; x++) {
            for (int y = 1; y <= lenY; y++) {
                switch (main.data[x][y]) {
                    case BLACK: {
                        main.probability[x][y][Dot.BLACK.code] = EXACTLY_BLACK;
                        main.probability[x][y][Dot.WHITE.code] = EXACTLY_BLACK;
                    }
                    break;
                    case WHITE: {
                        main.probability[x][y][Dot.BLACK.code] = EXACTLY_WHITE;
                        main.probability[x][y][Dot.WHITE.code] = EXACTLY_WHITE;
                    }
                    break;
                }
            }
        }
        if (b8) { // если нажали остановить
            if (assumption.b) {
                main.data[assumption.pt.x][assumption.pt.y] = Dot.UNSET;
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

    // очистка масивов флагов заполнености
    private void updateAllFinCh(boolean flag) {
        for (int x = 1; x <= lenX; x++) {
            main.chY[x] = !flag;
            main.finY[x] = flag;
        }
        for (int y = 1; y <= lenY; y++) {
            main.chX[y] = !flag;
            main.finX[y] = flag;
        }
    }

    private void draw(TPoint pt) {

    }
    
    public void setAssumptionDot(boolean isBlack) {
        TAllData data = getAssumptionData(isBlack);
        TPoint pt = assumption.pt;
        if (isBlack) {
            data.data[pt.x][pt.y] = Dot.BLACK;
            // меняем вероятности
            data.probability[pt.x][pt.y][Dot.BLACK.code] = EXACTLY_BLACK;
            data.probability[pt.x][pt.y][Dot.WHITE.code] = EXACTLY_BLACK;
        } else {
            data.data[pt.x][pt.y] = Dot.WHITE;
            // меняем вероятности
            data.probability[pt.x][pt.y][Dot.BLACK.code] = EXACTLY_WHITE;
            data.probability[pt.x][pt.y][Dot.WHITE.code] = EXACTLY_WHITE;
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
    
    public void savePustot(TPoint pt, boolean bDot) {
        TAllData data = getAssumptionData(bDot);
        for (int x = 1; x <= lenX; x++) { // по всему полю
            for (int y = 1; y <= lenY; y++) {
                data.data[x][y] = main.data[x][y];
                data.probability[x][y][Dot.BLACK.code] = main.probability[x][y][Dot.BLACK.code];
                data.probability[x][y][Dot.WHITE.code] = main.probability[x][y][Dot.WHITE.code];
                if (main.data[x][y] == Dot.UNSET) { // пусто?
                    // пусто
                    if (data.tchY[x] && data.tchX[y]) { // если производились изменения
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
        setAssumptionDot(bDot);
    }

    private TAllData getAssumptionData(boolean isBlack) {
        if (isBlack) { // что предполагаем?
            return assumptionBlack; // точку
        } else {
            return assumptionWhite; // путоту
        }
    }

    public boolean GetFin(TAllData data) {
        boolean c, c2;
        // заполнение поля
        c2 = true;
        for (int y = 1; y <= lenY; y++) {
            c = false; // флаг закончености строки
            for (int x = 1; x <= lenX; x++) {  // по строке
                c = c || (data.data[x][y] == Dot.UNSET); // если заполнено
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
                c = c || (data.data[x][y] == Dot.UNSET); // если заполнено
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

    public void loadNumbersFromFile(String fileName) {
        TextFile file = new TextFile();
        file.open(fileName);
        file.loadData();

        lenX = Integer.valueOf(file.readLine()); // ширина
        lenY = Integer.valueOf(file.readLine()); // высота

        loadNumbers(file,(x, y) -> pt(x, y), countNumbersX, numbersX, lenY);
        loadNumbers(file,(x, y) -> pt(y, x), countNumbersY, numbersY, lenX);

        int maxY = 0;
        for (int y = 1; y <= lenY; y++) {
            maxY = Math.max(maxY, countNumbersX[y]);
        }

        int maxX = 0;
        for (int x = 1; x <= lenX; x++) {
            maxX = Math.max(maxX, countNumbersX[x]);
        }
        offsetX = maxX;
        offsetY = maxY;


        file.close();
    }

    private void loadNumbers(TextFile file, BiFunction<Integer, Integer, Point> mirror, int[] counts, int[][] numbers, int len) {
        for (int y = 1; y <= len; y++) { ;
            counts[y] = Integer.valueOf(file.readLine()); // длинна y строки
            for (int x = 1; x <= counts[y]; x++) {
                Point pt = mirror.apply(x, y);
                numbers[pt.getX()][pt.getY()] = Integer.valueOf(file.readLine()); // числа y строки
            }
        }
    }

    public void saveNumbersToFile(String fileName) {
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
    
    public void loadFile(boolean loadNaklad, String fileName, int FilterIndex) {
        String tstr, tstr2;
        boolean b; // флаг накладывания

        if (loadNaklad) {
            tstr = changeFileExt(fileName, JAP_EXT);
            tstr2 = changeFileExt(fileName, JDT_EXT);
            if (fileExists(tstr) && fileExists(tstr2)) {
                mode = false;
                loadNumbersFromFile(tstr); // грузим файл
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
                loadNumbersFromFile(fileName); // грузим файл
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
                    getNumbersX(); // получаем ряды
                    getNumbersY();
                }
                draw(new TPoint(0, 0));
                System.out.println("Японские головоломки - " + extractfileName(fileName));
                printOpened();
            }
            break;
        }
    }

    public void saveFile(boolean numbersOrData, String fileName) {
        if (numbersOrData) {
            saveNumbersToFile(fileName);
        } else {
            saveDataToFile(fileName);
        }
        System.out.println("Японские головоломки - " + extractfileName(fileName));
    }

    public void loadDataFromFile(String fileName) {
        TextFile file = new TextFile();
        file.open(fileName);
        file.loadData();

        lenX = Integer.valueOf(file.readLine()); // ширина
        lenY = Integer.valueOf(file.readLine()); // высота
        for (int x = 1; x <= lenX; x++) {
            for (int y = 1; y <= lenY; y++) {
                main.data[x][y] = Dot.get(file.readLine()); // поле
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
                file.writeLine(Integer.toString(main.data[x][y].code));
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
            if (mode) dataFromNumbersY(current.pt.x);
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
            if (mode) dataFromNumbersX(current.pt.x);
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
        int a = 0;
        for (int x = 1; x <= lenX; x++) {
            for (int y = 1; y <= lenY; y++) {
                if (main.data[x][y] != Dot.UNSET) {
                    a = a + 1;
                }
            }
        }
        System.out.println("Открыто: " + Double.toString(Math.round(1000 * a / (lenX * lenY)) / 10) + "%(" + a + ")");
    }

    @Override
    public int size() {
        return Math.max(lenX + offsetX, lenY + offsetY);
    }

    @Override
    public Iterable<? extends Point> elements() {
        return new LinkedList<>(){{
            List<Point> pixels = (List<Point>) main.elements();
            pixels.forEach(pixel -> pixel.change(pt(offsetX, 0)));
            addAll(pixels);

            int dx = offsetX; // горизонтальные циферки вверху, должны быть
            int dy = lenY;     // потому мы их смещаем туда
            for (int x = 1; x <= lenX; x++) {
                for (int y = 1; y <= countNumbersY[x]; y++) {
                    add(new Number(pt(x - 1 + dx, y - 1 + dy), numbersY[x][y]));
                }
            }
            for (int y = 1; y <= lenY; y++) {
                for (int x = 1; x <= countNumbersX[y]; x++) {
                    int dxx = offsetX - countNumbersX[y]; // атут надо смещать хитрее
                    add(new Number(pt(x - 1 + dxx, y - 1), numbersX[x][y]));
                }
            }
        }};
    }

    public void load(Level level) {
        getOffset(level);

        // X рядки чисел
        lenX = level.size() - offsetX;
        lenY = lenX;

        List<Numbers> linesX = lines(level, Number::getY)
                .subList(0, lenX);
        linesX.forEach(numbers -> numbers.fill(offsetX, false));

        // Y стобики чисел
        List<Numbers> lines = lines(level, Number::getX);
        List<Numbers> linesY = lines
                .subList(offsetX, lines.size());
        linesY.forEach(numbers -> numbers.fill(offsetX, false));


        for (int y = 1; y <= offsetX; y++) {
            for (int x = 1; x <= lenX; x++) {
                numbersX[y][x] = linesX.get(x - 1).line.get(y - 1);
                numbersY[x][y] = linesY.get(x - 1).line.get(y - 1);

                if (numbersX[y][x] != 0) {
                    countNumbersX[x]++;
                }
                if (numbersY[x][y] != 0) {
                    countNumbersY[x]++;
                }
            }
        }
    }

    // TODO только для квадратных полей с одинаковой шириной зоны с цифрами
    private void getOffset(Level level) {
        List<Nan> nans = level.nans();
        Point pt = pt(0, level.size() - 1);
        while (nans.contains(pt)) {
            pt.change(pt(1, -1));
        }
        offsetX = pt.getX();
        offsetY = level.size() - 1 - pt.getY();
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
        public double[][][] probability = new double[MAX + 1][MAX + 1][3];
        public boolean[] finX = new boolean[MAX + 1];
        public boolean[] finY = new boolean[MAX + 1];
        public boolean[] chX = new boolean[MAX + 1];
        public boolean[] chY = new boolean[MAX + 1];
        public Dot[][] data = new Dot[MAX + 1][MAX + 1];
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
                    // инвертирование потому что в этом коде черный и белый отличаются от codenjoy'ного
                    int inverted = Math.abs(data[x][y].code - 1);
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
