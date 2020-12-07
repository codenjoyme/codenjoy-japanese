package com.codenjoy.dojo.japanese.model.portable;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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
