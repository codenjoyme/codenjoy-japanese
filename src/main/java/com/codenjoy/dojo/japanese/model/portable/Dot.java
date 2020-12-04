package com.codenjoy.dojo.japanese.model.portable;

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