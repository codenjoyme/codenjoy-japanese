package com.codenjoy.dojo.japanese.model.level;

import com.codenjoy.dojo.japanese.model.level.levels.Level1;
import com.codenjoy.dojo.japanese.model.level.levels.Level1WithoutNumbers;
import com.codenjoy.dojo.japanese.model.level.levels.Level1WithoutPixels;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LevelImplTest {

    private Level level;

    @Test
    public void shouldParse_whenPixels_andNumbers() {
        level = new Level1();

        assertB("...........\n" +
                "..467777764\n" +
                "22-**---**-\n" +
                "44****-****\n" +
                ".9*********\n" +
                ".9*********\n" +
                ".9*********\n" +
                ".7-*******-\n" +
                ".5--*****--\n" +
                ".3---***---\n" +
                ".1----*----\n");
    }

    @Test
    public void shouldParse_whenPixels_andNoNumbers() {
        level = new Level1WithoutNumbers();

        assertB("...........\n" +
                "..467777764\n" +
                "22-**---**-\n" +
                "44****-****\n" +
                ".9*********\n" +
                ".9*********\n" +
                ".9*********\n" +
                ".7-*******-\n" +
                ".5--*****--\n" +
                ".3---***---\n" +
                ".1----*----\n");
    }

    @Ignore // TODO реализовать решалку
    @Test
    public void shouldParse_whenNoPixels_andNumbers() {
        level = new Level1WithoutPixels();

        assertB("...........\n" +
                "..467777764\n" +
                "22-**---**-\n" +
                "44****-****\n" +
                ".9*********\n" +
                ".9*********\n" +
                ".9*********\n" +
                ".7-*******-\n" +
                ".5--*****--\n" +
                ".3---***---\n" +
                ".1----*----\n");
    }

    private void assertB(String expected) {
        String board = (String) new PrinterFactoryImpl<>().getPrinter((BoardReader)level, null).print();
        assertEquals(expected, board);
    }

}