package com.codenjoy.dojo.japanese.model.portable;

import com.codenjoy.dojo.japanese.model.items.Color;
import com.codenjoy.dojo.japanese.model.items.Pixel;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

class TAllData implements BoardReader {

    public double[][][] probability = new double[Solver.MAX + 1][Solver.MAX + 1][3];
    public boolean[] finX = new boolean[Solver.MAX + 1];
    public boolean[] finY = new boolean[Solver.MAX + 1];
    public boolean[] chX = new boolean[Solver.MAX + 1];
    public boolean[] chY = new boolean[Solver.MAX + 1];
    public Dot[][] data = new Dot[Solver.MAX + 1][Solver.MAX + 1];
    public boolean[] tchX = new boolean[Solver.MAX + 1];
    public boolean[] tchY = new boolean[Solver.MAX + 1];
    public boolean[][] noSet = new boolean[Solver.MAX + 1][Solver.MAX + 1];

    public Dot[] dataX(int y) {
        int len = Solver.width;
        Dot[] result = new Dot[len + 1];
        for (int x = 1; x <= len; x++) {
            result[x] = data[x][y];
        }
        return result;
    }

    public Dot[] dataY(int x) {
        int len = Solver.height;
        Dot[] result = new Dot[len + 1];
        for (int y = 1; y <= len; y++) {
            result[y] = data[x][y];
        }
        return result;
    }

    @Override
    public int size() {
        if (Solver.width != Solver.height) {
            throw new RuntimeException("Кроссворд не прямоугольный");
        }

        return Solver.width;
    }

    @Override
    public Iterable<? extends Point> elements() {
        List<Pixel> result = new LinkedList<>();
        for (int x = 1; x <= Solver.width; x++) {
            for (int y = 1; y <= Solver.height; y++) {
                // инвертирование потому что в этом коде черный и белый отличаются от codenjoy'ного
                int inverted = Math.abs(data[x][y].code() - 1);
                // так же надо отступить, потому что в этом коде индексы начинаются с 0
                result.add(new Pixel(pt(x - 1, y - 1), Color.get(inverted)));
            }
        }
        return result;
    }
}
