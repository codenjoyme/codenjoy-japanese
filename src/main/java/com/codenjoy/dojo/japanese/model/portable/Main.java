package com.codenjoy.dojo.japanese.model.portable;

import com.codenjoy.dojo.japanese.model.level.Level;
import com.codenjoy.dojo.japanese.model.level.LevelImpl;
import com.codenjoy.dojo.japanese.model.level.levels.Level1;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;

public class Main {

    public static void main(String[] args) {
        Unit1 unit = new Unit1();
        unit.init();
        unit.assumption = true; // не будем гадать, пробуем решить точно

        Level level = new Level1();
        unit.load(level);
        unit.solve();

//        String folder = "games\\japanese\\src\\main\\resources\\data\\jak\\";
//        unit.loadFile(true, folder + "5.jap", 1);
//        // unit.clear(false); // чистим только поле
//        unit.solve();
//        unit.saveFile(1, folder + "!5.jap");
//        unit.saveFile(2, folder + "!5.jdt");

        System.out.println("Поле без цифер");
        String board = unit.printData();
        System.out.println(board);
        System.out.println();
        System.out.println();

        System.out.println("Поле с цифрами");
        String board2 = unit.printAll();
        System.out.println(board2);
        System.out.println();
        System.out.println();

        System.out.println("Поле без цифр загруженное в Level (с пересчетом цифр)");
        LevelImpl level3 = new LevelImpl(board);
        String board3 = printLevel(level3);
        System.out.println(board3);

    }

    private static String printLevel(LevelImpl level) {
        return (String)new PrinterFactoryImpl<>().getPrinter(level, null).print();
    }


}
