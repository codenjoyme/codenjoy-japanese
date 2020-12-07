package com.codenjoy.dojo.japanese.model.portable;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static com.codenjoy.dojo.japanese.model.portable.Solver.EXACTLY_NOT_BLACK;
import static java.util.stream.Collectors.joining;

public class LineSolver {

    private Dot[] dots;
    private int length;

    private int[] numbers;
    private int countNumbers;

    private Blocks blocks;
    private Probabilities probabilities;

    protected boolean enableHistory = false;
    private List<String> history;

    public boolean calculate(int[] inputNumbers, Dot[] inputDots) {
        dots = inputDots;
        numbers = Arrays.stream(inputNumbers)
                .filter(number -> number != 0)
                .toArray();
        int[] copy = new int[numbers.length + 1];
        System.arraycopy(numbers, 0, copy, 1,
                Math.min(numbers.length, numbers.length + 1));
        numbers = copy;

        length = dots.length - 1;
        countNumbers = numbers.length - 1;

        probabilities = new Probabilities(length);
        blocks = new Blocks(countNumbers*2 + 1);
        history = new LinkedList<>();

        // если цифер нет вообще, то в этом ряде не может быть никаких BLACK
        // если кто-то в ходе проверки гипотезы все же сделал это, то
        // мы все равно поставим все белые и скажем что calculate этого сценария невозможен
        if (countNumbers == 0) {
            boolean foundBlack = false;
            for (int i = 1; i <= length; i++) {
                if (dots[i] == Dot.BLACK) {
                    foundBlack = true;
                }
                dots[i] = Dot.WHITE;
                probabilities.set(i, EXACTLY_NOT_BLACK);
            }
            return !foundBlack;
        }

        boolean result = generateCombinations();

        // и превращаем явные (1.0, 0.0) probabilities в dots
        probabilities.updateDots(dots);

        // возвращаем возможность этой комбинации случиться для этого ряда чисел
        return result;
    }

    public String toString(boolean applicable) {
        String nums = IntStream.range(1, numbers.length)
                .mapToObj(index -> numbers[index])
                .map(number -> String.valueOf(number))
                .collect(joining(","));

        boolean[] combinations = probabilities.combinations();
        String dots = IntStream.range(1, length + 1)
                .mapToObj(index -> combinations[index])
                .map(b -> (b)?"*":".")
                .collect(joining(""));

        return String.format("%s[%s]:%s", applicable ? "+" : "-", nums, dots);
    }

    private boolean generateCombinations() {
        if (!blocks.packTightToTheLeft(numbers, countNumbers, length)) {
            // если упаковка не удалась (места не хватило), то дальше нет смысла считать
            return false;
        }

        // пошли генерить комбинации
        do {
            blocks.saveCombinations(probabilities.combinations());
            boolean applicable = probabilities.isApplicable(dots);
            if (enableHistory) {
                history.add(toString(applicable));
            }
            if (applicable) {
                probabilities.addCombination();
            }
        } while (blocks.hasNext());

        probabilities.calculate();
        return probabilities.isAny();
    }

    public double probability(int x) {
        return probabilities.get(x);
    }

    public Dot dots(int x) {
        return dots[x];
    }

    public int length() {
        return length;
    }

    public int numbers(int index) {
        return numbers[index];
    }

    public List<String> history() {
        return history;
    }
}
