package com.codenjoy.dojo.japanese.model.portable;

import com.codenjoy.dojo.services.Point;

import java.io.File;
import java.util.function.BiFunction;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class FileReaderWriter {

    public static final String JAP_EXT = ".jap";
    public static final String JDT_EXT = ".jdt";

    private Solver solver;

    public FileReaderWriter(Solver solver) {
        this.solver = solver;
    }

    public void loadNumbersFromFile(String fileName) {
        TextFile file = new TextFile();
        file.open(fileName);
        file.loadData();

        solver.size(Integer.valueOf(file.readLine()), // ширина
                Integer.valueOf(file.readLine()));    // высота

        loadNumbers(file, (x, y) -> pt(x, y), solver.countNumbersX(), solver.numbersX(), solver.height());
        loadNumbers(file, (x, y) -> pt(y, x), solver.countNumbersY(), solver.numbersY(), solver.width());

        int maxX = 0;
        for (int y = 1; y <= solver.height(); y++) {
            maxX = Math.max(maxX, solver.countNumbersX(y));
        }

        int maxY = 0;
        for (int x = 1; x <= solver.width(); x++) {
            maxY = Math.max(maxY, solver.countNumbersY(x));
        }
        solver.offset(maxX, maxY);

        file.close();
    }

    private void loadNumbers(TextFile file, BiFunction<Integer, Integer, Point> mirror, int[] counts, int[][] numbers, int len) {
        for (int y = 1; y <= len; y++) { ;
            counts[y] = Integer.valueOf(file.readLine()); // длинна y строки
            for (int x = 1; x <= counts[y]; x++) {
                Point pt = mirror.apply(x, y);
                numbers[pt.getX()][pt.getY()] = Integer.valueOf(file.readLine()); // числа y строки
            }
        }
    }

    public void saveNumbersToFile(String fileName) {
        TextFile file = new TextFile();
        file.open(fileName);
        file.openWrite();

        file.writeLine(Integer.toString(solver.width()));
        file.writeLine(Integer.toString(solver.height()));

        for (int y = 1; y <= solver.height(); y++) {

            // длинна y строки
            int len = solver.countNumbersX(y);
            file.writeLine(Integer.toString(len));

            for (int x = 1; x <= len; x++) {
                // числа y строки
                file.writeLine(Integer.toString(solver.numbersX(x, y)));
            }
        }

        for (int x = 1; x <= solver.width(); x++) {

            // длинна х столбца
            int len = solver.countNumbersY(x);
            file.writeLine(Integer.toString(len));

            for (int y = 1; y <= len; y++) {
                // числа х столбца
                file.writeLine(Integer.toString(solver.numbersY(x, y)));
            }
        }
        file.close();
    }

    private String changeFileExt(String fileName, String ext) {
        return fileName.substring(0, fileName.lastIndexOf(".")) + ext;
    }

    private String extractfileName(String fileName) {
        return new File(fileName).getName();
    }

    private boolean fileExists(String fileName) {
        return new File(fileName).exists();
    }

    public void loadFile(String fileName, int mode) {
        switch (mode) {
            case 0: { // грузим оба файла
                String japFile = changeFileExt(fileName, JAP_EXT);
                String jdtFile = changeFileExt(fileName, JDT_EXT);
                if (fileExists(japFile) && fileExists(jdtFile)) {
                    loadNumbersFromFile(japFile);
                    loadDataFromFile(jdtFile);
                    System.out.println("Японские головоломки - "
                            + extractfileName(japFile)
                            + ", " + extractfileName(jdtFile));
                }
                break;
            }
            case 1: { // файл расшифровщика
                loadNumbersFromFile(fileName);
                System.out.println("Японские головоломки - "
                        + extractfileName(fileName));
            }
            break;
            case 2: { // файл редактора
                loadDataFromFile(fileName);
                solver.getNumbersX();
                solver.getNumbersY();
                System.out.println("Японские головоломки - "
                        + extractfileName(fileName));
            }
            break;
        }
    }

    public void saveFile(String fileName, int mode) {
        switch (mode) {
            case 0: {
                String japFile = changeFileExt(fileName, JAP_EXT);
                String jdtFile = changeFileExt(fileName, JDT_EXT);
                saveNumbersToFile(japFile);
                saveDataToFile(jdtFile);
            }
            break;
            case 1: {
                saveNumbersToFile(fileName);
            }
            break;
            case 2: {
                saveDataToFile(fileName);
            }
            break;
        }
    }

    public void loadDataFromFile(String fileName) {
        TextFile file = new TextFile();
        file.open(fileName);
        file.loadData();

        solver.size(Integer.valueOf(file.readLine()), // ширина
                Integer.valueOf(file.readLine()));    // высота

        for (int x = 1; x <= solver.width(); x++) {
            for (int y = 1; y <= solver.height(); y++) {
                solver.setDot(x, y, Dot.get(file.readLine()));
            }
        }
        file.close();
    }

    public void saveDataToFile(String fileName) {
        TextFile file = new TextFile();
        file.open(fileName);
        file.openWrite();

        file.writeLine(Integer.toString(solver.width()));
        file.writeLine(Integer.toString(solver.height()));

        for (int x = 1; x <= solver.width(); x++) {
            for (int y = 1; y <= solver.height(); y++) {
                file.writeLine(Integer.toString(solver.getDot(x, y).code()));
            }
        }
        file.close();
    }
}
