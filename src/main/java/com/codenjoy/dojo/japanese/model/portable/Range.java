package com.codenjoy.dojo.japanese.model.portable;

import java.util.function.Consumer;
import java.util.function.Function;

public class Range {

    private int from;
    private int to;
    private int length;

    public int length() {
        return length;
    }

    public int from() {
        return from;
    }

    public int to() {
        return to;
    }

    public void iterate(Consumer<Integer> consumer) {
        for (int i = from; i <= to; i++) {
            consumer.accept(i);
        }
    }

    public void from(int input) {
        from = input;
        calcLength();
    }

    public void to(int input) {
        to = input;
        calcLength();
    }

    private void calcLength() {
        length = to - from + 1;
    }

    public boolean exists() {
        return from <= to;
    }
}
