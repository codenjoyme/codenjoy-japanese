package com.codenjoy.dojo.japanese.model.portable;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.japanese.model.Player;
import com.codenjoy.dojo.japanese.model.items.Nan;
import com.codenjoy.dojo.japanese.model.items.Number;
import com.codenjoy.dojo.japanese.model.level.Level;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Solver implements BoardReader<Player> {

    public static final double EXACTLY_BLACK = 1.0;
    public static final double EXACTLY_NOT_BLACK = 0.0;
    public static final double UNKNOWN = -1.0;

    private boolean withAssumption; // гадать ли алгоритму, если нет вариантов точных на поле

    private AllData main; // тут решение точное
    private AllData assumptionBlack; // тут предполагаем black
    private AllData assumptionWhite;// тут предполагаем white

    private Assumption assumption;
    public int width, height;
    private int[][] numbersX; // тут хранятся цифры рядов // TODO объединить с countNumbersX
    private int[][] numbersY;
    private int[] countNumbersX; // тут хранятся количества цифер рядов
    private int[] countNumbersY;
    private LineSolver lineSolver;

    int offsetX;
    int offsetY;

    public Solver(boolean withAssumption) {
        this.withAssumption = withAssumption;
        lineSolver = new LineSolver();
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
        main.clear();
        if (all) {
            for (int x = 1; x <= width; x++) {
                countNumbersY[x] = 0;
            }
            for (int y = 1; y <= height; y++) {
                countNumbersX[y] = 0;
            }
        }
    }

    // по загруженным данным на поле может построить numbers/countNumbers
    public void getNumbersX() {
        for (int y = 1; y <= height; y++) {
            int length = 0;
            countNumbersX[y] = 1;
            for (int x = 1; x <= width; x++) {
                if (main.data[x][y].isBlack()) {
                    length++;
                    continue;
                }

                if (length != 0) {
                    numbersX[countNumbersX[y]][y] = length;
                    length = 0;
                    countNumbersX[y]++;
                }
            }
            if (length != 0) {
                numbersX[countNumbersX[y]][y] = length;
                countNumbersX[y]++;
            }
            countNumbersX[y]--;
        }
    }

    // по загруженным данным на поле может построить numbers/countNumbers
    public void getNumbersY() {
        for (int x = 1; x <= width; x++) {
            int length = 0;
            countNumbersY[x] = 1;
            for (int y = 1; y <= height; y++) {
                if (main.data[x][y].isBlack()) {
                    length++;
                    continue;
                }

                if (length != 0) {
                    numbersY[x][countNumbersY[x]] = length;
                    length = 0;
                    countNumbersY[x]++;
                }
            }
            if (length != 0) {
                numbersY[x][countNumbersY[x]] = length;
                countNumbersY[x]++;
            }
            countNumbersY[x]--;
        }
    }

    // дает все суммы в двух блоках чисел по X и по Y
    // нужно для проверки валидности кроссворда
    public Pt check() {
        int sum1 = 0;
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= countNumbersY[x]; y++) {
                sum1 += numbersY[x][y];
            }
        }
        int sum2 = 0;
        for (int y = 1; y <= height; y++) {
            for (int x = 1; x <= countNumbersX[y]; x++) {
                sum2 += numbersX[x][y];
            }
        }
        return new Pt(sum1, sum2);  // разница рядов
    }

    
    public void printField() {
//        System.out.println(printAll());
//        System.out.println();
//        System.out.println();
    }

    // TODO порефакторить
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

        // проверка на совпадение рядов
        Pt pt = check();
        int x0 = Math.abs(pt.x - pt.y);
        if (x0 > 0) {
            System.out.println("Ошибка! Несовпадение на " + x0);
            return;
        }

        for (int x = 1; x <= width; x++) {
            main.tchY[x] = true;
            assumptionBlack.tchY[x] = true;
            assumptionWhite.tchY[x] = true;
        }
        for (int y = 1; y <= height; y++) {
            main.tchX[y] = true;
            assumptionBlack.tchX[y] = true;
            assumptionWhite.tchX[y] = true;
        }
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                main.noSet[x][y] = false;
                assumptionBlack.noSet[x][y] = false;
                assumptionWhite.noSet[x][y] = false;
            }
        }
        ableToNewAssumption = false;
        b7 = false;
        wasError = false;
        // для пропуска прогона по у если в группе x и чисел меньше
        // (значения больше) и длинна строки (группы) меньше, это все для ускорения
        double a1 = 0;
        for (int x = 1; x <= width; x++) {
            a1 = a1 + countNumbersY[x];
        }
        double a2 = 0;
        for (int y = 1; y <= height; y++) {
            a2 = a2 + countNumbersX[y];
        }
        b11 = (a1 / height > a2 / width);
        //----------------------------------------------------------
        assumption = new Assumption();
        wasChanges = false;
        AllData data;
        do {
            if (wasChanges && b11) {
                b11 = false;
            }
            wasChanges = false;

            data = getData();

            if (!ableToNewAssumption && !b11) {
                // при поиске другой точки
                // или если LenX больше LenY (в начале)
                for (int y = 1; y <= height; y++) {
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
                    for (int x = 1; x <= width; x++) {
                        data.probability[x][y][Dot.BLACK.code()] = lineSolver.probability(x);

                        if (data.data[x][y] != lineSolver.dots(x)) {
                            data.data[x][y] = lineSolver.dots(x);
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
                for (int x = 1; x <= width; x++) { // дальше то же только для столбцов
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

                    for (int y = 1; y <= height; y++) {
                        data.probability[x][y][Dot.WHITE.code()] = lineSolver.probability(y);

                        if (data.data[x][y] != lineSolver.dots(y)) {
                            data.data[x][y] = lineSolver.dots(y);
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
                        // пока вероятности такие
                        double max1 = 0;
                        double max2 = 0;
                        foundMaxProbDot = false;
                        for (int x = 1; x <= width; x++) {  // по всему полю
                            for (int y = 1; y <= height; y++) {
                                if ((max1 <= main.probability[x][y][Dot.BLACK.code()])
                                        && (max2 <= main.probability[x][y][Dot.WHITE.code()])
                                        && (main.probability[x][y][Dot.BLACK.code()] < 1.0) // TODO тут странно, вероятность белой точки вроде как EXACLTY_WHITE
                                        && (main.probability[x][y][Dot.WHITE.code()] < 1.0)) { // ищем наиболее вероятную точку, но не с вероятностью 1 и 0
                                    if (main.noSet[x][y]) continue;
                                    max1 = main.probability[x][y][Dot.BLACK.code()];
                                    max2 = main.probability[x][y][Dot.WHITE.code()];
                                    pt = new Pt(x, y);
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
                            data.updateAllFinCh(false);
                            b7 = true; // последний прогон для нормального отображения вероятностей
                        }
                        ableToNewAssumption = false;
                    }
                }
            }
            if (wasError) wasChanges = false; // все конец
        } while (wasChanges);

        data.updateAllFinCh(false);
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                switch (main.data[x][y]) {
                    case BLACK: {
                        main.probability[x][y][Dot.BLACK.code()] = EXACTLY_BLACK;
                        main.probability[x][y][Dot.WHITE.code()] = EXACTLY_NOT_BLACK;
                    }
                    break;
                    case WHITE: {
                        main.probability[x][y][Dot.BLACK.code()] = EXACTLY_NOT_BLACK;
                        main.probability[x][y][Dot.WHITE.code()] = EXACTLY_BLACK;
                    }
                    break;
                }
            }
        }

        printField(); // прорисовка поля
    }

    // Так как если мы зайдем в тупик и явно ничего не сможем отгадать
    // алгоритм будет пытаться отгадывать методом научного тыка в точку с максимальной вероятностью
    // есть три сценария:
    // 1) мы поставили точку черную и попробовали решить кроссворд в assumptionBlack объекте
    // 2) если 1) приводит к ошибке решения или не решается полностью, мы ставим точку белую и пробуем решать в assumptionWhite объенкте
    // 3) если и 2) привело к ошибке, значит ошибка кроссворда, потому что ни черную ни белую мы не можем поставить
    // 4) может случиться так, что ни в 1) ни в 2) ошибок нет (в решении продвинулись но не до конца), тогда мы точно не знаем какого цвета там точка
    // и тогда нам надо вернуться в основной режим с объектом main и порпобовать то же где-то еще
    private AllData getData() {
        if (assumption.isBlack()) {
            return assumptionBlack;
        }

        if (assumption.isWhite()) {
            return assumptionWhite;
        }

        return main;
    }

    private int[] numbersX(int y) {
        int count = countNumbersX[y];
        int[] result = new int[count + 1];
        for (int x = 1; x <= count; x++) {
            result[x] = numbersX[x][y];
        }
        return result;
    }

    private int[] numbersY(int x) {
        int count = countNumbersY[x];
        int[] result = new int[count + 1];
        for (int y = 1; y <= count; y++) {
            result[y] = numbersY[x][y];
        }
        return result;
    }

    private void draw(Pt pt) {

    }
    
    public void updateAssumptionDot(Dot dot) {
        AllData data = getAssumptionData(dot);
        data.update(assumption);
        draw(assumption.at());
    }
    
    public void applyAssumptionData(Dot dot) {
        AllData data;
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

    // TODO порефакторить
    public void tryAssumption(Pt pt, Dot dot) {
        assumption.start(pt, dot);

        AllData data = getAssumptionData(dot);
        for (int x = 1; x <= width; x++) { // по всему полю
            for (int y = 1; y <= height; y++) {
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
        for (int x = 1; x <= width; x++) {
            data.chY[x] = main.chY[x];
            data.finY[x] = main.finY[x];
            data.tchY[x] = false;
        }
        for (int y = 1; y <= height; y++) {
            data.chX[y] = main.chX[y];
            data.finX[y] = main.finX[y];
            data.tchX[y] = false;
        }
        updateAssumptionDot(dot);
    }

    private AllData getAssumptionData(Dot dot) {
        if (dot.isBlack()) {
            return assumptionBlack;
        } else {
            return assumptionWhite;
        }
    }

    // TODO порефакторить
    public boolean getFin(AllData data) {
        // заполнение поля
        boolean c2 = true;
        for (int y = 1; y <= height; y++) {
            boolean c = false; // флаг закончености строки
            for (int x = 1; x <= width; x++) {  // по строке
                c = c || (data.data[x][y] == Dot.UNSET); // если заполнено
            }
            c2 = c2 && (!c);
            data.finX[y] = !c;
        }
        for (int x = 1; x <= width; x++) {
            boolean c = false;
            for (int y = 1; y <= height; y++) {
                c = c || (data.data[x][y] == Dot.UNSET);
            }
            c2 = c2 && (!c);
            data.finY[x] = !c;
        }
        if (c2) {
            if (data == main) {
                return c2;
            }
            applyAssumptionData(assumption.color());
        }
        return c2;
    }

    public String getOpened() {
        int count = 0;
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                if (main.data[x][y] != Dot.UNSET) {
                    count++;
                }
            }
        }
        return "Открыто: " + Double.toString(Math.round(1000 * count / (width * height)) / 10) + "%(" + count + ")";
    }

    @Override
    public int size() {
        return Math.max(width + offsetX, height + offsetY);
    }

    // используется для отрисовки состояния кроссворда в текстовом представлении принтером
    @Override
    public void addAll(Player player, Consumer<Iterable<? extends Point>> processor) {
        List<Point> pixels = new LinkedList<>() ;
        main.addAll(player, list -> pixels.addAll((Collection) list));
        pixels.forEach(pixel -> pixel.moveDelta(pt(offsetX, 0)));

        List<Point> numbers = new LinkedList<>() ;
        int dx = offsetX; // горизонтальные циферки вверху, должны быть
        int dy = height;  // потому мы их смещаем туда
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= countNumbersY[x]; y++) {
                int dyy = countNumbersY[x] + 1; // зеркальное отображение по вертикали
                numbers.add(new Number(pt(x - 1 + dx, dyy - y - 1 + dy), numbersY[x][y]));
            }
        }
        for (int y = 1; y <= height; y++) {
            for (int x = 1; x <= countNumbersX[y]; x++) {
                int dxx = offsetX - countNumbersX[y]; // атут надо смещать хитрее
                int dyy = height + 1; // зеркальное отображение по вертикали
                numbers.add(new Number(pt(x - 1 + dxx, dyy - y - 1), numbersX[x][y]));
            }
        }

        processor.accept(pixels);
        processor.accept(numbers);

    }

    public void load(Level level) {
        getOffset(level);

        // X рядки чисел
        int width = level.size() - offsetX;
        size(width, width);

        List<Numbers> linesX = lines(level, Number::getY)
                .subList(0, width);
        linesX.forEach(numbers -> numbers.fill(offsetX, false));
        Collections.reverse(linesX); // зеркально отображаем по вертикали

        // Y стобики чисел
        List<Numbers> lines = lines(level, Number::getX);
        List<Numbers> linesY = lines
                .subList(offsetX, lines.size());
        linesY.forEach(Numbers::reverse); // зеркально отображаем по вертикали
        linesY.forEach(numbers -> numbers.fill(offsetX, false));


        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                main.data[x][y] = Dot.UNSET;
            }
        }

        for (int y = 1; y <= offsetX; y++) {
            for (int x = 1; x <= width; x++) {
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
            pt.moveDelta(pt(1, -1));
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

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void size(int width, int height) {
        this.width = width;
        this.height = height;

        main = new AllData(width, height);
        assumptionBlack = new AllData(width, height);
        assumptionWhite = new AllData(width, height);

        numbersX = new int[width + 1][height + 1];
        numbersY = new int[width + 1][height + 1];
        countNumbersX = new int[height + 1];
        countNumbersY = new int[width + 1];
    }

    public void offset(int x, int y) {
        offsetX = x;
        offsetY = y;
    }

    public void setDot(int x, int y, Dot dot) {
        main.data[x][y] = dot;
    }

    public Dot getDot(int x, int y) {
        return main.data[x][y];
    }

    public int countNumbersX(int y) {
        return countNumbersX[y];
    }

    public int numbersX(int x, int y) {
        return numbersX[x][y];
    }

    public int countNumbersY(int x) {
        return countNumbersY[x];
    }

    public int numbersY(int x, int y) {
        return numbersY[x][y];
    }

    public int[][] numbersX() {
        return numbersX;
    }

    public int[][] numbersY() {
        return numbersY;
    }

    public int[] countNumbersX() {
        return countNumbersX;
    }

    public int[] countNumbersY() {
        return countNumbersY;
    }
}
