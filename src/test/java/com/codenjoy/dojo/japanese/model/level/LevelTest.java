package com.codenjoy.dojo.japanese.model.level;

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

import com.codenjoy.dojo.japanese.model.level.levels.*;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LevelTest {

    private Level level;

    @Test
    public void shouldParse_whenPixels_andNumbers_level1() {
        level = new Level1();

        assertB("...........\n" +
                "..467777764\n" +
                "22-**---**-\n" +
                "44****-****\n" +
                ".9*********\n" +
                ".9*********\n" +
                ".9*********\n" +
                ".7-*******-\n" +
                ".5--*****--\n" +
                ".3---***---\n" +
                ".1----*----\n");
    }

    @Test
    public void shouldParse_whenPixels_andNoNumbers_level1() {
        level = new Level1WithoutNumbers();

        assertB("...........\n" +
                "..467777764\n" +
                "22-**---**-\n" +
                "44****-****\n" +
                ".9*********\n" +
                ".9*********\n" +
                ".9*********\n" +
                ".7-*******-\n" +
                ".5--*****--\n" +
                ".3---***---\n" +
                ".1----*----\n");
    }

    @Test
    public void shouldParse_whenPixels_andNumbers_level10() {
        level = new Level10();

        assertB("........1.................\n" +
                "........23122.2.34...2....\n" +
                "........114332321321432...\n" +
                "........2444431312612111..\n" +
                "......2312521326622612222.\n" +
                "......34151233222141114141\n" +
                "....52--*****-----------**\n" +
                "...143---*-****-------***-\n" +
                "...252---**--*****---**---\n" +
                "....37----***--*******----\n" +
                "...531----*****-***-*-----\n" +
                "...813--********-*-***----\n" +
                "..3412***----****-*-**----\n" +
                ".24133**-****--*-***-***--\n" +
                "...852-********-*****--**-\n" +
                "..5161---*****-*-******-*-\n" +
                "..6223--******-**-**--***-\n" +
                "..2321-**-----***-**----*-\n" +
                "..2443**-****-****-***----\n" +
                "..6142******-*-****--**---\n" +
                "211231**--*--*-**-***-*---\n" +
                "..2322---**-***-**---**---\n" +
                ".21221---**-*-**-**---*---\n" +
                "..1221---*-**--**-*-------\n" +
                "....33---***----***-------\n" +
                "....21--**--------*-------\n");
    }

    @Test
    public void shouldParse_whenPixels_andNoNumbers_level10() {
        level = new Level10WithoutNumbers();

        assertB("........1.................\n" +
                "........23122.2.34...2....\n" +
                "........114332321321432...\n" +
                "........2444431312612111..\n" +
                "......2312521326622612222.\n" +
                "......34151233222141114141\n" +
                "....52--*****-----------**\n" +
                "...143---*-****-------***-\n" +
                "...252---**--*****---**---\n" +
                "....37----***--*******----\n" +
                "...531----*****-***-*-----\n" +
                "...813--********-*-***----\n" +
                "..3412***----****-*-**----\n" +
                ".24133**-****--*-***-***--\n" +
                "...852-********-*****--**-\n" +
                "..5161---*****-*-******-*-\n" +
                "..6223--******-**-**--***-\n" +
                "..2321-**-----***-**----*-\n" +
                "..2443**-****-****-***----\n" +
                "..6142******-*-****--**---\n" +
                "211231**--*--*-**-***-*---\n" +
                "..2322---**-***-**---**---\n" +
                ".21221---**-*-**-**---*---\n" +
                "..1221---*-**--**-*-------\n" +
                "....33---***----***-------\n" +
                "....21--**--------*-------\n");
    }

    @Ignore // TODO реализовать решалку
    @Test
    public void shouldParse_whenNoPixels_andNumbers() {
        level = new Level1WithoutPixels();

        assertB("...........\n" +
                "..467777764\n" +
                "22-**---**-\n" +
                "44****-****\n" +
                ".9*********\n" +
                ".9*********\n" +
                ".9*********\n" +
                ".7-*******-\n" +
                ".5--*****--\n" +
                ".3---***---\n" +
                ".1----*----\n");
    }

    private void assertB(String expected) {
        String board = (String) new PrinterFactoryImpl<>().getPrinter(level.reader(), null).print();
        assertEquals(expected, board);
    }

}
