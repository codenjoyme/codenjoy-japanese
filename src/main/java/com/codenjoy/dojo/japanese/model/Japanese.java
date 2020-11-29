package com.codenjoy.dojo.japanese.model;

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


import com.codenjoy.dojo.japanese.model.items.Color;
import com.codenjoy.dojo.japanese.model.items.Nan;
import com.codenjoy.dojo.japanese.model.items.Number;
import com.codenjoy.dojo.japanese.model.items.Pixel;
import com.codenjoy.dojo.japanese.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;

public class Japanese implements Field {

    private List<Pixel> pixels;
    private List<Nan> nan;
    private List<Number> numbers;

    private Player player;

    private final int size;
    private Dice dice;

    public Japanese(Level level, Dice dice) {
        this.dice = dice;
        pixels = level.getPixels();
        numbers = level.getNumbers();
        nan = level.getNan();
        size = level.getSize();
    }

    @Override
    public void tick() {
        Hero hero = player.getHero();

        hero.tick();

        if (!hero.isAlive()) {
            player.event(Events.LOOSE);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void setPixel(Point pt, Color color) {
        pixels.removeIf(pixel -> pixel.equals(pt));
        pixels.add(new Pixel(pt, color));
    }

    public List<Number> getNumbers() {
        return numbers;
    }

    @Override
    public void newGame(Player player) {
        this.player = player;
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        this.player = null;
    }

    public List<Nan> getNans() {
        return nan;
    }

    public List<Pixel> getPixels() {
        return pixels;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = Japanese.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<>(){{
                    addAll(Japanese.this.getPixels());
                    addAll(Japanese.this.getNans());
                    addAll(Japanese.this.getNumbers());
                }};
            }
        };
    }
}
