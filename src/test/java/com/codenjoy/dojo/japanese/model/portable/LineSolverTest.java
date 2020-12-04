package com.codenjoy.dojo.japanese.model.portable;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LineSolverTest {

    LineSolver solver = new LineSolver();

    @Test
    public void test() {
        assertCombinations("[B-100%, B-100%, B-100%, W-0%, B-100%]",
                "UUUUU", 3, 1);

        assertCombinations("[B-100%, B-100%]",
                "UU", 2);

        assertCombinations("[U-33%, U-33%, U-33%]",
                "UUU", 1);

        assertCombinations("[U-50%, B-100%, U-50%]",
                "UUU", 2);

        assertCombinations("[U-33%, U-67%, U-67%, U-33%]",
                "UUUU", 2);

        assertCombinations("[U-50%, B-100%, B-100%, U-50%]",
                "UUUU", 3);

        assertCombinations("[U-67%, B-100%, B-100%, U-33%, U-33%, U-67%]",
                "UUUUUU", 3, 1);
    }

    private void assertCombinations(String expected, String dots, Integer... input) {
        int[] numbers = new LinkedList<>(Arrays.asList(input)){{
            add(0, 0);
        }}.stream().mapToInt(i -> i).toArray();

        solver.calculate(numbers, parseDots(dots));
        assertEquals(expected, formatResult());
    }

    private Dot[] parseDots(String string) {
        Dot[] result = new Dot[string.length() + 1];
        for (int index = 0; index < string.length(); index++) {
            result[index + 1] = Dot.get(string.charAt(index));
        }
        return result;
    }

    private String formatResult() {
        List<String> data = new LinkedList<>();
        for (int i = 1; i <= solver.length(); i++) {
            data.add(String.format("%s-%s%%",
                    solver.dots(i).ch(),
                    Math.round(solver.probability(i) * 100)));
        }
        return data.toString();
    }

}