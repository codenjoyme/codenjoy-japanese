package com.codenjoy.dojo.japanese.model.portable;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

class Assumption {

    private Pt at; // координата, которую пытаемся подобрать
    private Dot color; // цвет в котором делали проверку подбором
    private boolean error; // была ли ошибка в процессе подбора
    private boolean errorOnBlack; // была ли ошибка в подборе BLACK точки
    private boolean errorOnWhite; // была ли ошибка в подборе WHITE точки

    public Assumption() {
        stop();
    }

    public boolean inProgress() {
        return color != Dot.UNSET;
    }

    public void stop() {
        color = Dot.UNSET;
        error = false;
        errorOnBlack = false;
        errorOnWhite = false;
    }

    public void start(Pt at, Dot color) {
        this.at = at;
        this.color = color;
    }

    public boolean isBlack() {
        return color == Dot.BLACK;
    }

    public boolean isWhite() {
        return color == Dot.WHITE;
    }

    public Dot color() {
        return color;
    }

    public void error() {
        error = true;
        if (inProgress()) {
            if (isBlack()) {
                errorOnBlack = true;
            } else {
                errorOnWhite = true;
            }
        }
    }

    public boolean wasError() {
        return error;
    }

    public boolean errorOnBoth() {
        return errorOnBlack && errorOnWhite;
    }

    public boolean noErrors() {
        return !errorOnBlack && !errorOnWhite;
    }

    public Dot errorOn() {
        if (errorOnBlack) {
            return Dot.BLACK;
        }
        if (errorOnWhite) {
            return Dot.WHITE;
        }
        return Dot.UNSET;
    }

    public Pt at() {
        return at;
    }
}
