package com.codenjoy.dojo.japanese.model.portable;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
        // если предложен ряд, который не совпадет ни с одной комбинацией
        // probability везде будут -1
        // а solver ответит false
        assertEquals("false:[W:-100%, W:-100%, B:-100%, W:-100%, W:-100%]\n" +
                        "\t-[1,2]:*.**.\n" +
                        "\t-[1,2]:*..**\n" +
                        "\t-[1,2]:.*.**",
                getCombinations("1,2", "WWBWW"));
    }

    @Test
    public void test_2() {
        // если предложен ряд, который не совпадет ни с одной комбинацией
        // probability везде будут -1
        // а solver ответит false
        assertEquals("false:[W:-100%, W:-100%, B:-100%, W:-100%, W:-100%]\n" +
                        "\t-[2,1]:**.*.\n" +
                        "\t-[2,1]:**..*\n" +
                        "\t-[2,1]:.**.*",
                getCombinations("2,1", "WWBWW"));
    }

    @Test
    public void test_3() {
        // тут нормальное размещение рядов в неоткрытом диапазоне
        // а solver ответит true и даст валидные вероятности
        assertEquals("true:[U:67%, B:100%, U:33%, U:33%, U:67%]\n" +
                        "\t+[2,1]:**.*.\n" +
                        "\t+[2,1]:**..*\n" +
                        "\t+[2,1]:.**.*",
                getCombinations("2,1", "UUUUU"));
    }

    @Test
    public void test_4() {
        // ряд чисел отсутствует
        // это значит только одно на UNSET будет WHITE
        assertEquals("true:[W:0%]",
                getCombinations("0", "U"));
    }

    @Test
    public void test_5() {
        // ряд чисел отсутствует, но предложенa BLACK точка
        // это значит что solver вернет false -
        // это маркер ошибки рассчета
        assertEquals("false:[W:0%]",
                getCombinations("0", "B"));
    }

    @Test
    public void test_6() {
        // ряд чисел отсутствует
        // предложен 1 WHITE пиксель
        // это ок для solver и он ответит true
        // при этом вероятность = 0%, но стоит понимать что в probability
        // у нас хранятся вероятности BLACK, а не WHITE - так что все ок
        assertEquals("true:[W:0%]",
                getCombinations("0", "W"));
    }

    @Test
    public void test_7() {
        // пытаемся разместить ряд, который физически не может поместиться
        // solver вернет false что бы на вход не было предложено
        assertEquals("false:[U:-100%]",
                getCombinations("1,1", "U"));

        assertEquals("false:[B:-100%]",
                getCombinations("1,1", "B"));

        assertEquals("false:[W:-100%]",
                getCombinations("1,1", "W"));
    }

    @Test
    public void test_8() {
        // один пиксель размещается ок в одном UNSET
        assertEquals("true:[B:100%]\n" +
                        "\t+[1]:*",
                getCombinations("1", "U"));

        // так же если он уже установлен
        assertEquals("true:[B:100%]\n" +
                        "\t+[1]:*",
                getCombinations("1", "B"));

        // а вот WHITE не прокатит
        assertEquals("false:[W:-100%]\n" +
                        "\t-[1]:*",
                getCombinations("1", "W"));
    }

    @Test
    public void test_9() {
        // пустой ряд в двух позициях разместиться может
        assertEquals("true:[W:0%, W:0%]",
                getCombinations("", "UU"));

        assertEquals("true:[W:0%, W:0%]",
                getCombinations("0", "UU"));

        // даже если через запятую будем размещать нули
        assertEquals("true:[W:0%, W:0%]",
                getCombinations("0,0", "UU"));

        assertEquals("true:[W:0%, W:0%]",
                getCombinations("0,0,0", "UU"));
    }

    @Test
    public void test_10() {
        // даже если укажем 0 перед 3, то это не сможет вместиться в 1 пиксель
        assertEquals("false:[U:-100%]",
                getCombinations("0,3", "U"));
    }

    @Test
    public void test_11() {
        // все помещается
        assertEquals("true:[B:100%, W:0%, B:100%, B:100%]\n" +
                        "\t+[1,2]:*.**",
                getCombinations("1,2", "UUUU"));

        assertEquals("true:[B:100%, B:100%, W:0%, B:100%]\n" +
                        "\t+[2,1]:**.*",
                getCombinations("2,1", "UUUU"));

        assertEquals("true:[U:50%, B:100%, B:100%, U:50%]\n" +
                        "\t+[3]:***.\n" +
                        "\t+[3]:.***",
                getCombinations("3", "UUUU"));

        assertEquals("true:[B:100%, B:100%, B:100%, B:100%]\n" +
                        "\t+[4]:****",
                getCombinations("4", "UUUU"));
    }

    @Test
    public void test_12() {
        // все помещается
        assertEquals("true:[B:100%, B:100%]\n" +
                        "\t+[2]:**",
                getCombinations("2", "UU"));

        // а тут нет
        assertEquals("false:[U:-100%, U:-100%]",
                getCombinations("1,2", "UU"));

        assertEquals("false:[U:-100%, U:-100%]",
                getCombinations("2,1", "UU"));
    }

    @Test
    public void test_13() {
        // все помещается, даже если окружить нолями
        assertEquals("true:[B:100%, B:100%]\n" +
                        "\t+[2]:**",
                getCombinations("0,2", "UU"));

        assertEquals("true:[B:100%, B:100%]\n" +
                        "\t+[2]:**",
                getCombinations("2,0", "UU"));

        assertEquals("true:[B:100%, B:100%]\n" +
                        "\t+[2]:**",
                getCombinations("0,2,0", "UU"));

        assertEquals("true:[B:100%, B:100%]\n" +
                        "\t+[2]:**",
                getCombinations("0,0,2,0,0,0", "UU"));
    }

    @Test
    public void test_14() {
        // не помещается
        assertEquals("false:[U:-100%]",
                getCombinations("1,1,1", "U"));

        assertEquals("false:[U:-100%, U:-100%]",
                getCombinations("1,1,1", "UU"));

        assertEquals("false:[U:-100%, U:-100%, U:-100%]",
                getCombinations("1,1,1", "UUU"));

        assertEquals("false:[U:-100%, U:-100%, U:-100%, U:-100%]",
                getCombinations("1,1,1", "UUUU"));

        // дальше места хватает
        assertEquals("true:[B:100%, W:0%, B:100%, W:0%, B:100%]\n" +
                        "\t+[1,1,1]:*.*.*",
                getCombinations("1,1,1", "UUUUU"));

        assertEquals("true:[U:75%, U:25%, U:50%, U:50%, U:25%, U:75%]\n" +
                        "\t+[1,1,1]:*.*.*.\n" +
                        "\t+[1,1,1]:*.*..*\n" +
                        "\t+[1,1,1]:*..*.*\n" +
                        "\t+[1,1,1]:.*.*.*",
                getCombinations("1,1,1", "UUUUUU"));

        assertEquals("true:[U:60%, U:30%, U:40%, U:40%, U:40%, U:30%, U:60%]\n" +
                        "\t+[1,1,1]:*.*.*..\n" +
                        "\t+[1,1,1]:*.*..*.\n" +
                        "\t+[1,1,1]:*.*...*\n" +
                        "\t+[1,1,1]:*..*.*.\n" +
                        "\t+[1,1,1]:*..*..*\n" +
                        "\t+[1,1,1]:*...*.*\n" +
                        "\t+[1,1,1]:.*.*.*.\n" +
                        "\t+[1,1,1]:.*.*..*\n" +
                        "\t+[1,1,1]:.*..*.*\n" +
                        "\t+[1,1,1]:..*.*.*",
                getCombinations("1,1,1", "UUUUUUU"));

        assertEquals("true:[U:50%, U:30%, U:35%, U:35%, U:35%, U:35%, U:30%, U:50%]\n" +
                        "\t+[1,1,1]:*.*.*...\n" +
                        "\t+[1,1,1]:*.*..*..\n" +
                        "\t+[1,1,1]:*.*...*.\n" +
                        "\t+[1,1,1]:*.*....*\n" +
                        "\t+[1,1,1]:*..*.*..\n" +
                        "\t+[1,1,1]:*..*..*.\n" +
                        "\t+[1,1,1]:*..*...*\n" +
                        "\t+[1,1,1]:*...*.*.\n" +
                        "\t+[1,1,1]:*...*..*\n" +
                        "\t+[1,1,1]:*....*.*\n" +
                        "\t+[1,1,1]:.*.*.*..\n" +
                        "\t+[1,1,1]:.*.*..*.\n" +
                        "\t+[1,1,1]:.*.*...*\n" +
                        "\t+[1,1,1]:.*..*.*.\n" +
                        "\t+[1,1,1]:.*..*..*\n" +
                        "\t+[1,1,1]:.*...*.*\n" +
                        "\t+[1,1,1]:..*.*.*.\n" +
                        "\t+[1,1,1]:..*.*..*\n" +
                        "\t+[1,1,1]:..*..*.*\n" +
                        "\t+[1,1,1]:...*.*.*",
                getCombinations("1,1,1", "UUUUUUUU"));

        assertEquals("true:[U:43%, U:29%, U:31%, U:31%, U:31%, U:31%, U:31%, U:29%, U:43%]\n" +
                        "\t+[1,1,1]:*.*.*....\n" +
                        "\t+[1,1,1]:*.*..*...\n" +
                        "\t+[1,1,1]:*.*...*..\n" +
                        "\t+[1,1,1]:*.*....*.\n" +
                        "\t+[1,1,1]:*.*.....*\n" +
                        "\t+[1,1,1]:*..*.*...\n" +
                        "\t+[1,1,1]:*..*..*..\n" +
                        "\t+[1,1,1]:*..*...*.\n" +
                        "\t+[1,1,1]:*..*....*\n" +
                        "\t+[1,1,1]:*...*.*..\n" +
                        "\t+[1,1,1]:*...*..*.\n" +
                        "\t+[1,1,1]:*...*...*\n" +
                        "\t+[1,1,1]:*....*.*.\n" +
                        "\t+[1,1,1]:*....*..*\n" +
                        "\t+[1,1,1]:*.....*.*\n" +
                        "\t+[1,1,1]:.*.*.*...\n" +
                        "\t+[1,1,1]:.*.*..*..\n" +
                        "\t+[1,1,1]:.*.*...*.\n" +
                        "\t+[1,1,1]:.*.*....*\n" +
                        "\t+[1,1,1]:.*..*.*..\n" +
                        "\t+[1,1,1]:.*..*..*.\n" +
                        "\t+[1,1,1]:.*..*...*\n" +
                        "\t+[1,1,1]:.*...*.*.\n" +
                        "\t+[1,1,1]:.*...*..*\n" +
                        "\t+[1,1,1]:.*....*.*\n" +
                        "\t+[1,1,1]:..*.*.*..\n" +
                        "\t+[1,1,1]:..*.*..*.\n" +
                        "\t+[1,1,1]:..*.*...*\n" +
                        "\t+[1,1,1]:..*..*.*.\n" +
                        "\t+[1,1,1]:..*..*..*\n" +
                        "\t+[1,1,1]:..*...*.*\n" +
                        "\t+[1,1,1]:...*.*.*.\n" +
                        "\t+[1,1,1]:...*.*..*\n" +
                        "\t+[1,1,1]:...*..*.*\n" +
                        "\t+[1,1,1]:....*.*.*",
                getCombinations("1,1,1", "UUUUUUUUU"));
    }

    @Test
    public void test_15() {
        // кейз когда все почти впритык, но есть 1 белый пиксель после последнего черного
//        assertEquals("true:[U:50%, U:50%]\n" +
//                        "\t+[1]:*.\n" +
//                        "\t+[1]:.*",
//                getCombinations("1", "UU"));

        assertEquals("true:[U:67%, U:33%, U:33%, U:67%]\n" +
                        "\t+[1,1]:*.*.\n" +
                        "\t+[1,1]:*..*\n" +
                        "\t+[1,1]:.*.*",
                getCombinations("1,1", "UUUU"));

        assertEquals("true:[U:75%, U:25%, U:50%, U:50%, U:25%, U:75%]\n" +
                        "\t+[1,1,1]:*.*.*.\n" +
                        "\t+[1,1,1]:*.*..*\n" +
                        "\t+[1,1,1]:*..*.*\n" +
                        "\t+[1,1,1]:.*.*.*",
                getCombinations("1,1,1", "UUUUUU"));

        assertEquals("true:[U:80%, U:20%, U:60%, U:40%, U:40%, U:60%, U:20%, U:80%]\n" +
                        "\t+[1,1,1,1]:*.*.*.*.\n" +
                        "\t+[1,1,1,1]:*.*.*..*\n" +
                        "\t+[1,1,1,1]:*.*..*.*\n" +
                        "\t+[1,1,1,1]:*..*.*.*\n" +
                        "\t+[1,1,1,1]:.*.*.*.*",
                getCombinations("1,1,1,1", "UUUUUUUU"));
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
            if (history) {
                e.printStackTrace();
            }
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
