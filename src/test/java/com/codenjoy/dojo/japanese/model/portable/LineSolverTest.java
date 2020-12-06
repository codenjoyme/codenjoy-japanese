package com.codenjoy.dojo.japanese.model.portable;

import org.approvaltests.legacycode.LegacyApprovals;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;

public class LineSolverTest {

    @Test
    public void test_1() {
        assertEquals("false:[W:-100%, W:-100%, B:-100%, W:-100%, W:-100%]\n" +
                        "\t-[1,2]:*.**.\n" +
                        "\t-[1,2]:*..**\n" +
                        "\t-[1,2]:.*.**",
                getCombinations("1,2", "WWBWW"));
    }

    @Test
    public void test_2() {
        assertEquals("false:[W:-100%, W:-100%, B:-100%, W:-100%, W:-100%]\n" +
                        "\t-[2,1]:**.*.\n" +
                        "\t-[2,1]:**..*\n" +
                        "\t-[2,1]:.**.*",
                getCombinations("2,1", "WWBWW"));
    }

    @Test
    public void test_3() {
        assertEquals("true:[U:67%, B:100%, U:33%, U:33%, U:67%]\n" +
                        "\t+[2,1]:**.*.\n" +
                        "\t+[2,1]:**..*\n" +
                        "\t+[2,1]:.**.*",
                getCombinations("2,1", "UUUUU"));
    }

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
                "UUUUUWUUWUUUWUUWUWWUUUWWUU",
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
                "B",
                "WB",
                "WBWW",
                "WWBWW",
                "WWBWBW",
                "WBWBWBW",
                "WWBWWBWW",
                "WWBWWBWBW",
                "WWBWWBBBWW",
                "WWBWBWWBWWW",
                "WWBBBWWBWWWW",
                "BWWBWWWWWWWWW",
                "BWWWWBWWWWWWWW",
                "WWWWWBWWWWWWWWW",
                "WWWWWWWWBWWWWWWW",
                "WWWWWWWWWWWWWWWBB",
                "WWWBWWWWWBWWWWBWWW",
                "WWBWWWBWWWWWBWWWBWB",
                "WWWWWWWWWWWWBWWWWWWW",
                "WWWWWBWWBWWWWWWWWWWWW",
                "WWWWWBWWBWWWBWWWWBBWWW",
                "WWWWWBWWBWWWBWWWWBBWWWW",
                "WWWWWBWWBWWWBWWWWBBWWWWW",
                "WWWWWBWWBWWWBWWBWBBWWWWWW",
                "WWWWWBWWBBWWBWWBWBBWBBBBWW",
                "B",
                "WB",
                "WBWW",
                "WUUUW",
                "WWBUBW",
                "WBWBUBW",
                "WUBWUUWW",
                "WWUWWBUUW",
                "WWBUWBBUWW",
                "WWBWUWUBWWW",
                "WWBBBUWUWWUW",
                "BUWBWWUWUUWWW",
                "BWUWWBWUWUUUWW",
                "WWWUWBWWUWUUWWW",
                "WWWWUWWWBUWWWWWW",
                "WWWWWUWWWWUUUUWBB",
                "WWWUUUUWWBWUWWBWWW",
                "WWBWWWBUWWWWUUWUBWB",
                "WUWWWUUWUWUUBUWWUWWW",
                "WWUWWBWWBUWWWWUWWUWWW",
                "WWWUWBUWBWUWBWWUWBUWWW",
                "WWWWUBWUBWWUUWWWUBBUWWW",
                "WWWWWUWWUUWWBUWWWUBWUWWW",
                "WWWWWBUWBWWWBWUBWBUWWUWWW",
                "WWWWWBWWBBWWBWWBWBBUBUUUUU",
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
                "{}",
                "{0}",
                "{1}",
                "{2}",
                "{1,2}",
                "{2,1}",
                "{3}",
                "{3,0}",
                "{0,3}",
                "{1,2,3}",
                "{1,3,3}",
                "{1,3,3,9}",
                "{1,5,3}",
                "{1,0,5,3}",
                "{1,5,3,1}",
                "{1,5,3,2,2}",
                "{1,5,3,2,2,0,0}",
                "{5,7}",
                "{9}",
                "{9,1,1,1,1}",
                "{1,1,1,1,9}",
                "{2,2,2}",
                "{3,2,1}",
                "{3,4,2,1}",
                "{1,1,1,1}",
                "{10,10}",
                "{10,1,10}",
                "{10,10,10}",
                "{20}",
                "{1,3,2,1,1}",
                "{1,3,1,1}",
                "{1,1,1}",
                "{1,1,1,1,1,1}",
                "{1,1}",
                "{1,4}",
                "{4}",
                "{2,3}",
        };
        LegacyApprovals.LockDown(this, "getCombinationsWithoutHistory", numbers, dots);
    }

    public String getCombinationsWithoutHistory(String inputNumbers, String dots) {
        return getCombinations(inputNumbers, dots, false);
    }

    public String getCombinations(String inputNumbers, String dots) {
        return getCombinations(inputNumbers, dots, true);
    }

    public String getCombinations(String inputNumbers, String dots, boolean history) {
        LineSolver solver = new LineSolver(){{
            enableHistory = history;
        }};

        int[] numbers = Arrays.stream(("0," + inputNumbers.replaceAll("[\\{\\}]", "")).split(","))
                .mapToInt(i -> Integer.valueOf(i))
                .toArray();

        try {
            return formatResult(solver, solver.calculate(numbers, parseDots(dots)));
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

    private String formatResult(LineSolver solver, boolean calculateResult) {
        List<String> data = new LinkedList<>();
        for (int i = 1; i <= solver.length(); i++) {
            data.add(String.format("%s:%s%%",
                    solver.dots(i).ch(),
                    Math.round(solver.probability(i) * 100)));
        }

        String historyResult = solver.history().stream()
                .map(line -> "\n\t" + line)
                .collect(joining(""));

        return String.format("%s:%s%s",
                calculateResult,
                data.toString(),
                historyResult);
    }

}