package com.codenjoy.dojo.japanese.model.portable;

import com.codenjoy.dojo.japanese.model.items.Number;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

class Numbers {

    private int pos;
    private LinkedList<Integer> line;

    public Numbers(Map.Entry<Integer, List<Number>> entry) {
        pos = entry.getKey();
        line = new LinkedList<>(entry.getValue().stream()
                .map(Number::number)
                .collect(toList()));
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

    public int get(int i) {
        return line.get(i);
    }

    public void reverse() {
        Collections.reverse(line);
    }
}
