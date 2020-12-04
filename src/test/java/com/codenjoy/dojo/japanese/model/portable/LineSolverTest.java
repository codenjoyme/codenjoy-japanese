package com.codenjoy.dojo.japanese.model.portable;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.japanese.model.portable.Dot.*;
import static org.junit.Assert.*;

public class LineSolverTest {

    LineSolver solver = new LineSolver();

    @Test
    public void test() {
        solver.calculate(new int[]{1, 2, 2}, new Dot[]{ UNSET, UNSET, UNSET, UNSET, UNSET, UNSET, UNSET });

        assertEquals("", formatResult());
    }

    private String formatResult() {
        List<String> data = new LinkedList<>();
        for (int i = 0; i < solver.length(); i++) {
            data.add(String.format("[%s%%=%s]", Math.round(solver.probability(i) * 100), solver.dots(i)));
        }
        return data.toString();
    }

}