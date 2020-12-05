package com.codenjoy.dojo.japanese.model.portable;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LineSolverTest {

    LineSolver solver = new LineSolver();

    @Test
    public void test1() {
        assertCombinations("{3,1}UUUUU->[B-100%, B-100%, B-100%, W-0%, B-100%]");
    }

    @Test
    public void test2() {
        assertCombinations("{2}UU->[B-100%, B-100%]");
    }

    @Test
    public void test3() {
        assertCombinations("{1}UUU->[U-33%, U-33%, U-33%]");
    }

    @Test
    public void test4() {
        assertCombinations("{2}UUU->[U-50%, B-100%, U-50%]");
    }

    @Test
    public void test5() {
        assertCombinations("{2}UUUU->[U-33%, U-67%, U-67%, U-33%]");
    }

    @Test
    public void test6() {
        assertCombinations("{3}UUUU->[U-50%, B-100%, B-100%, U-50%]");
    }

    @Test
    public void test7() {
        assertCombinations("{3,1}UUUUUU->[U-67%, B-100%, B-100%, U-33%, U-33%, U-67%]");
    }

    private void assertCombinations(String data) {
        String[] split = data.split("->");
        String left = split[0];
        String expected = split[1];

        String[] inputs = left.split("}");
        String numbers = inputs[0].replaceAll("\\{", "");
        String dots = inputs[1];

        assertCombinations(expected, dots, numbers);
    }

    private void assertCombinations(String expected, String dots, String inputNumbers) {
        int[] numbers = Arrays.stream(("0," + inputNumbers).split(","))
                .mapToInt(i -> Integer.valueOf(i))
                .toArray();

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