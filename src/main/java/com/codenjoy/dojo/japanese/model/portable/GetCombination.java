package com.codenjoy.dojo.japanese.model.portable;

public interface GetCombination {

    void saveCombinations(boolean[] combinations);

    boolean hasNext();

    void saveApplicable(boolean[] combinations);

    boolean packTightToTheLeft(int length);

    void removeNotApplicable();
}
