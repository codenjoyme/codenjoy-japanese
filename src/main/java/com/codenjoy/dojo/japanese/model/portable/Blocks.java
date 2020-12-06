package com.codenjoy.dojo.japanese.model.portable;

import org.apache.commons.lang3.StringUtils;

public class Blocks {

    public static class Info {

        public int length;
        public boolean isBlack;

        @Override
        public String toString() {
            String color = isBlack ? "*" : ".";
            return StringUtils.repeat(color, length);
        }
    }
    
    private int current;
    private Info[] items;

    public Blocks(int length) {
        items = new Info[length + 1];
        for (int i = 1; i < items.length; i++) {
            items[i] = new Info();
        }
    }

    public boolean packTightToTheLeft(int[] numbers, int countNumbers, Range range) {
        int size = 0;
        for (int numIndex = 1; numIndex <= countNumbers; numIndex++) {
            if (size != 0) {
                // WHITE только после BLACK блока
                // BLACK всегда идет первым
                size += 1; // WHITE
            }
            size += numbers[numIndex]; // BLACK
        }
        if (size > range.length()) {
            // если упаковать не получится, сразу выходим и сигнализируем
            return false;
        }

        // упаковываем максимально все блоки слева впритык друг к другу
        // возвращаем количество блоков, которые удалось упаковать
        current = 1;

        // первым идет WHITE, который нулевой длинны
        // позже нам он понадобится для подбора комбинаций
        items[current].isBlack = false;
        items[current].length = 0;

        int offset = 0;
        for (int numIndex = 1; numIndex <= countNumbers; numIndex++) {

            current++;

            items[current].isBlack = true;
            items[current].length = numbers[numIndex];
            // отступаем в ряду размер новосозданного блока
            offset += items[current].length;

            current++;

            // если нарисовали BLACK ряд после обязательно WHITE
            // за каждым BLACK идет WHITE
            items[current].isBlack = false;
            items[current].length = 1;
            // отступаем в ряду размер новосозданного блока
            offset += items[current].length;
        }

        if (offset < range.length()) {
            // последний WHITE блок мы делаем длинной так, чтобы он заполнил остаток рабочей зоны range (от from до to)
            // по умолчанию он длинной 1
            items[current].length = items[current].length + range.length() - offset;
        } else if (offset == range.length()) {
            // вписались точно, ничего делать не надо
        } else if (offset > range.length()) {
            items[current].length = 0;
        }
        return true;
    }

    public void saveCombinations(Range range, boolean[] combinations) {
        int offset = range.from() - 1;
        for (int block = 1; block <= items.length - 1; block++) {
            for (int len = 1; len <= items[block].length; len++) {
                combinations[offset + len] = items[block].isBlack;
            }
            offset += items[block].length;
        }
    }

    public boolean hasNext() {
        for (int i = items.length - 1; i >= 1; i--) {
            if (items[i].isBlack) {
                continue;
            }

            int skipWhiteLength = (i == items.length - 1) ? 0 : 1;
            if (items[i].length == skipWhiteLength) {
                continue;
            }

            if (i == 1) {
                break;
            }

            items[i - 2].length++;
            items[i].length--;

            for (int j = i; j < items.length - 2; j = j + 2) {
                items[j + 2].length += items[j].length - 1;
                items[j].length = 1;
            }

            return true;
        }

        return false;
    }

}
