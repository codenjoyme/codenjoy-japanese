package com.codenjoy.dojo.japanese.model.portable;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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

    public boolean packTightToTheLeft(int[] numbers, int countNumbers, int length) {
        int size = 0;
        for (int numIndex = 1; numIndex <= countNumbers; numIndex++) {
            if (size != 0) {
                // WHITE только после BLACK блока
                // BLACK всегда идет первым
                size += SMALLEST_WHITE; // WHITE
            }
            size += numbers[numIndex]; // BLACK
        }
        if (size > length) {
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

        if (offset < length) {
            // последний WHITE блок мы делаем длинной так, чтобы он заполнил остаток рабочей зоны range (от from до to)
            // по умолчанию он длинной 1
            items[current].length = items[current].length + length - offset;
        } else if (offset == length) {
            // вписались точно, ничего делать не надо
        } else if (offset > length) {
            items[current].length = 0;
        }
        return true;
    }

    public void saveCombinations(boolean[] combinations) {
        int offset = 0;
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
