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

import static com.codenjoy.dojo.japanese.model.portable.Solver.*;

public class Probabilities {

    private int length;
    private boolean[] dots;
    private int combinations;
    private double[] probabilities;

    public Probabilities(int length) {
        this.length = length;

        // TODO тут в некоторых тестах case_b15 случается переполнение, потому тут массив большой
        dots = new boolean[length + 1 + 100];
        probabilities = new double[length + 1 + 100];

        // изначально вероятности у нас неясные
        for (int i = 1; i <= length; i++) {
            set(i, UNKNOWN);
        }

        combinations = 0;
    }

    public void addCombination() {
        combinations++;

        for (int i = 1; i <= length; i++) {
            if (dots[i]) {
                probabilities[i]++;
            }
        }
    }

    public boolean isAny() {
        return combinations != 0;
    }

    public void calculate() {
        for (int i = 1; i <= length; i++) {
            if (combinations != 0) {
                probabilities[i] -= UNKNOWN; // TODO так как изначально у нас был -1, его надо отнять
                probabilities[i] = probabilities[i] / combinations;
            } else {
                probabilities[i] = UNKNOWN;
            }
        }
    }

    public boolean isApplicable(Dot[] reference) {
        for (int i = 1; i <= length; i++) {
            boolean isBlack = dots[i];
            switch (reference[i]) {
                case UNSET:
                case ASSUMPTION:
                    break;
                case BLACK:
                    if (!isBlack) {
                        return false;
                    }
                    break;
                case WHITE:
                    if (isBlack) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    // после того, как у нас есть массив вероятностей для всех возможных комбинаций
    // мы в праве утверждать, что ячейки с вероятностями 1.0 и 0.0 могут быть
    // заполнены BLACK и WHITE соответственно
    public void updateDots(Dot[] recipient) {
        for (int i = 1; i <= length; i++) {
            if (get(i) == EXACTLY_BLACK) {
                recipient[i] = Dot.BLACK;
            }
            if (get(i) == EXACTLY_NOT_BLACK) {
                recipient[i] = Dot.WHITE;
            }
        }
    }

    public void set(int offset, double probability) {
        probabilities[offset] = probability;
    }

    public double get(int offset) {
        return probabilities[offset];
    }

    public boolean[] combinations() {
        return dots;
    }
}
