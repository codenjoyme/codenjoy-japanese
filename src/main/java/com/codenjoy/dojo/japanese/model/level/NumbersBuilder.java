package com.codenjoy.dojo.japanese.model.level;

import com.codenjoy.dojo.japanese.model.items.Nan;
import com.codenjoy.dojo.japanese.model.items.Number;
import com.codenjoy.dojo.japanese.model.items.Pixel;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class NumbersBuilder {

    private LevelImpl level;

    private List<Pixels> pixelsRows;
    private List<Pixels> pixelsCols;
    private List<Numbers> numbersRows;
    private List<Numbers> numbersCols;

    public NumbersBuilder(LevelImpl level) {
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
        int max = Math.max(maxLength(numbersRows), maxLength(numbersCols));

        // меняем размер поля и двигаем все пиксели так, чтобы поместились циферки
        level.size(level.size() + max);
        level.pixels().forEach(pixel -> pixel.change(pt(max, 0)));

        // закрашиваем квадратик пустоты слева сверху
        range(0, max).forEach(x ->
            range(0, max).forEach(y ->
                    level.nans().add(new Nan(pt(x, invert(y))))));

        // прописываем цифры и nan'ы в соостветствующих местах на поле
        // TODO чувствуешь силу? Попробуй реши проще
        generate(numbersRows, (x, y, len) -> pt(x, y), max, 0);
        generate(numbersCols, (x, y, len) -> pt(y + max, invert(max - x) - (max - len - 1)), max, max);
    }

    private int invert(int pos) {
        return level.size() - 1 - pos;
    }

    @FunctionalInterface
    public interface TriFunction<T, U, V, R> {

        R apply(T t, U u, V v);
    }

    private void generate(List<Numbers> list, TriFunction<Integer, Integer, Integer, Point> pt, int max, int dx) {
        list.forEach(numbers -> {
            List<Integer> line = numbers.line;
            Integer y = numbers.pos;

            int end = max - line.size();

            range(0, end).forEach(x ->
                    addNan(pt.apply(x + dx, y, line.size())));

            range(end, max).forEach(x ->
                    addNumber(pt.apply(x, y, line.size()), line.get(x - end)));
        });
    }

    private void addNumber(Point pt, int number) {
        level.numbers().add(new Number(pt, number));
    }

    private void addNan(Point pt) {
        level.nans().add(new Nan(pt));
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
