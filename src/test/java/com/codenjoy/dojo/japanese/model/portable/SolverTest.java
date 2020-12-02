package com.codenjoy.dojo.japanese.model.portable;

import com.codenjoy.dojo.japanese.model.level.LevelImpl;
import org.junit.Test;

import static org.junit.Assert.*;

public class SolverTest {

    @Test
    public void case1() {
        assertS("-**---**-\n" +
                "****-****\n" +
                "*********\n" +
                "*********\n" +
                "*********\n" +
                "-*******-\n" +
                "--*****--\n" +
                "---***---\n" +
                "----*----",

                "           \n" +
                "  467777764\n" +
                "22---------\n" +
                "44---------\n" +
                " 9---------\n" +
                " 9---------\n" +
                " 9---------\n" +
                " 7---------\n" +
                " 5---------\n" +
                " 3---------\n" +
                " 1---------\n",

                "           \n" +
                "  467777764\n" +
                "22---------\n" +
                "44****-****\n" +
                " 9*********\n" +
                " 9*********\n" +
                " 9*********\n" +
                " 7--*****--\n" +
                " 5----*----\n" +
                " 3---------\n" +
                " 1---------\n");
    }

    @Test
    public void case2() {
        assertS("---------\n" +
                "---------\n" +
                "*********\n" +
                "*---*---*\n" +
                "**-***-**\n" +
                "--*****--\n" +
                "--*---*--\n" +
                "--*****--\n" +
                "---------",

                "      1 1   \n" +
                "    1124211 \n" +
                "   313111313\n" +
                " 11---------\n" +
                "112---------\n" +
                "313---------\n" +
                "  9---------\n" +
                "111---------\n" +
                "232---------\n" +
                "  5---------\n" +
                " 11---------\n" +
                "  5---------\n",

                "      1 1   \n" +
                "    1124211 \n" +
                "   313111313\n" +
                " 11---------\n" +
                "112---------\n" +
                "313---------\n" +
                "  9---------\n" +
                "111---------\n" +
                "232---------\n" +
                "  5---------\n" +
                " 11---------\n" +
                "  5---------\n");
    }

    private void assertS(String map, String expected, String expected1) {
        // given
        Solver solver = new Solver();
        solver.tryAssumption = true;
        solver.load(new LevelImpl(map));
        solver.clear(false);
        assertEquals(expected, solver.printAll());

        // when
        solver.solve();

        // then
        assertEquals(expected1, solver.printAll());
    }

}