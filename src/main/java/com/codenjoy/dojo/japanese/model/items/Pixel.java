package com.codenjoy.dojo.japanese.model.items;

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


import com.codenjoy.dojo.games.japanese.Element;
import com.codenjoy.dojo.japanese.model.Player;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.printer.state.State;

public class Pixel extends PointImpl implements State<Element, Player> {

    private Color color;

    public Pixel(Point point, Color color) {
        super(point);
        this.color = color;
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        switch (color) {
            case BLACK: return Element.BLACK;
            case WHITE: return Element.WHITE;
            default: return Element.UNSET;
        }
    }

    public Color color() {
        return color;
    }

    @Override
    public String toString() {
        return String.format("%s=%s", super.toString(), color);
    }

    public boolean isSet() {
        return color != Color.UNSET;
    }
}
