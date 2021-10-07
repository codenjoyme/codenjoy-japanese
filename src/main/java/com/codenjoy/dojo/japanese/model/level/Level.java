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


import com.codenjoy.dojo.games.japanese.Element;
import com.codenjoy.dojo.japanese.model.items.Color;
import com.codenjoy.dojo.japanese.model.items.Nan;
import com.codenjoy.dojo.japanese.model.items.Number;
import com.codenjoy.dojo.japanese.model.items.Pixel;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.field.AbstractLevel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

import static com.codenjoy.dojo.games.japanese.Element.*;

public class Level extends AbstractLevel {

    private List<Pixel> pixels;
    private List<Number> numbers;
    private List<Nan> nans;

    public Level(String map) {
        super(map);

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

    public void size(int size) {
        this.size = size;
    }

    private List<Number> parseNumbers() {
        return find((pt, el) -> new Number(pt, el.code()),
                Element.getNumbers());
    }


    private List<Nan> parseNans() {
        return find(Nan::new, NAN);
    }

    private List<Pixel> parsePixels() {
        return find(new LinkedHashMap<>(){{
                    put(BLACK, pt -> new Pixel(pt, Color.BLACK));
                    put(WHITE, pt -> new Pixel(pt, Color.WHITE));
                    put(UNSET, pt -> new Pixel(pt, Color.UNSET));
                }});
    }

    public List<Number> numbers() {
        return numbers;
    }

    public List<Nan> nans() {
        return nans;
    }

    public List<Pixel> pixels() {
        return pixels;
    }

    public String map() {
        return map;
    }

    @Override
    protected void addAll(Consumer<Iterable<? extends Point>> processor) {
        processor.accept(numbers());
        processor.accept(pixels());
        processor.accept(nans());
    }
}
