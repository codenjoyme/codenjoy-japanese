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

import com.codenjoy.dojo.japanese.model.level.levels.Level1;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;

public class Main {

    public static void main(String[] args) {
        Solver solver = new Solver(true);

//        String folder = "games\\japanese\\src\\main\\resources\\data\\jak\\";
//        solver.loadFile(true, folder + "5.jap", 1);
//        solver.clear(false); // чистим только поле
//        solver.solve();
//        solver.saveFile(1, folder + "!5.jap");
//        solver.saveFile(2, folder + "!5.jdt");

        solver.load(new Level1());
        solver.clear(false);
        print(solver);
        solver.solve();
        print(solver);

    }

    private static void print(Solver solver) {
        System.out.println("Поле с цифрами");
        String board = solver.printAll();
        System.out.println(board);
        System.out.println();
    }

    private static String print(BoardReader reader) {
        return (String)new PrinterFactoryImpl<>().getPrinter(reader, null).print();
    }


}
