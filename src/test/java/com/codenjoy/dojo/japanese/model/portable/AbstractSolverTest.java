package com.codenjoy.dojo.japanese.model.portable;

import com.codenjoy.dojo.japanese.model.level.LevelImpl;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class AbstractSolverTest {

    protected void assertF(String fileName, String expected) {
        Solver solver = new Solver(true);
        String file = "src\\main\\resources\\data\\" + fileName;
        String board = process(solver, new File(file));
        assertEquals(expected, board);
    }

    private String process(Solver solver, File file) {
        new FileReaderWriter(solver).loadFile(file.getAbsolutePath(), 1);
        solver.clear(false); // чистим только поле
        solver.solve();
        return solver.getOpened() + "\n"
                + solver.printAll() + "\n\n";
    }

    protected void assertA(boolean tryAssumption, String map, String expected) {
        // given
        Solver solver = new Solver(tryAssumption);
        solver.load(new LevelImpl(map));

        // when
        solver.solve();

        // then
        assertEquals(expected, solver.printAll());
    }

    protected void assertS(boolean tryAssumption, String map, String expected, String expected1) {
        // given
        Solver solver = new Solver(tryAssumption);
        solver.load(new LevelImpl(map));
        solver.clear(false);
        assertEquals(expected, solver.printAll());

        // when
        solver.solve();

        // then
        assertEquals(expected1, solver.printAll());
    }
}
