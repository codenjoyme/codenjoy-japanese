package com.codenjoy.dojo.japanese.model.portable;

public class Main {

    public static void main(String[] args) {
        Unit1 unit1 = new Unit1();
        unit1.FormCreate();
        unit1.assumption = false; // не будем гадать, пробуем решить точно
        unit1.btLoadClick(true, "games\\japanese\\src\\main\\resources\\data\\banzai\\1.jap", 1);
//        unit1.btSaveClick(1, "1.jap");
//        unit1.btSaveClick(2, "1.jdt");
        unit1.btCalcClick();
    }
}
