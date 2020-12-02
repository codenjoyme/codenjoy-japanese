package com.codenjoy.dojo.japanese.model.portable;

import com.codenjoy.dojo.japanese.model.level.LevelImpl;
import org.junit.Test;

import static org.junit.Assert.*;

public class SolverTest {

    @Test
    public void test() {
        Solver solver = new Solver();
        solver.init();
        solver.assumption = true;
        solver.load(new LevelImpl(
                "-**---**-\n" +
                "****-****\n" +
                "*********\n" +
                "*********\n" +
                "*********\n" +
                "-*******-\n" +
                "--*****--\n" +
                "---***---\n" +
                "----*----"));
        solver.clear(false);

        assertEquals(
                "              \n" +
                "              \n" +
                "              \n" +
                "              \n" +
                "     467777764\n" +
                "   22---------\n" +
                "   44---------\n" +
                "    9---------\n" +
                "    9---------\n" +
                "    9---------\n" +
                "    7---------\n" +
                "    5---------\n" +
                "    3---------\n" +
                "    1---------\n", solver.printAll());

        solver.solve();

        assertEquals(
                "              \n" +
                "              \n" +
                "              \n" +
                "              \n" +
                "     467777764\n" +
                "   22---------\n" +
                "   44****-****\n" +
                "    9*********\n" +
                "    9*********\n" +
                "    9*********\n" +
                "    7--*****--\n" +
                "    5----*----\n" +
                "    3---------\n" +
                "    1---------\n", solver.printAll());
    }

}