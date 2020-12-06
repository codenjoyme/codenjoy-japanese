package com.codenjoy.dojo.japanese.model.portable;

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

    public void addCombination(Range range) {
        combinations++;

        range.iterate(i -> {
            if (dots[i]) {
                probabilities[i]++;
            }
        });
    }

    public boolean isAny() {
        return combinations != 0;
    }

    public void calculate(Range range) {
        range.iterate(i -> {
            if (combinations != 0) {
                probabilities[i] -= UNKNOWN; // TODO так как изначально у нас был -1, его надо отнять
                probabilities[i] = probabilities[i] / combinations;
            } else {
                probabilities[i] = UNKNOWN;
            }
        });
    }

    public boolean isApplicable(Range range, Dot[] reference) {
        for (int i = range.from(); i <= range.to(); i++) {
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
