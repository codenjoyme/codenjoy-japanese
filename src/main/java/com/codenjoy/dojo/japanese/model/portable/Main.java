package com.codenjoy.dojo.japanese.model.portable;

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
