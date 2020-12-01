package com.codenjoy.dojo.japanese.model.portable;

import com.codenjoy.dojo.japanese.model.level.LevelImpl;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;

public class Main {

    public static void main(String[] args) {
        String folder = "games\\japanese\\src\\main\\resources\\data\\jak\\";
        Unit1 unit = new Unit1();
        unit.FormCreate();
        unit.assumption = true; // не будем гадать, пробуем решить точно
        unit.loadFile(true, folder + "5.jap", 1);
        unit.ClearData(false); // чистим только поле
        unit.solve();
        unit.saveFile(1, folder + "!5.jap");
        unit.saveFile(2, folder + "!5.jdt");

        String board = unit.printData();

        LevelImpl level = new LevelImpl(board);

        String board2 = printLevel(level);

        System.out.println(board);
    }

    private static String printLevel(LevelImpl level) {
        return (String)new PrinterFactoryImpl<>().getPrinter(level, null).print();
    }


}
