package com.codenjoy.dojo.japanese.model.portable;

import org.approvaltests.legacycode.LegacyApprovals;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LineSolverTest {

    LineSolver solver = new LineSolver();

    @Test
    public void test1() {
        String[] dots = new String[]{
                "U",
                "UU",
                "UUUU",
                "UUUUU",
                "UUUUUU",
                "UUUUUUU",
                "UUUUUUUU",
                "UUUUUUUUU",
                "UUUUUUUUUU",
                "UUUUUUUUUUU",
                "UUUUUUUUUUUU",
                "UUUUUUUUUUUUU",
                "UUUUUUUUUUUUUU",
                "UUUUUUUUUUUUUUU",
                "UUUUUUUUUUUUUUUU",
                "UUUUUUUUUUUUUUUUU",
                "UUUUUUUUUUUUUUUUUU",
                "UUUUUUUUUUUUUUUUUUU",
                "UUUUUUUUUUUUUUUUUUUU",
                "UUUUUUUUUUUUUUUUUUUUU",
                "UUUUUUUUUUUUUUUUUUUUUU",
                "UUUUUUUUUUUUUUUUUUUUUUU",
                "UUUUUUUUUUUUUUUUUUUUUUUU",
                "UUUUUUUUUUUUUUUUUUUUUUUUU",
                "UUUUUUUUUUUUUUUUUUUUUUUUUU",
                "UUUUUUUUUUUUUUUUUUUUUUUUUUU",
                "UUUUUUUUUUUUUUUUUUUUUUUUUUUU",
                "B",
                "UB",
                "UBUU",
                "UUBUU",
                "UUBUBU",
                "UBUBUBU",
                "UUBUUBUU",
                "UUBUUBUBU",
                "UUBUUBBBUU",
                "UUBUBUUBUUU",
                "UUBBBUUBUUUU",
                "BUUBUUUUUUUUU",
                "BUUUUBUUUUUUUU",
                "UUUUUBUUUUUUUUU",
                "UUUUUUUUBUUUUUUU",
                "UUUUUUUUUUUUUUUBB",
                "UUUBUUUUUBUUUUBUUU",
                "UUBUUUBUUUUUBUUUBUB",
                "UUUUUUUUUUUUBUUUUUUU",
                "UUUUUBUUBUUUUUUUUUUUU",
                "UUUUUBUUBUUUUUUUBUUUUU",
                "UUUUUBUUBUUUUBUUBUUUUUU",
                "UUUUUBUUBUUUUBUUUUBBBUUU",
                "UUUUUBUUBUUUUUUUBUBUUUUUU",
                "UUUUUBUUBUUUUUUUBBUBUUUUUU",
                "UUUUUBUUBUUUUUUUBBUBUUUUBUU",
                "W",
                "UW",
                "UWUU",
                "UUWUU",
                "UUWUWU",
                "UWUWUWU",
                "UUWUUWUU",
                "UUWUUWUWU",
                "UUWUUWWWUU",
                "UUWUWUUWUUU",
                "UUWWWUUWUUUU",
                "WUUWUUUUUUUUU",
                "WUUUUWUUUUUUUU",
                "UUUUUWUUUUUUUUU",
                "UUUUUUUUWUUUUUUU",
                "UUUUUUUUUUUUUUUWW",
                "UUUWUUUUUWUUUUWUUU",
                "UUWUUUWUUUUUWUUUWUW",
                "UUUUUUUUUUUUWUUUUUUU",
                "UUUUUWUUWUUUUUUUUUUUU",
                "UUUUUWUUWUUUWUUUUWWUUU",
                "UUUUUWUUWUUUWUUUUWWUUUU",
                "UUUUUWUUWUUUWUUUUWWUUUUU",
                "UUUUUWUUWUUUWUUWUWWUUUUUU",
                "UUUUUWUUWWUUWUUWUWWUWWWWUU",
                "UUUUUWUUWUUUWUUWUWWUUUWWUU",
                "W",
                "BW",
                "UWUU",
                "UBWUU",
                "UUWBWU",
                "UWUWUWU",
                "UUWBUWUU",
                "UUWBUWUWU",
                "UUWUBWWWUU",
                "UUWUWBUWUUU",
                "UUWWWUUWUUUU",
                "WUUWUBUUUUUUU",
                "WUUBUWUUUUUUUU",
                "UUUBUWUBUUWWWUU",
                "UUUUUUUUWBWBWUUU",
                "UUUUUBUUUBUUUUUWW",
                "UUUWUBUUUWUUUUWUUU",
                "UUWUUUWWWWBUWUUUWUW",
                "UUUUUWUWUUBUWUUUUUUU",
                "UUUBWWUUWUUUUUUUUUUUU",
                "UUUBWWUUWUUUUBUUUUUUUU",
                "UUUBWWUUWUUUUUUBUUUBUUU",
                "UUUBWWUUWUUUUBUUUUUUBUUU",
                "UUUBWWUUWUUUUBUUUUUUBUUUB",
                "UUUBWWUUWUUUUBUUUUUUBUUUBB",
                "UUUBWWUUWUUUUBUUUUUUBUUUBBB",
        };
        String[] numbers = new String[]{
                "{1}",
                "{2}",
                "{1,2}",
                "{2,1}",
                "{3}",
                "{1,2,3}",
                "{1,3,3}",
                "{1,3,3,9}",
                "{1,5,3}",
                "{1,5,3,1}",
                "{1,5,3,2,2}",
                "{5,7}",
                "{9}",
                "{9,1,1,1,1}",
                "{1,1,1,1,9}",
                "{2,2,2}",
                "{3,2,1}",
                "{3,4,2,1}",
                "{1,1,1,1}",
                "{1,3,2,1,1}",
                "{1,3,1,1}",
                "{1,1,1}",
                "{1,1,1,1,1,1}",
                "{1,1}",
                "{1,4}",
                "{4}",
                "{2,3}",
        };
        LegacyApprovals.LockDown(this, "getCombinations", numbers, dots);
    }

    public String getCombinations(String inputNumbers, String dots) {
        int[] numbers = Arrays.stream(("0," + inputNumbers.replaceAll("[\\{\\}]", "")).split(","))
                .mapToInt(i -> Integer.valueOf(i))
                .toArray();

        try {
            solver.calculate(numbers, parseDots(dots));
            return formatResult();
        } catch (Exception e) {
            return e.getClass().getSimpleName();
        }
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
            data.add(String.format("%s:%s%%",
                    solver.dots(i).ch(),
                    Math.round(solver.probability(i) * 100)));
        }
        return data.toString();
    }

}