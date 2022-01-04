package com.codenjoy.dojo.japanese.model.level;

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

import com.codenjoy.dojo.japanese.model.items.Nan;
import com.codenjoy.dojo.japanese.model.items.Number;
import com.codenjoy.dojo.japanese.model.items.Pixel;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class NumbersBuilder {

    private Level level;

    private List<Pixels> pixelsRows;
    private List<Pixels> pixelsCols;
    private List<Numbers> numbersRows;
    private List<Numbers> numbersCols;
    private int max;

    public NumbersBuilder(Level level) {
        this.level = level;
    }

    static class Numbers {
        int pos;
        LinkedList<Integer> line;
        int index;

        public Numbers(int pos) {
            this.pos = pos;
            this.line = new LinkedList<>();
            index = 0;
        }

        protected void addNext() {
            if (line.isEmpty() || last() != 0) {
                line.add(0);
            }
            index = line.size() - 1;
        }

        private Integer last() {
            return line.get(line.size() - 1);
        }

        protected void increaseCurrent() {
            line.set(index, line.get(index) + 1);
        }

        protected void removeLastEmpty() {
            if (last() == 0) {
                line.removeLast();
            }
        }

        public void fill(int max, boolean before) {
            range(0, max - line.size()).forEach(i -> {
                if (before) {
                    line.addFirst(0);
                } else {
                    line.addLast(0);
                }
            });
        }
    }

    static class Pixels {
        int pos;
        List<Pixel> line;

        public Pixels(Map.Entry<Integer, List<Pixel>> entry) {
            pos = entry.getKey();
            line = entry.getValue();
        }
    }

    public void process() {
        // получаем строчки/рядки пикселей
        pixelsRows = lines(Pixel::getY);
        pixelsCols = lines(Pixel::getX);

        // получаем строчки/рядки цифер
        numbersRows = numbers(pixelsRows);
        numbersCols = numbers(pixelsCols);

        // определяем максимальную длинну циферок
        max = Math.max(maxLength(numbersRows), maxLength(numbersCols));

        // дополняем нулями, с которых потом нарисуем nan'ы
        fillZero(numbersRows, max, true);
        fillZero(numbersCols, max, false);

        // меняем размер поля и двигаем все пиксели так, чтобы поместились циферки
        level.size(level.size() + max);
        level.pixels().forEach(pixel -> pixel.moveDelta(pt(max, 0)));

        // закрашиваем квадратик пустоты слева сверху
        range(0, max).forEach(x ->
            range(0, max).forEach(y ->
                    level.nans().add(new Nan(pt(x, invert(y))))));

        // прописываем цифры и nan'ы в соостветствующих местах на поле
        // TODO чувствуешь силу? Попробуй реши проще
        generate(numbersRows, (x, y, len) -> pt(x + max - len, y), max);
        generate(numbersCols, (x, y, len) -> pt(y + max, x + level.size() - max), max);
    }

    private void fillZero(List<Numbers> numbersRows, int max, boolean before) {
        numbersRows.forEach(numbers -> numbers.fill(max, before));
    }

    private int invert(int pos) {
        return level.size() - 1 - pos;
    }

    @FunctionalInterface
    public interface TriFunction<T, U, V, R> {

        R apply(T t, U u, V v);
    }

    private void generate(List<Numbers> list, TriFunction<Integer, Integer, Integer, Point> pt, int max) {
        list.forEach(numbers -> {
            List<Integer> line = numbers.line;
            Integer y = numbers.pos;

            range(0, line.size()).forEach(x ->
                    add(pt.apply(x, y, line.size()), line.get(x)));
        });
    }

    private void add(Point pt, int number) {
        if (number == 0) {
            level.nans().add(new Nan(pt));
        } else {
            level.numbers().add(new Number(pt, number));
        }
    }

    private Integer maxLength(List<Numbers> numbers) {
        return numbers.stream()
                    .map(it -> it.line.size())
                    .max(Integer::compareTo)
                    .get();
    }

    private List<Numbers> numbers(List<Pixels> pixels) {
        return pixels.stream()
                    .map(this::calculate)
                    .collect(toList());
    }

    private List<Pixels> lines(Function<Pixel, Integer> grouping) {
        return level.pixels().stream()
                .sorted()
                .collect(groupingBy(grouping))
                .entrySet().stream()
                .map(Pixels::new)
                .collect(toList());
    }

    private Numbers calculate(Pixels pixels) {
        return new Numbers(pixels.pos){{
            this.addNext();
            for (Pixel pixel : pixels.line) {
                switch (pixel.color()) {
                    case BLACK : this.increaseCurrent(); break;
                    case WHITE : this.addNext(); break;
                }
            }
            this.removeLastEmpty();
        }};
    }
}
