package com.codenjoy.dojo.japanese.model.portable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

class TextFile {

    private File file;
    private List<String> lines;
    private int index;
    private Writer writer;

    public void open(String fileName) {
        file = new File(fileName);
        lines = new LinkedList<>();
    }

    public void loadData() {
        try (Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()))) {
            stream.forEach(lines::add);
            index = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine() {
        return lines.get(index++);
    }

    public void close() {
        lines.clear();
        index = 0;
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer = null;
        }
    }

    public void openWrite() {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeLine(String text) {
        try {
            writer.write(text + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
