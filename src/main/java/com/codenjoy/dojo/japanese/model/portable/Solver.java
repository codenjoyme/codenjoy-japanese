package com.codenjoy.dojo.japanese.model.portable;

import com.codenjoy.dojo.japanese.model.items.Nan;
import com.codenjoy.dojo.japanese.model.items.Number;
import com.codenjoy.dojo.japanese.model.level.Level;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;

import java.io.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

class Solver implements BoardReader {

    public static final String JAP_EXT = ".jap";
    public static final String JDT_EXT = ".jdt";

    public static final double EXACTLY_BLACK = 1.0;
    public static final double EXACTLY_WHITE = 0.0;
    public static final double UNKNOWN = -1.0;

    public static final int MAX = 150;

    private boolean mode;
    public boolean withAssumption = false; // гадать ли алгоритму, если нет вариантов точных на поле
    private TAllData main = new TAllData(); // тут решение точное
    private TAllData assumptionBlack = new TAllData(); // тут предполагаем black
    private TAllData assumptionWhite = new TAllData();// тут предполагаем white
    private Assumption assumption; //данные предположения
    public static int lenX = 15, lenY = 15; // длинна и высота кроссворда
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
                main.probability[x][y][Dot.BLACK.code()] = UNKNOWN;
                main.probability[x][y][Dot.WHITE.code()] = UNKNOWN;
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
        lenX = 15;
        lenY = 15;
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

    
    public void printField() {
        System.out.println(printAll());
        System.out.println();
        System.out.println();
    }
    
