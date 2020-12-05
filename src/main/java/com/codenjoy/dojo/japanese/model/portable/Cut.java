package com.codenjoy.dojo.japanese.model.portable;

import static com.codenjoy.dojo.japanese.model.portable.Solver.EXACTLY_BLACK;
import static com.codenjoy.dojo.japanese.model.portable.Solver.EXACTLY_NOT_BLACK;

public class Cut {

    private LineSolver solver;

    public Cut(LineSolver solver) {
        this.solver = solver;
    }

    // метод пытается с начала и конца ряда определить ряды точек, которые уже открыты полностью.
    // возвращает true если есть над чем гонять комбинации и даем возможность перебрать их
    // но перебирать будем только на тех числах, которые остались после оптимизации и в диапазоне точек
    // from ... to
    // TODO продолжить рефакторить
    public boolean process() {
        // идем по ряду в прямом порядке
        Dot previous = Dot.WHITE;
        int numbersIndex = 0;
        int countDots = 0; // количество точек в ряду
        int i = 1;
        solver.range().from(i);
        boolean stop = false;
        do {
            switch (solver.dots(i)) {
                case UNSET: {
                    if (previous.isBlack()) {
                        // UNSET после BLACK - надо заканчивать ряд
                        if (countDots < solver.numbers(numbersIndex)) {
                            // незакончили numbersIndex'ный ряд
                            countDots++; // количество точек
                            solver.probabilities().set(i, EXACTLY_BLACK);
                            previous = Dot.BLACK;
                        } else {
                            // закончили numbersIndex'ный ряд
                            countDots = 0; // новый ряд еще не начали
                            solver.probabilities().set(i, EXACTLY_NOT_BLACK);
                            previous = Dot.WHITE;

                            solver.SHLNumbers(); // сдвигаем ряд (удаляем первый элемент)
                            numbersIndex--; // из за смещения
                        }
                    } else {
                        // UNSET после WHITE
                        if (solver.countNumbers() == 0) {
                            // ряд пустой, тут все WHITE
                            solver.probabilities().set(i, EXACTLY_NOT_BLACK);
                        } else {
                            solver.range().from(i);
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
                    solver.probabilities().set(i, EXACTLY_BLACK);
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

                        solver.SHLNumbers(); // сдвигаем ряд (удаляем первый элемент)
                        numbersIndex--; // из за смещения
                    }
                    solver.probabilities().set(i, EXACTLY_NOT_BLACK);
                }
                break;
            }
            i++;
            // TODO подумать почему тут нельзя перебирать комбинации
            if ((!stop && i > solver.length()) || solver.countNumbers() == 0) return false; // достигли конца
        } while (!stop);

        // идем по ряду в обратном порядке
        previous = Dot.WHITE;
        numbersIndex = solver.countNumbers() + 1;
        countDots = 0;
        i = solver.length();
        solver.range().to(i);
        stop = false;
        do {
            switch (solver.dots(i)) {
                case UNSET: {
                    if (previous.isBlack()) {
                        // UNSET после BLACK - надо заканчивать ряд
                        if (countDots < solver.numbers(numbersIndex)) {
                            // незакончили numbersIndex'ный ряд
                            countDots++; // количество точек
                            solver.probabilities().set(i, EXACTLY_BLACK);
                            previous = Dot.BLACK;
                        } else {
                            // закончили numbersIndex'ный ряд
                            countDots = 0; // новый ряд еще не начали
                            solver.probabilities().set(i, EXACTLY_NOT_BLACK);
                            previous = Dot.WHITE;
                            solver.countNumbers(solver.countNumbers() - 1);
                        }
                    } else {
                        // WHITE после пустоты
                        if (solver.countNumbers() == 0) { // в этом ряде ничего больше делать нечего
                            solver.probabilities().set(i, EXACTLY_NOT_BLACK); // кончаем его:) // TODO тут скорее UNKNOWN
                        } else {
                            solver.range().to(i);
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
                    solver.probabilities().set(i, EXACTLY_BLACK);
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
                        solver.countNumbers(solver.countNumbers() - 1);
                    }
                    solver.probabilities().set(i, EXACTLY_NOT_BLACK);
                }
                break;
            }
            i--;
            // TODO подумать почему тут нельзя перебирать комбинации
            if ((!stop && i < 1) || solver.countNumbers() == 0) return false; // достигли конца
        } while (!stop);

        solver.range().calcLength();
        // если остались ряды точек то надо прогнать генератор, чем сообщаем возвращая true
        return solver.countNumbers() != 0 && solver.range().exists();
    }

}
