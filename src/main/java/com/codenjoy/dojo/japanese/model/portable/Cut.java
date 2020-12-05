package com.codenjoy.dojo.japanese.model.portable;

import static com.codenjoy.dojo.japanese.model.portable.Solver.EXACTLY_BLACK;
import static com.codenjoy.dojo.japanese.model.portable.Solver.EXACTLY_NOT_BLACK;

public class Cut {

    private LineSolver solver;
    private Dot previous;
    private int numbersIndex;
    private int countDots;  // количество точек в ряду
    private int index;
    private boolean stop;

    public Cut(LineSolver solver) {
        this.solver = solver;
    }

    // метод пытается с начала и конца ряда определить ряды точек, которые уже открыты полностью.
    // возвращает true если есть над чем гонять комбинации и даем возможность перебрать их
    // но перебирать будем только на тех числах, которые остались после оптимизации и в диапазоне точек
    // from ... to
    public boolean process() {
        // идем по ряду в прямом порядке
        boolean order = true;
        init(order);
        do {
            switch (solver.dots(index)) {
                case UNSET: {
                    if (previous.isBlack()) {
                        // UNSET после BLACK - надо заканчивать ряд
                        if (countDots < solver.numbers(numbersIndex)) {
                            // незакончили numbersIndex'ный ряд
                            countDots++; // количество точек
                            solver.probabilities().set(index, EXACTLY_BLACK);
                            previous = Dot.BLACK;
                        } else {
                            // закончили numbersIndex'ный ряд
                            countDots = 0; // новый ряд еще не начали
                            solver.probabilities().set(index, EXACTLY_NOT_BLACK);
                            previous = Dot.WHITE;

                            removeNumbers(order);
                        }
                    } else {
                        // UNSET после WHITE
                        if (solver.countNumbers() == 0) {
                            // ряд пустой, тут все WHITE
                            solver.probabilities().set(index, EXACTLY_NOT_BLACK);
                        } else {
                            solver.range().from(index);
                            stop = true; // иначе выходим
                        }
                    }
                }
                break;
                case BLACK: {
                    if (previous.isWhite()) {
                        numbersIndex++;
                        countDots = 0;
                        previous = Dot.BLACK;
                    }
                    solver.probabilities().set(index, EXACTLY_BLACK);
                    countDots++;
                }
                break;
                case WHITE: {
                    if (previous.isBlack()) {
                        if (countDots != solver.numbers(numbersIndex)) {
                            // TODO подумать почему тут можно перебирать комбинации
                            return true;
                        }
                        previous = Dot.WHITE;

                        removeNumbers(order);
                    }
                    solver.probabilities().set(index, EXACTLY_NOT_BLACK);
                }
                break;
            }
            index++;
            // TODO подумать почему тут нельзя перебирать комбинации
            if ((!stop && index > solver.length()) || solver.countNumbers() == 0) return false; // достигли конца
        } while (!stop);

        order = false;
        init(order);
        do {
            switch (solver.dots(index)) {
                case UNSET: {
                    if (previous.isBlack()) {
                        // UNSET после BLACK - надо заканчивать ряд
                        if (countDots < solver.numbers(numbersIndex)) {
                            // незакончили numbersIndex'ный ряд
                            countDots++; // количество точек
                            solver.probabilities().set(index, EXACTLY_BLACK);
                            previous = Dot.BLACK;
                        } else {
                            // закончили numbersIndex'ный ряд
                            countDots = 0; // новый ряд еще не начали
                            solver.probabilities().set(index, EXACTLY_NOT_BLACK);
                            previous = Dot.WHITE;
                            removeNumbers(order);
                        }
                    } else {
                        // WHITE после пустоты
                        if (solver.countNumbers() == 0) { // в этом ряде ничего больше делать нечего
                            solver.probabilities().set(index, EXACTLY_NOT_BLACK); // кончаем его:) // TODO тут скорее UNKNOWN
                        } else {
                            solver.range().to(index);
                            stop = true; // иначе выходим
                        }
                    }
                }
                break;
                case BLACK: {
                    if (previous.isWhite()) {
                        numbersIndex--;
                        countDots = 0;
                        previous = Dot.BLACK;
                    }
                    solver.probabilities().set(index, EXACTLY_BLACK);
                    countDots++;
                }
                break;
                case WHITE: {
                    if (previous.isBlack()) {
                        if (countDots != solver.numbers(numbersIndex)) {
                            // TODO подумать почему тут можно перебирать комбинации
                            return true;
                        }
                        previous = Dot.WHITE;
                        removeNumbers(order);
                    }
                    solver.probabilities().set(index, EXACTLY_NOT_BLACK);
                }
                break;
            }
            index--;
            // TODO подумать почему тут нельзя перебирать комбинации
            if ((!stop && index < 1) || solver.countNumbers() == 0) return false; // достигли конца
        } while (!stop);

        solver.range().calcLength();
        // если остались ряды точек то надо прогнать генератор, чем сообщаем возвращая true
        return solver.countNumbers() != 0 && solver.range().exists();
    }

    private void removeNumbers(boolean order) {
        if (order) {
            solver.SHLNumbers(); // сдвигаем ряд (удаляем первый элемент)
            numbersIndex--; // из за смещения
        } else {
            solver.countNumbers(solver.countNumbers() - 1);
        }
    }

    private void init(boolean order) {
        countDots = 0;
        stop = false;
        previous = Dot.WHITE;
        if (order) {
            // идем по ряду в прямом порядке
            numbersIndex = 0;
            index = 1;
            solver.range().from(index);
        } else {
            // идем по ряду в обратном порядке
            numbersIndex = solver.countNumbers() + 1;
            index = solver.length();
            solver.range().to(index);
        }
    }

}
