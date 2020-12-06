package com.codenjoy.dojo.japanese.model.portable;

import java.util.*;

public class CombinationsStorage {

    private Map<String, Combinations> map = new HashMap<>();

    public static class Combinations implements GetCombination {

        private List<boolean[]> combinations = new LinkedList<>();
        private int index;

        public Combinations() {
            reset();
        }

        @Override
        public void saveCombinations(boolean[] combination) {
            boolean[] saved = this.combinations.get(index);
            for (int i = 0; i < combination.length; i++) {
                combination[i] = saved[i];
            }
        }

        @Override
        public boolean hasNext() {
            index++;
            return index < combinations.size();
        }

        @Override
        public void saveApplicable(boolean[] combination) {
            boolean[] copy = Arrays.copyOf(combination, combination.length);
            combinations.add(copy);
        }

        @Override
        public boolean packTightToTheLeft(int length) {
            return false; // do nothing
        }

        @Override
        public void removeNotApplicable() {
            combinations.remove(index);
            index--;
        }

        public void reset() {
            index = 0;
        }
    }

    public GetCombination getFor(String key) {
        Combinations result = map.get(key);
        if (result == null) {
            result = new Combinations();
            map.put(key, result);
        }
        result.reset();
        return result;
    }

    public boolean has(String key) {
        return map.containsKey(key);
    }
}
