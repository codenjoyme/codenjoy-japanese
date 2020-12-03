package com.codenjoy.dojo.japanese.model.portable;

class Assumption {

    private TPoint at; // координата, которую пытаемся подобрать
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

    public void start(TPoint at, Dot color) {
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

    public TPoint at() {
        return at;
    }
}
