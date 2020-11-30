package com.codenjoy.dojo.japanese.model.level;

import com.codenjoy.dojo.japanese.model.items.Color;
import com.codenjoy.dojo.japanese.model.items.Nan;
import com.codenjoy.dojo.japanese.model.items.Number;
import com.codenjoy.dojo.japanese.model.items.Pixel;
import com.codenjoy.dojo.services.Point;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

public class NumbersBuilder {

    private LevelImpl level;

    private Map<Integer, List<Pixel>> pixelsRows;
    private Map<Integer, List<Pixel>> pixelsCols;
    private Map<Integer, List<Integer>> numbersRows;
    private Map<Integer, List<Integer>> numbersCols;

    public NumbersBuilder(LevelImpl level) {
        this.level = level;
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
        generate(numbersRows, (x, y) -> pt(x, y), max);
        generate(numbersCols, (x, y) -> pt(invert(y), invert(x)), max);
    }

    private int invert(int pos) {
        return level.size() - 1 - pos;
    }

    private void generate(Map<Integer, List<Integer>> map, BiFunction<Integer, Integer, Point> pt, int max) {
        map.entrySet().forEach(entry -> {
            List<Integer> numbers = entry.getValue();
            Integer y = entry.getKey();

            int end = max - numbers.size();

            range(0, end).forEach(x ->
                    addNan(pt.apply(x, y)));

            range(end, max).forEach(x ->
                    addNumber(pt.apply(x, y), numbers.get(x - end)));
        });
    }

    private void addNumber(Point pt, int number) {
        level.numbers().add(new Number(pt, number));
    }

    private void addNan(Point pt) {
        level.nans().add(new Nan(pt));
    }

    private Integer maxLength(Map<Integer, List<Integer>> numbers) {
        return numbers.entrySet().stream()
                    .map(entry -> entry.getValue().size())
                    .max(Integer::compareTo)
                    .get();
    }

    private Map<Integer, List<Integer>> numbers(Map<Integer, List<Pixel>> pixels) {
        return pixels.entrySet().stream()
                    .map(this::calculateNumbers)
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<Integer, List<Pixel>> lines(Function<Pixel, Integer> grouping) {
        return level.pixels().stream()
                .sorted()
                .collect(groupingBy(grouping));
    }

    private Map.Entry<Integer, List<Integer>> calculateNumbers(Map.Entry<Integer, List<Pixel>> entry) {
        List<Integer> result = new LinkedList<>();

        int count = 0;
        List<Pixel> pixels = entry.getValue();
        for (int index = 0; index < pixels.size(); index++) {
            Pixel pixel = pixels.get(index);
            if (pixel.color() == Color.BLACK) {
                count++;
            } else if (pixel.color() == Color.WHITE) {
                if (count != 0) {
                    result.add(count);
                }
                count = 0;
            }
        }
        if (count != 0) {
            result.add(count);
        }

        return new AbstractMap.SimpleEntry(entry.getKey(), result);
    }
}
