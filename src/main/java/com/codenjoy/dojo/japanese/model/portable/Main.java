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

//        String folder = "games\\japanese\\src\\main\\resources\\data\\jak\\";
//        unit.loadFile(true, folder + "5.jap", 1);
//        // unit.clear(false); // чистим только поле
//        unit.solve();
//        unit.saveFile(1, folder + "!5.jap");
//        unit.saveFile(2, folder + "!5.jdt");


        unit.load(new Level1());
        unit.clear(false);
        print(unit);
        unit.solve();
        print(unit);

    }

    private static void print(Unit1 unit) {
        System.out.println("Поле с цифрами");
        String board2 = unit.printAll();
        System.out.println(board2);
        System.out.println();
    }

    private static String printLevel(LevelImpl level) {
        return (String)new PrinterFactoryImpl<>().getPrinter(level, null).print();
    }


}
