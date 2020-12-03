package com.codenjoy.dojo.japanese.model.portable;

import com.codenjoy.dojo.japanese.model.items.Color;
import com.codenjoy.dojo.japanese.model.items.Pixel;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.japanese.model.portable.Solver.*;
import static com.codenjoy.dojo.services.PointImpl.pt;

public class AllData implements BoardReader {

    private final int width;
    private final int height;
    public double[][][] probability;
    public boolean[] finX;
    public boolean[] finY;
    public boolean[] chX;
    public boolean[] chY;
    public Dot[][] data;
    public boolean[] tchX;
    public boolean[] tchY;
    public boolean[][] noSet;

    public AllData(int width, int height) {
        this.width = width;
        this.height = height;

        probability = new double[width + 1][height + 1][3];
        finX = new boolean[height + 1];
        finY = new boolean[width + 1];
        chX = new boolean[height + 1];
        chY = new boolean[width + 1];
        data = new Dot[width + 1][height + 1];
        tchX = new boolean[height + 1];
        tchY = new boolean[width + 1];
        noSet = new boolean[width + 1][height + 1];
    }

    public Dot[] dataX(int y) {
        Dot[] result = new Dot[width + 1];
        for (int x = 1; x <= width; x++) {
            result[x] = data[x][y];
        }
        return result;
    }

    public Dot[] dataY(int x) {
        Dot[] result = new Dot[height + 1];
        for (int y = 1; y <= height; y++) {
            result[y] = data[x][y];
        }
        return result;
    }

    // очистка масивов флагов заполнености
    public void updateAllFinCh(boolean flag) {
        for (int x = 1; x <= width; x++) {
            chY[x] = !flag;
            finY[x] = flag;
        }
        for (int y = 1; y <= height; y++) {
            chX[y] = !flag;
            finX[y] = flag;
        }
    }

    @Override
    public int size() {
        if (width != height) {
            throw new RuntimeException("Кроссворд не прямоугольный");
        }

        return width;
    }

    @Override
    public Iterable<? extends Point> elements() {
        List<Pixel> result = new LinkedList<>();
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                // конвертирование потому что в этом коде черный и белый отличаются от codenjoy'ного
                int color = data[x][y].code() - 1;
                // так же надо отступить, потому что в этом коде индексы начинаются с 0
                result.add(new Pixel(pt(x - 1, y - 1), Color.get(color)));
            }
        }
        return result;
    }

    public void clear() {
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                data[x][y] = Dot.UNSET;
                probability[x][y][Dot.BLACK.code()] = UNKNOWN;
                probability[x][y][Dot.WHITE.code()] = UNKNOWN;
            }
        }
        for (int x = 1; x <= width; x++) {
            finY[x] = false;
            chY[x] = true;
        }
        for (int y = 1; y <= height; y++) {
            finX[y] = false;
            chX[y] = true;
        }
    }

    public void update(Assumption assumption) {
        Pt pt = assumption.at();
        if (assumption.color().isBlack()) {
            data[pt.x][pt.y] = Dot.BLACK;
            // меняем вероятности
            probability[pt.x][pt.y][Dot.BLACK.code()] = EXACTLY;
            probability[pt.x][pt.y][Dot.WHITE.code()] = EXACTLY_NOT;
        } else {
            data[pt.x][pt.y] = Dot.WHITE;
            // меняем вероятности
            probability[pt.x][pt.y][Dot.BLACK.code()] = EXACTLY_NOT;
            probability[pt.x][pt.y][Dot.WHITE.code()] = EXACTLY;
        }
        // строка и солбец, содержащие эту точку пересчитать
        chX[pt.y] = true;
        chY[pt.x] = true;
        finX[pt.y] = false;
        finY[pt.x] = false;
    }
}
