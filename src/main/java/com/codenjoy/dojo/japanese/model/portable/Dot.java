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

import java.util.Arrays;

public enum Dot {

    UNSET(0), BLACK(1), WHITE(2), ASSUMPTION(3);

    private int code;

    Dot(int code) {
        this.code = code;
    }

    public static Dot get(char ch) {
        return Arrays.stream(Dot.values())
                .filter(dot -> dot.name().charAt(0) == ch)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Верный символ для Dot"));
    }

    public static Dot get(String data) {
        return Arrays.stream(Dot.values())
                .filter(dot -> dot.code == Integer.valueOf(data))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Верный код для Dot"));
    }

    public boolean isBlack() {
        return this == BLACK;
    }

    public boolean isWhite() {
        return this == WHITE;
    }

    public Dot invert() {
        if (this == BLACK) {
            return WHITE;
        } else if (this == WHITE) {
            return BLACK;
        } else {
            return UNSET;
        }
    }

    public int code() {
        return code;
    }

    public char ch() {
        return name().charAt(0);
    }
}
