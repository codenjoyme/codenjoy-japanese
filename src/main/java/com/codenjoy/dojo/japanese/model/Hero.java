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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.joystick.NoDirectionJoystick;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import static com.codenjoy.dojo.games.japanese.Element.*;

public class Hero extends PlayerHero<Field> implements NoDirectionJoystick {

    private boolean alive;
    private boolean win;
    private Point point;
    private Color color;

    public Hero() {
        super();
        alive = true;
    }

    @Override
    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void act(int... p) {
        if (!alive) return;

        if (p.length != 3) return;

        Point point = pt(p[0], p[1]);

        if (point.isOutOf(field.size())) return;

        Point offset = field.offset();
        if (point.getX() < offset.getX()
            || point.getY() > offset.getY()) return;

        int color = p[2];

        if (color != BLACK.code()
            && color != WHITE.code()
            && color != UNSET.code()) return;

        this.point = point;
        this.color = Color.get(color);
    }

    @Override
    public void tick() {
        if (!alive) return;
        if (point == null) return;

        field.setPixel(point, color);

        point = null;
        color = Color.UNSET;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    public void gameOver(boolean win) {
        alive = false;
        this.win = win;
    }

    public boolean isWin() {
        return win;
    }
}
