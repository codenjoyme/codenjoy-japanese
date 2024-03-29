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
import com.codenjoy.dojo.japanese.model.items.Color;
import com.codenjoy.dojo.japanese.model.items.Pixel;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static com.codenjoy.dojo.japanese.model.portable.Solver.*;
import static com.codenjoy.dojo.services.PointImpl.pt;

public class AllData implements BoardReader<Player> {

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
    public void addAll(Player player, Consumer<Iterable<? extends Point>> processor) {
        List<Pixel> result = new LinkedList<>();
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                // конвертирование потому что в этом коде черный и белый отличаются от codenjoy'ного
                int color = data[x][y].code() - 1;
                // так же надо отступить, потому что в этом коде индексы начинаются с 0
                int dy = height + 1; // зеркальное отображение по вертикали
                result.add(new Pixel(pt(x - 1, (dy) - y - 1), Color.get(color)));
            }
        }
        processor.accept(result);
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
            probability[pt.x][pt.y][Dot.BLACK.code()] = EXACTLY_BLACK;
            probability[pt.x][pt.y][Dot.WHITE.code()] = EXACTLY_NOT_BLACK;
        } else {
            data[pt.x][pt.y] = Dot.WHITE;
            // меняем вероятности
            probability[pt.x][pt.y][Dot.BLACK.code()] = EXACTLY_NOT_BLACK;
            probability[pt.x][pt.y][Dot.WHITE.code()] = EXACTLY_BLACK;
        }
        // строка и солбец, содержащие эту точку пересчитать
        chX[pt.y] = true;
        chY[pt.x] = true;
        finX[pt.y] = false;
        finY[pt.x] = false;
    }
}