    public void solve() {
        boolean wasChanges, ableToNewAssumption, foundMaxProbDot, b7, wasError, b11;
        // wasChanges - произошли ли изменения,
        // b3 - ,
        // b4 - ,
        // ableToNewAssumption - предполагать максимальной вероятности с учетом массива NoSet,
        // foundMaxProbDot - если точка с максимальной вероятностью была найдена,
        // b7 - последний прогон для нормального отображения вероятностей,
        // wasError - если остановка по ошибке,
        // b11 - нужно для пропуска прогона по у если LenX больше LenY

        int h; 
        double max1, max2;
        double a1, a2; 
        TPoint pt;
        TAllData data;

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
        ableToNewAssumption = false;
        b7 = false;
        wasError = false;
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
        assumption = new Assumption();
        wasChanges = false; // для b11
        do {
            if (wasChanges && b11) b11 = false;
            wasChanges = false;

            data = getData();

            if (!ableToNewAssumption && !b11) {
                // при поиске другой точки
                // или если LenX больше LenY (в начале)
                for (int y = 1; y <= lenY; y++) {
                    if (data.finX[y]) continue;
                    if (!data.chX[y]) continue;

                    if (!lineSolver.calculate(numbersX(y), data.dataX(y))) {
                        // если нет ни одной комбинации - ошибка
                        if (!withAssumption) {
                            System.out.println("Ошибка в кроссворде (строка " + y + ").");
                            wasError = true;
                            break;
                        }
                        wasChanges = false;
                        assumption.error();
                        break;
                    }
                    for (int x = 1; x <= lenX; x++) {
                        data.probability[x][y][Dot.BLACK.code()] = lineSolver.probability(x);

                        if (data.data[x][y] != lineSolver.array(x)) {
                            data.data[x][y] = lineSolver.array(x);
                            if (!wasChanges) {
                                wasChanges = true;
                            }
                            if (!data.chY[x]) {
                                data.chY[x] = true;
                            }
                            if (assumption.inProgress()) {
                                if (!data.tchY[x]) {
                                    data.tchY[x] = true;
                                }
                            }
                        }
                    }
                    data.chX[y] = false;
                }
                if (!assumption.inProgress()) {
                    printField(); // прорисовка поля только в случае точного расчета
                }
            }
            if (!wasError) {
                wasError = getFin(data); // если небыло ошибки, то если сложили все wasError = GetFin; выходим как если была бы ошибка
            }

            if (!assumption.wasError() && !ableToNewAssumption && !wasError) {
                // если была ошибка (assumptionError)
                // или надо найти другую точку (ableToNewAssumption)
                // или была ошибка (wasError) то пропускаем этот шаг
                for (int x = 1; x <= lenX; x++) { // дальше то же только для столбцов
                    if (data.finY[x]) continue;
                    if (!data.chY[x]) continue;
                    if (!lineSolver.calculate(numbersY(x), data.dataY(x))) {
                        if (!withAssumption) {
                            System.out.println("Ошибка в кроссворде (столбец " + x + ").");
                            wasError = true;
                            break;
                        }
                        wasChanges = false;
                        assumption.error();
                        break;
                    }

                    for (int y = 1; y <= lenY; y++) {
                        data.probability[x][y][Dot.WHITE.code()] = lineSolver.probability(y);

                        if (data.data[x][y] != lineSolver.array(y)) {
                            data.data[x][y] = lineSolver.array(y);
                            if (!wasChanges) {
                                wasChanges = true;
                            }
                            if (!data.chX[y]) {
                                data.chX[y] = true;
                            }
                            if (assumption.inProgress()) {
                                if (!data.tchY[x]) {
                                    data.tchY[x] = true;
                                }
                            }
                        }
                    }
                    data.chY[x] = false;
                }
                if (!assumption.inProgress()) {
                    printField();
                }
                if (b11) wasChanges = true; // чтобы после прогона по х пошел прогон по у
            }
            if (!wasError) {
                // если небыло ошибки, то если сложили все wasError = GetFin
                // выходим как если была бы ошибка
                wasError = getFin(data);
            }

            if (b7) wasChanges = false; // все конец

            if (withAssumption && !wasChanges && !b7 && !wasError) {
                // если включено предположение (tryAssumption)
                // и ничего не получается решить точно (wasChanges)
                // и последнего прогона нет (b7)
                // и ошибок так же нет (wasError)

                if (b11) b11 = false;

                if (assumption.inProgress()) {
                    if (assumption.isBlack()) {
                        // после BLACK пробуем предположить WHITE
                        tryAssumption(assumption.at(), Dot.WHITE);
                        wasChanges = true;
                    } else {
                        // были предположения на WHITE (а до этого на BLACK),
                        // значит будем определять что нам записывать в клетку
                        if (assumption.errorOnBoth()) {
                            // ошибка на WHITE и на BLACK - ошибка где-то в кроссворде
                            System.out.println("Ошибка в кроссворде.");
                            wasError = true;
                        } else if (assumption.noErrors()) {
                            // нет ошибок ни там ни там - значит неизвестно
                            pt = assumption.at();
                            main.noSet[pt.x][pt.y] = true;
                            main.data[pt.x][pt.y] = Dot.UNSET;
                            draw(pt);
                            ableToNewAssumption = true;
                            wasChanges = true;
                        } else {
                            // ошибка была на одном из цветов, а значит точка другая
                            applyAssumptionData(assumption.errorOn().invert());
                            printField();
                            ableToNewAssumption = true;
                            wasChanges = true;
                        }
                        assumption.stop();
                    }
                } else {
                    if (assumption.wasError()) { // ошибка была?
                        // если была ошибка без предположений то в кросворде ошибка
                        System.out.println("Ошибка в кроссворде.");
                        wasError = true;
                    } else { // еще не предполагали
                        max1 = 0; // пока вероятности такие
                        max2 = 0;
                        foundMaxProbDot = false;
                        for (int x = 1; x <= lenX; x++) {  // по всему полю
                            for (int y = 1; y <= lenY; y++) {
                                if ((max1 <= main.probability[x][y][Dot.BLACK.code()])
                                        && (max2 <= main.probability[x][y][Dot.WHITE.code()])
                                        && (main.probability[x][y][Dot.BLACK.code()] < 1.0) // TODO тут странно, вероятность белой точки вроде как EXACLTY_WHITE
                                        && (main.probability[x][y][Dot.WHITE.code()] < 1.0)) { // ищем наиболее вероятную точку, но не с вероятностью 1 и 0
                                    if (main.noSet[x][y]) continue;
                                    max1 = main.probability[x][y][Dot.BLACK.code()];
                                    max2 = main.probability[x][y][Dot.WHITE.code()];
                                    pt = new TPoint(x, y);
                                    foundMaxProbDot = true;
                                }
                            }
                        }

                        foundMaxProbDot = foundMaxProbDot && ((max1 > 0) || (max2 > 0)); // критерий отбора
                        if (foundMaxProbDot) {
                            // нашли точку
                            if (!ableToNewAssumption) {
                                tryAssumption(pt, Dot.BLACK); // сохраняемся только если искали макс вероятность без учета массива NoSet
                            }
                            main.data[pt.x][pt.y] = Dot.ASSUMPTION;
                            draw(pt);
                            wasChanges = true; // произошли изменения
                        } else {
                            // не нашли точку
                            if (ableToNewAssumption) {
                                assumption.stop();
                                printField();
                            }
                            wasChanges = true;
                            updateAllFinCh(false);
                            b7 = true; // последний прогон для нормального отображения вероятностей
                        }
                        ableToNewAssumption = false;
                    }
                }
            }
            if (wasError) wasChanges = false; // все конец
        } while (wasChanges);

        updateAllFinCh(false);
        for (int x = 1; x <= lenX; x++) {
            for (int y = 1; y <= lenY; y++) {
                switch (main.data[x][y]) {
                    case BLACK: {
                        main.probability[x][y][Dot.BLACK.code()] = EXACTLY_BLACK;
                        main.probability[x][y][Dot.WHITE.code()] = EXACTLY_BLACK;
                    }
                    break;
                    case WHITE: {
                        main.probability[x][y][Dot.BLACK.code()] = EXACTLY_WHITE;
                        main.probability[x][y][Dot.WHITE.code()] = EXACTLY_WHITE;
                    }
                    break;
                }
            }
        }

        printField(); // прорисовка поля
        printOpened();
    }

