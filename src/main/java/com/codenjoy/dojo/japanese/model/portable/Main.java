package com.codenjoy.dojo.japanese.model.portable;

public class Main {

    public static void main(String[] args) {
        String folder = "games\\japanese\\src\\main\\resources\\data\\banzai\\";
        Unit1 unit1 = new Unit1();
        unit1.FormCreate();
        unit1.assumption = true; // не будем гадать, пробуем решить точно
        unit1.loadFile(true, folder + "1.jap", 1);
        unit1.solve();
        unit1.saveFile(1, folder + "!1.jap");
        unit1.saveFile(2, folder + "!1.jdt");
    }
}
