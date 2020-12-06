package com.codenjoy.dojo.japanese.model.portable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static com.codenjoy.dojo.japanese.model.portable.Solver.EXACTLY_NOT_BLACK;
import static java.util.stream.Collectors.joining;

public class LineSolver {

    private Dot[] dots;
    private int length;

    private int[] numbers;
    private int countNumbers;

    private Blocks blocks;
    private Probabilities probabilities;
    private Range range;
    private Cut cut;

    protected boolean enableHistory = false;
    private List<String> history;

    public boolean calculate(int[] inputNumbers, Dot[] inputDots) {
        dots = inputDots;
        numbers = Arrays.stream(inputNumbers)
                .filter(number -> number != 0)
                .toArray();
        int[] copy = new int[numbers.length + 1];
        System.arraycopy(numbers, 0, copy, 1,
                Math.min(numbers.length, numbers.length + 1));
        numbers = copy;

        length = dots.length - 1;
        countNumbers = numbers.length - 1;

        probabilities = new Probabilities(length);
        blocks = new Blocks(countNumbers*2 + 1);
        range = new Range(length);
        cut = new Cut(this);
        history = new LinkedList<>();

        // если цифер нет вообще, то в этом ряде не может быть никаких BLACK
        // если кто-то в ходе проверки гипотезы все же сделал это, то
        // мы все равно поставим все белые и скажем что calculate этого сценария невозможен
        if (countNumbers == 0) {
            boolean foundBlack = false;
            for (int i = 1; i <= length; i++) {
                if (dots[i] == Dot.BLACK) {
                    foundBlack = true;
                }
                dots[i] = Dot.WHITE;
                probabilities.set(i, EXACTLY_NOT_BLACK);
            }
            return !foundBlack;
        }

        boolean result = true;
        // обрезаем слева и справа уже отгаданные числа
//        if (cut.process()) {
            // дальше работаем в диапазоне from...to
            result = generateCombinations();
//        }

        // и превращаем явные (1.0, 0.0) probabilities в dots
        probabilities.updateDots(dots);

        // возвращаем возможность этой комбинации случиться для этого ряда чисел
        return result;
    }

    public String toString(boolean applicable) {
        String nums = IntStream.range(1, numbers.length)
                .mapToObj(index -> numbers[index])
                .map(number -> String.valueOf(number))
                .collect(joining(","));

        boolean[] combinations = probabilities.combinations();
        String dots = IntStream.range(1, length + 1)
                .mapToObj(index -> combinations[index])
                .map(b -> (b)?"*":".")
                .collect(joining(""));

        return String.format("%s[%s]:%s", applicable ? "+" : "-", nums, dots);
    }

    private boolean generateCombinations() {
        if (!blocks.packTightToTheLeft(numbers, countNumbers, range)) {
            // если упаковка не удалась (места не хватило), то дальше нет смысла считать
            return false;
        }

        // пошли генерить комбинации
        do {
            blocks.saveCombinations(range, probabilities.combinations());
            boolean applicable = probabilities.isApplicable(range, dots);
            if (enableHistory) {
                history.add(toString(applicable));
            }
            if (applicable) {
                probabilities.addCombination(range);
            }
            // blocks.loadCombination(range, probabilities.combinations());
        } while (blocks.hasNext());

        probabilities.calculate(range);
        return probabilities.isAny();
    }

    public void shlNumbers() {
        for (int j = 2; j <= countNumbers; j++) {
            numbers[j - 1] = numbers[j];
        }
        countNumbers--;
    }

    public double probability(int x) {
        return probabilities.get(x);
    }

    public Dot dots(int x) {
        return dots[x];
    }

    public int length() {
        return length;
    }

    public int numbers(int index) {
        return numbers[index];
    }

    public Range range() {
        return range;
    }

    public Probabilities probabilities() {
        return probabilities;
    }

    public int countNumbers() {
        return countNumbers;
    }

    public void countNumbers(int count) {
        countNumbers = count;
    }

    public List<String> history() {
        return history;
    }
}