    // Так как если мы зайдем в тупик и явно ничего не сможем отгадать
    // алгоритм будет пытаться отгадывать методом научного тыка в точку с максимальной вероятностью
    // есть три сценария:
    // 1) мы поставили точку черную и попробовали решить кроссворд в assumptionBlack объекте
    // 2) если 1) приводит к ошибке решения или не решается полностью, мы ставим точку белую и пробуем решать в assumptionWhite объенкте
    // 3) если и 2) привело к ошибке, значит ошибка кроссворда, потому что ни черную ни белую мы не можем поставить
    // 4) может случиться так, что ни в 1) ни в 2) ошибок нет (в решении продвинулись но не до конца), тогда мы точно не знаем какого цвета там точка
    // и тогда нам надо вернуться в основной режим с объектом main и порпобовать то же где-то еще
    private TAllData getData() {
        if (assumption.isBlack()) {
            return assumptionBlack;
        }

        if (assumption.isWhite()) {
            return assumptionWhite;
        }

        return main;
    }

    private int[] numbersX(int y) {
        int countNumbers = countNumbersX[y];
        int[] result = new int[countNumbers + 1];
        for (int x = 1; x <= countNumbers; x++) {
            result[x] = numbersX[x][y];
        }
        return result;
    }

    private int[] numbersY(int x) {
        int countNumbers = countNumbersY[x];
        int[] result = new int[countNumbers + 1];
        for (int y = 1; y <= countNumbers; y++) {
            result[y] = numbersY[x][y];
        }
        return result;
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
    
    public void updateAssumptionDot(Dot dot) {
        TAllData data = getAssumptionData(dot);
        TPoint pt = assumption.at();
        if (dot.isBlack()) {
            data.data[pt.x][pt.y] = Dot.BLACK;
            // меняем вероятности
            data.probability[pt.x][pt.y][Dot.BLACK.code()] = EXACTLY_BLACK;
            data.probability[pt.x][pt.y][Dot.WHITE.code()] = EXACTLY_BLACK;
        } else {
            data.data[pt.x][pt.y] = Dot.WHITE;
            // меняем вероятности
            data.probability[pt.x][pt.y][Dot.BLACK.code()] = EXACTLY_WHITE;
            data.probability[pt.x][pt.y][Dot.WHITE.code()] = EXACTLY_WHITE;
        }
        // строка и солбец, содержащие эту точку пересчитать
        data.chX[pt.y] = true;
        data.chY[pt.x] = true;
        data.finX[pt.y] = false;
        data.finY[pt.x] = false;
        draw(pt);
    }
    
    public void applyAssumptionData(Dot dot) {
        TAllData data;
        if (dot.isBlack()) {
            data = main;
            main = assumptionBlack;
            assumptionBlack = data;
        } else if (dot.isWhite()) {
            data = main;
            main = assumptionWhite;
            assumptionWhite = data;
        }
    }
    
    public void tryAssumption(TPoint pt, Dot dot) {
        assumption.start(pt, dot);

        TAllData data = getAssumptionData(dot);
        for (int x = 1; x <= lenX; x++) { // по всему полю
            for (int y = 1; y <= lenY; y++) {
                data.data[x][y] = main.data[x][y];
                data.probability[x][y][Dot.BLACK.code()] = main.probability[x][y][Dot.BLACK.code()];
                data.probability[x][y][Dot.WHITE.code()] = main.probability[x][y][Dot.WHITE.code()];

                if (main.data[x][y] == Dot.UNSET) { // пусто в оригинале?
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
        updateAssumptionDot(dot);
    }

    private TAllData getAssumptionData(Dot dot) {
        if (dot.isBlack()) {
            return assumptionBlack;
        } else {
            return assumptionWhite;
        }
    }

    public boolean getFin(TAllData data) {
        // заполнение поля
        boolean c2 = true;
        for (int y = 1; y <= lenY; y++) {
            boolean c = false; // флаг закончености строки
            for (int x = 1; x <= lenX; x++) {  // по строке
                c = c || (data.data[x][y] == Dot.UNSET); // если заполнено
            }
            c2 = c2 && (!c);
            data.finX[y] = !c;
        }
        for (int x = 1; x <= lenX; x++) {
            boolean c = false;
            for (int y = 1; y <= lenY; y++) {
                c = c || (data.data[x][y] == Dot.UNSET);
            }
            c2 = c2 && (!c);
            data.finY[x] = !c;
        }
        boolean result = c2;
        if (result) {
            if (data == main) return result;
            applyAssumptionData(assumption.color());
        }
        return result;
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
                file.writeLine(Integer.toString(main.data[x][y].code()));
            }
        }
        file.close();
    }
    
    public int parseSplitted(String str, char ch, int i) {
        return 0; // TODO надо реализовать
    }

    // TODO метод в котором уже продумано заполнение, вангую повторно можно будет использовать
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
        TPoint pt = new TPoint(1, 1); // координаты
        boolean xy = true; // ряд / столбец
        if (!xy) { // столбцы
            // заполнение
            countNumbersY[pt.x] = a;
            for (int j = 1; j <= a; j++) {
                numbersY[pt.x][j] = parseSplitted(input, '.', j);
            }
            int cx = pt.x;
            // проверка на ввод нулей - они не нужны
            a = calcCountNumbersY(a, cx);
            if (countNumbersY[pt.x] == 0) return true;
            // проверка на ввод чила большего чем ширина
            int j = 0;
            for (i = 1; i <= a; i++) {
                j = j + numbersY[pt.x][i];
            }
            if ((j + a - 1) > lenY) {
                countNumbersY[pt.x] = 0;
                return true;
            }
            // прорисовать на поле если надо
            if (mode) dataFromNumbersY(pt.x);
        } else { // строки
            // заполнение
            countNumbersX[pt.x] = a;
            for (i = 1; i <= a; i++) {
                numbersX[i][pt.x] = parseSplitted(input, '.', i);
            }
            int cx = pt.x;
            // проверка на ввод нулей - они не нужны
            a = calcCountNumbersX(a, cx);
            if (countNumbersX[pt.x] == 0) return true;
            // проверка на ввод чила большего чем ширина
            int j = 0;
            for (i = 1; i <= a; i++) {
                j = j + numbersX[i][pt.x];
            }
            if ((j + a - 1) > lenX) {
                countNumbersY[pt.x] = 0;
                return true;
            }
            // прорисовать на поле если надо
            if (mode) dataFromNumbersX(pt.x);
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
                numbersX[y][x] = linesX.get(x - 1).get(y - 1);
                numbersY[x][y] = linesY.get(x - 1).get(y - 1);

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

    private List<Numbers> lines(Level level, Function<Number, Integer> grouping) {
        return level.numbers().stream()
                .sorted()
                .collect(groupingBy(grouping))
                .entrySet().stream()
                .map(Numbers::new)
                .collect(toList());
    }

}
