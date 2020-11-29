package com.codenjoy.dojo.japanese.model.items;

import com.codenjoy.dojo.japanese.model.Elements;

public enum Color {

    WHITE, BLACK, UNSET;

    public static Color get(int color) {
        if (color == Elements.WHITE.code()) {
            return WHITE;
        }

        if (color == Elements.BLACK.code()) {
            return BLACK;
        }

        return UNSET;
    }
}
