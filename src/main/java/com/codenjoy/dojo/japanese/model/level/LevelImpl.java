package com.codenjoy.dojo.japanese.model.level;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.japanese.model.Elements;
import com.codenjoy.dojo.japanese.model.items.Color;
import com.codenjoy.dojo.japanese.model.items.Nan;
import com.codenjoy.dojo.japanese.model.items.Number;
import com.codenjoy.dojo.japanese.model.items.Pixel;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.japanese.model.Elements.*;

public class LevelImpl implements Level, BoardReader {

    private LengthToXY xy;
    private String map;

    private int size;
    private List<Pixel> pixels;
    private List<Number> numbers;
    private List<Nan> nans;

    public LevelImpl(String map) {
        this.map = LevelUtils.clear(map);
        size = (int) Math.sqrt(map.length());
        xy = new LengthToXY(size);

        pixels = parsePixels();
        numbers = parseNumbers();
        nans = parseNans();

        if (!pixelsExists()) {
            // TODO рисунка нет - надо решить паззл и нарисовать
            return;
        }

        // нет цифер - надо сгенерить
        if (!numbersExists()) {
            new NumbersBuilder(this).process();
            return;
        }

        // TODO только проверяем соответствие циферок и рисунка
    }

    private boolean numbersExists() {
        return !(nans.isEmpty() && numbers.isEmpty());
    }

    private boolean pixelsExists() {
        return pixels.stream().allMatch(Pixel::isSet);
    }

    @Override
    public int size() {
        return size;
    }

    public void size(int size) {
        this.size = size;
    }

    private List<Number> parseNumbers() {
        return LevelUtils.getObjects(xy, map,
                (pt, el) -> new Number(pt, el.code()),
                Elements.getNumbers());
    }


    private List<Nan> parseNans() {
        return LevelUtils.getObjects(xy, map,
                Nan::new,
                NAN);
    }

    private List<Pixel> parsePixels() {
        return LevelUtils.getObjects(xy, map,
                new HashMap<>(){{
                    put(BLACK, pt -> new Pixel(pt, Color.BLACK));
                    put(WHITE, pt -> new Pixel(pt, Color.WHITE));
                    put(UNSET, pt -> new Pixel(pt, Color.UNSET));
                }});
    }

    @Override
    public List<Number> numbers() {
        return numbers;
    }

    @Override
    public List<Nan> nans() {
        return nans;
    }

    @Override
    public List<Pixel> pixels() {
        return pixels;
    }

    @Override
    public String map() {
        return map;
    }

    // только для целей тестирования
    @Override
    public Iterable<? extends Point> elements() {
        return new LinkedList<>() {{
            addAll(LevelImpl.this.numbers());
            addAll(LevelImpl.this.nans());
            addAll(LevelImpl.this.pixels());
        }};
    }
}
