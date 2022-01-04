package com.codenjoy.dojo.japanese.model.portable;

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
