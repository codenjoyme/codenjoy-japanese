package com.codenjoy.dojo.japanese.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.printer.CharElements;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum Elements implements CharElements {

    WHITE('-', 1),      // игрок утверждает, что пиксель белый
    BLACK('X', 0),      // игрок утверждает, что пиксель черный
    UNSET(' ', -1),    // игрок пока не определился, какого цвета этот пиксель

    NAN('.', -1),        // пустое место в полое для цифер
    _0('0', 0),         // блок отстутствует
    _1('1', 1),         // блок длинной в 1
    _2('2', 2),         // блок длинной в 2
    _3('3', 3),         // ...
    _4('4', 4),
    _5('5', 5),
    _6('6', 6),
    _7('7', 7),
    _8('8', 8),
    _9('9', 9),
    _10('a', 10),
    _11('b', 11),
    _12('c', 12),
    _13('d', 13),
    _14('e', 14),
    _15('f', 15),
    _16('g', 16),
    _17('h', 17),
    _18('i', 18),
    _19('j', 19),
    _20('k', 20),
    _21('l', 21),
    _22('m', 22),
    _23('n', 23),
    _24('o', 24),
    _25('p', 25),
    _26('q', 26),
    _27('r', 27),
    _28('s', 28),
    _29('t', 29),
    _30('u', 30),
    _31('v', 31),
    _32('w', 32),
    _33('x', 33),
    _34('y', 34),
    _35('z', 35),
    _36('A', 36),
    _37('B', 37),
    _38('C', 38),
    _39('D', 39),
    _40('E', 40),
    _41('F', 41),
    _42('G', 42),
    _43('H', 43),
    _44('I', 44),
    _45('J', 45),
    _46('K', 46),
    _47('L', 47),
    _48('M', 48),
    _49('N', 49),
    _50('O', 50),
    _51('P', 51),
    _52('Q', 52),
    _53('R', 53),
    _54('S', 54),
    _55('T', 55),
    _56('U', 56),
    _57('V', 57),
    _58('W', 58),
    _59('X', 59),
    _60('Y', 60),
    _61('Z', 61);

    final char ch;
    final int code;

    Elements(char ch, int code) {
        this.ch = ch;
        this.code = code;
    }

    public static Elements forNumber(int number) {
        return Elements.valueOf("_" + number);
    }

    public static Elements[] getNumbers() {
        return valuesExcept(NAN, WHITE, BLACK, UNSET);
    }

    public int code() {
        return code;
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static Elements valueOf(char ch) {
        for (Elements el : Elements.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

    public static Elements[] valuesExcept(Elements... excluded) {
        List<Elements> list = Arrays.asList(excluded);
        return Arrays.stream(values())
                .filter(el -> !list.contains(el))
                .collect(toList())
                .toArray(new Elements[0]);
    }

}
