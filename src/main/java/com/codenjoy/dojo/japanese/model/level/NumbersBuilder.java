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
import java.util.stream.IntStream;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

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
        pixelsRows = getLines(Pixel::getY);
        pixelsCols = getLines(Pixel::getX);
        numbersRows = getNumbers(pixelsRows);
        numbersCols = getNumbers(pixelsCols);
        int max = Math.max(maxLength(numbersRows), maxLength(numbersCols));

        generateResult(numbersRows, (x, y) -> pt(x, y), max);
        generateResult(numbersCols, (x, y) -> pt(y, x), max);
    }

    private void generateResult(Map<Integer, List<Integer>> numbers, BiFunction<Integer, Integer, Point> pt, int max) {
        numbers.entrySet().forEach(entry -> {
            List<Integer> line = entry.getValue();
            int end = max - line.size();
            IntStream.range(0, end)
                    .forEach(pos -> level.nans().add(new Nan(pt.apply(pos, entry.getKey()))));
            IntStream.range(end, max)
                    .forEach(pos -> level.numbers().add(new Number(pt.apply(pos, entry.getKey()), line.get(pos - end))));
        });
    }

    private Integer maxLength(Map<Integer, List<Integer>> numbersLine) {
        return numbersLine.entrySet().stream()
                    .map(entry -> entry.getValue().size())
                    .max(Integer::compareTo)
                    .get();
    }

    private Map<Integer, List<Integer>> getNumbers(Map<Integer, List<Pixel>> lines) {
        return lines.entrySet().stream()
                    .map(this::calculateNumbers)
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<Integer, List<Pixel>> getLines(Function<Pixel, Integer> grouping) {
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
