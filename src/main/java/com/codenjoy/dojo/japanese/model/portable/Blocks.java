package com.codenjoy.dojo.japanese.model.portable;

import org.apache.commons.lang3.StringUtils;

public class Blocks {

    private static final int SMALLEST_WHITE = 1;

    public static class Info {

        public int length;
        public boolean isBlack;

        @Override
        public String toString() {
            String color = isBlack ? "*" : ".";
            return StringUtils.repeat(color, length);
        }
    }
    
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
                size += SMALLEST_WHITE; // WHITE
            }
            size += numbers[numIndex]; // BLACK
        }
        if (size > range.length()) {
            // если упаковать не получится, сразу выходим и сигнализируем
            return false;
        }

        // упаковываем максимально все блоки слева впритык друг к другу
        // возвращаем количество блоков, которые удалось упаковать
        int current = 1;

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
            items[current].length = SMALLEST_WHITE;
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

    // метод перебирает варианты рсположения заданного набора рядов
    // true возвращает, если есть еще комбинация
    public boolean hasNext() {
        // мы идем от хвоста в голову последовательности
        // что мы ищем - так это WHITE блок длинной больше 1
        for (int i = items.length - 1; i >= 1; i--) {
            // если текущий элемент BLACK идем дальше
            if (items[i].isBlack) {
                continue;
            }

            // мы так же пропускаем
            // 1) первый (с хвоста) WHITE если его длинна = 0
            // 2) каждый последующий WHITE если его длинна = 1
            int skipWhiteLength = (i == items.length - 1) ? 0 : SMALLEST_WHITE;
            if (items[i].length == skipWhiteLength) {
                continue;
            }

            // мы выходим если достигли начала последовательности
            if (i == 1) {
                break;
            }

            // нашли WHITE блок длинной больше 1
            // 1 пиксель с него перекидываем через BLACK ряд к следующему со стороны головы WHITE ряду
            // так мы как бы перемещаем BLACK к хвосту
            int odd = 2;
            items[i - odd].length++;
            items[i].length--;

            // но это не все - все последующие BLACK рядки надо сомкнуть к текущему WHITE впритык
            for (int j = i; j < items.length - 2; j += odd) {
                items[j + odd].length += items[j].length - SMALLEST_WHITE;
                items[j].length = SMALLEST_WHITE;
            }

            return true;
        }

        return false;
    }

}
