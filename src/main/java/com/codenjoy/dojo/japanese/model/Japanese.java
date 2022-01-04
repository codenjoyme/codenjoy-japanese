package com.codenjoy.dojo.japanese.model;

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


import com.codenjoy.dojo.japanese.model.items.Color;
import com.codenjoy.dojo.japanese.model.items.Nan;
import com.codenjoy.dojo.japanese.model.items.Number;
import com.codenjoy.dojo.japanese.model.items.Pixel;
import com.codenjoy.dojo.japanese.model.level.Level;
import com.codenjoy.dojo.japanese.services.Event;
import com.codenjoy.dojo.japanese.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Japanese implements Field {

    private List<Pixel> pixels;
    private List<Pixel> actPixels;
    private List<Pixel> winPixels;

    private List<Nan> nan;
    private List<Number> numbers;
    private Point offset;

    private Player player;

    private final int size;
    private Dice dice;
    private int levelNumber;

    private GameSettings settings;

    public Japanese(Level level, Dice dice, int levelNumber, GameSettings settings) {
        this.dice = dice;
        this.levelNumber = levelNumber;
        size = level.size();

        pixels = level.pixels();
        this.settings = settings;
        resetActs();

        offset = pt(getOffsetX(), getOffsetY());

        numbers = level.numbers();
        nan = level.nans();
    }

    private void resetActs() {
        actPixels = new LinkedList<>();
        winPixels = new LinkedList<>();
    }

    private int getOffsetX() {
        int x;
        int y = 0;
        for (x = 0; x < size; x++) {
            if (pixels.indexOf(pt(x, y)) != -1) {
                break;
            }
        }
        return x;
    }

    private int getOffsetY() {
        int x = size - 1;
        int y;
        for (y = 0; y < size; y++) {
            if (pixels.indexOf(pt(x, y)) == -1) {
                break;
            }
        }
        return y - 1;
    }

    @Override
    public void tick() {
        Hero hero = player.getHero();

        hero.tick();

        checkSolved(hero);
    }

    @Override
    public int level() {
        return levelNumber;
    }

    private void checkSolved(Hero hero) {
        if (isSolved()) {
            boolean win = isGotItRight();
            if (win) {
                player.event(Event.WIN);
            } else {
                player.event(Event.LOSE);
            }
            hero.gameOver(win);
        }
    }

    public boolean isGotItRight() {
        return winPixels.containsAll(pixels);
    }

    private boolean isSolved() {
        return actPixels.containsAll(pixels);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void setPixel(Point pt, Color color) {
        actPixels.removeIf(pixel -> pixel.equals(pt));
        Pixel actual = new Pixel(pt, color);
        actPixels.add(actual);

        int index = pixels.indexOf(actual);
        if (index == -1) {
            return;
        }

        Pixel expected = pixels.get(index);
        if (expected.color() != actual.color()) {
            player.event(Event.INVALID);
            return;
        }

        if (winPixels.indexOf(actual) != -1) {
            return;
        }

        winPixels.add(actual);
        player.event(Event.VALID);
    }

    public List<Number> getNumbers() {
        return numbers;
    }

    @Override
    public void newGame(Player player) {
        this.player = player;
        resetActs();
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        this.player = null;
        resetActs();
    }

    public List<Nan> getNans() {
        return nan;
    }

    public List<Pixel> getActPixels() {
        return actPixels;
    }

    @Override
    public Point offset() {
        return offset;
    }

    @Override
    public BoardReader<Player> reader() {
        return new BoardReader<>() {
            private int size = Japanese.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public void addAll(Player player, Consumer<Iterable<? extends Point>> processor) {
                processor.accept(getActPixels());
                processor.accept(getNans());
                processor.accept(getNumbers());
            }
        };
    }

    @Override
    public GameSettings settings() {
        return settings;
    }
}
