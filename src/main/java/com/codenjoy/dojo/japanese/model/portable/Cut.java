package com.codenjoy.dojo.japanese.model.portable;

import static com.codenjoy.dojo.japanese.model.portable.Solver.EXACTLY_BLACK;
import static com.codenjoy.dojo.japanese.model.portable.Solver.EXACTLY_NOT_BLACK;

public class Cut {

    public static final boolean FORWARD = true;
    public static final boolean BACKWARD = false;

    private LineSolver solver;
    private Dot previous;
    private int numbersIndex;
    private int countDots;  // количество точек в ряду
    private int index;
    private boolean stop;
    private boolean order;

    public Cut(LineSolver solver) {
        this.solver = solver;
    }

    // метод пытается с начала и конца ряда определить ряды точек, которые уже открыты полностью.
    // возвращает true если есть над чем гонять комбинации и даем возможность перебрать их
    // но перебирать будем только на тех числах, которые остались после оптимизации и в диапазоне точек
    // from ... to
    public boolean process() {
        Boolean result = go(FORWARD);
        if (result != null) return result;

        result = go(BACKWARD);
        if (result != null) return result;

        // если остались ряды точек то надо прогнать генератор,
        // чем сообщаем возвращая true
        return solver.countNumbers() != 0
                && solver.range().exists();
    }

    private Boolean go(boolean order) {
        this.order = order;
        init();
        do {
            switch (solver.dots(index)) {
                case UNSET: {
                    processUnset();
                }
                break;
                case BLACK: {
                    processBlack();
                }
                break;
                case WHITE: {
                    if (processWhite()) {
                        return true;
                    }
                }
                break;
            }
            changeIndex();
            if ((!stop && checkOutOf()) || solver.countNumbers() == 0) {
                // TODO подумать почему тут нельзя перебирать комбинации
                return false; // достигли конца
            }
        } while (!stop);

        return null;
    }

    private boolean processWhite() {
        if (previous.isBlack()) {
            // WHITE после BLACK
            if (countDots != solver.numbers(numbersIndex)) {
                // TODO подумать почему тут можно перебирать комбинации
                return true;
            }

            removeNumbers();
        }
        setDot(Dot.WHITE);
        return false;
    }

    private void processBlack() {
        if (previous.isWhite()) {
            // BLACK после WHITE
            changeNumberIndex();
            countDots = 0;
        }
        setDot(Dot.BLACK);
        countDots++;
    }

    private void processUnset() {
        if (previous.isBlack()) {
            // UNSET после BLACK - надо заканчивать ряд
            if (countDots < solver.numbers(numbersIndex)) {
                // незакончили numbersIndex'ный ряд
                countDots++;
                setDot(Dot.BLACK);
            } else {
                // закончили numbersIndex'ный ряд
                countDots = 0; // новый ряд еще не начали
                setDot(Dot.WHITE);
                removeNumbers();
            }
        } else {
            // UNSET после WHITE
            if (solver.countNumbers() == 0) {
                // ряд пустой, тут все WHITE
                setDot(Dot.WHITE);
            } else {
                updateRange();
                stop = true; // иначе выходим
            }
        }
    }

    private void setDot(Dot color) {
        previous = color;
        solver.probabilities().set(index,
                (color.isBlack()) ? EXACTLY_BLACK : EXACTLY_NOT_BLACK);
    }

    private boolean checkOutOf() {
        if (order == FORWARD) {
            return index > solver.length();
        } else {
            return index < 1;
        }
    }

    private void changeIndex() {
        if (order == FORWARD) {
            index++;
        } else {
            index--;
        }
    }

    private void changeNumberIndex() {
        if (order == FORWARD) {
            numbersIndex++;
        } else {
            numbersIndex--;
        }
    }

    private void updateRange() {
        if (order == FORWARD) {
            solver.range().from(index);
        } else {
            solver.range().to(index);
        }
    }

    private void removeNumbers() {
        if (order == FORWARD) {
            solver.shlNumbers(); // сдвигаем ряд (удаляем первый элемент)
            numbersIndex--; // из за смещения
        } else {
            solver.countNumbers(solver.countNumbers() - 1);
        }
    }

    private void init() {
        countDots = 0;
        stop = false;
        previous = Dot.WHITE;
        if (order == FORWARD) {
            numbersIndex = 0;
            index = 1;
            solver.range().from(index);
        } else {
            numbersIndex = solver.countNumbers() + 1;
            index = solver.length();
            solver.range().to(index);
        }
    }

}
