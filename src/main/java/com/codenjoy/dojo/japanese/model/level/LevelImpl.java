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
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.HashMap;
import java.util.List;

import static com.codenjoy.dojo.japanese.model.Elements.*;

public class LevelImpl implements Level {

    private final LengthToXY xy;

    private String map;

    public LevelImpl(String map) {
        this.map = map;
        xy = new LengthToXY(size());
    }

    @Override
    public int size() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Number> numbers() {
        return LevelUtils.getObjects(xy, map,
                (pt, el) -> new Number(pt, el.code()),
                Elements.getNumbers());
    }

    @Override
    public List<Nan> nans() {
        return LevelUtils.getObjects(xy, map,
                Nan::new,
                NAN);
    }

    @Override
    public List<Pixel> pixels() {
        return LevelUtils.getObjects(xy, map,
                new HashMap<>(){{
                    put(BLACK, pt -> new Pixel(pt, Color.BLACK));
                    put(WHITE, pt -> new Pixel(pt, Color.WHITE));
                    put(UNSET, pt -> new Pixel(pt, Color.UNSET));
                }});
    }

    @Override
    public String map() {
        return map;
    }

}
