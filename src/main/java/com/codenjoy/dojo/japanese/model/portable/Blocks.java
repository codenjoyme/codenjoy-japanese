package com.codenjoy.dojo.japanese.model.portable;

public class Blocks {

    public static class Info {
        public int length;
        public boolean isBlack;
    }
    
    private int current;
    private Info[] items;

    public Blocks(int length) {
        items = new Info[length + 1];
        for (int i = 1; i < items.length; i++) {
            items[i] = new Info();
        }
    }

    public void packTightToTheLeft(int[] numbers, int countNumbers, Range range) {
        // упаковываем максимально все блоки слева впритык друг к другу
        // возвращаем количество блоков, которые удалось упаковать
        current = 0;
        int offset = 0;
        for (int numIndex = 1; numIndex <= countNumbers; numIndex++) {
            current++;
            if (current >= items.length) {
                // работаем дальше, если нам хватает места
                break;
            }

            // если размер BLACK блока больше 0 тогда мы рисуем его
            if (numbers[numIndex] > 0) {
                items[current].isBlack = true;
                items[current].length = numbers[numIndex];
                // отступаем в ряду размер новосозданного блока
                offset += items[current].length;

                current++;
                if (current >= items.length) {
                    // работаем дальше, если нам хватает места
                    break;
                }
            }

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
        }
    }

    public void saveCombinations(Range range, boolean[] combinations) {
        int offset = range.from() - 1;
        for (int block = 1; block <= items.length - 1; block++) {
            for (int len = 1; len <= items[block].length; len++) {
//                if (offset + len > len || block > len) {
//                    break; // TODO этого не должно происходить, но происходит
//                }
                combinations[offset + len] = items[block].isBlack;
            }
            offset += items[block].length;
        }
    }

    public void loadCombination(Range range, boolean[] combinations) {
        boolean color = combinations[range.from()];
        int blockLength = 1;
        current = 1;
        for (int offset = range.from() + 1; offset <= range.to(); offset++) {
//            if (offset >= combinations.length) break; // TODO этого не должно происходить но происходит
            if (combinations[offset] ^ color) {
                items[current].length = blockLength;
                items[current].isBlack = color;
                current++; ;
                blockLength = 0;
            }
            blockLength++;
            color = combinations[offset];
        }
        if (blockLength != 0) {
            items[current].length = blockLength;
            items[current].isBlack = color;
        }
    }

    // TODO надо дорефакторить
    public boolean hasNext() {
        boolean b2 = false;
        int a = current;
        while (true) {
            if (items[a].isBlack) {
                a--;
                if (a <= 0) {
                    return false;
                }
                boolean b = true;
                while (true) {
                    int a2 = 2;
                    if (!b) {
                        continue;
                    }

                    if (a + 2 > current) {
                        if (a + 2 != current + 1) break;
                        if (!items[current].isBlack) break;

                        b2 = false;
                        if (items[a].length != 1 && current == 2) {
                            b2 = true;
                        }
                        while (items[a].length == 1) {
                            a -= 2;
                            a2 += 2;
                            if (a <= 0 || a2 == current) {
                                b2 = true;
                                break;
                            }
                        }

                        if (b2) break;

                        current++;
                        items[current].isBlack = false;
                        items[current].length = 0;
                    }
                    items[a + a2].length = items[a + a2].length + items[a].length - 2;
                    items[a].length = 2;
                    break;
                }
                if (b2) {
                    return false;
                }
            } else {
                if (current == 1) {
                    return false;
                }
                if (a - 2 <= 0) {
                    if (a < 1) break;
                    current++;
                    for (int i = current - 1; i >= 1; i--) {
                        items[i + 1].length = items[i].length;
                        items[i + 1].isBlack = items[i].isBlack;
                    }
                    items[1].length = 1;
                    items[1].isBlack = false;
                    if (current > 3) {
                        items[3].length = 1;
                    }
                    if (current == 3) {
                        items[3].length--;
                    }
                    break;
                }
                items[a - 2].length++;
                items[a].length--;
                if (items[a].length == 0) {
                    if (a == current) {
                        current--;
                        break;
                    } else {
                        items[a - 2].length--;
                        items[a].length++;
                        a = a - 2;
                    }
                    continue;
                } else {
//                   current--;
                    break;
                }
            }
        }
        return true;
    }

}
