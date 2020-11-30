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


import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.japanese.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class JapaneseTest {

    private Japanese game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);

        game = new Japanese(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        this.hero = player.getHero();
        hero.init(game);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    private void assertNotE(String expected) {
        assertNotEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldEmptyField_whenStart() {
        // given when
        givenFl("........" +
                "....1.1." +
                "...01100" +
                "..0-----" +
                ".11-*-*-" +
                "..0-----" +
                "..3-***-" +
                "..0-----");

        // then
        assertE("........" +
                "....1.1." +
                "...01100" +
                "..0     " +
                ".11     " +
                "..0     " +
                "..3     " +
                "..0     ");
    }

    @Test
    public void shouldValid_whenGuessedWhite() {
        // given
        shouldEmptyField_whenStart();

        assertE("........" +
                "....1.1." +
                "...01100" +
                "..0     " +
                ".11     " +
                "..0     " +
                "..3     " +
                "..0     ");

        // when
        hero.act(3, 0, Elements.WHITE.code());
        game.tick();

        // then
        verify(listener).event(Events.VALID);
        verifyNoMoreInteractions(listener);

        assertE("........" +
                "....1.1." +
                "...01100" +
                "..0     " +
                ".11     " +
                "..0     " +
                "..3     " +
                "..0-    ");
    }

    @Test
    public void shouldValid_whenGuessedBlack() {
        // given
        shouldEmptyField_whenStart();

        assertE("........" +
                "....1.1." +
                "...01100" +
                "..0     " +
                ".11     " +
                "..0     " +
                "..3     " +
                "..0     ");

        // when
        hero.act(4, 3, Elements.BLACK.code());
        game.tick();

        // then
        verify(listener).event(Events.VALID);
        verifyNoMoreInteractions(listener);

        assertE("........" +
                "....1.1." +
                "...01100" +
                "..0     " +
                ".11 *   " +
                "..0     " +
                "..3     " +
                "..0     ");
    }

    @Test
    public void shouldInvalid_whenDidNotGuessWhite() {
        // given
        shouldEmptyField_whenStart();

        assertE("........" +
                "....1.1." +
                "...01100" +
                "..0     " +
                ".11     " +
                "..0     " +
                "..3     " +
                "..0     ");

        // when
        hero.act(3, 0, Elements.BLACK.code());
        game.tick();

        // then
        verify(listener).event(Events.INVALID);
        verifyNoMoreInteractions(listener);

        assertE("........" +
                "....1.1." +
                "...01100" +
                "..0     " +
                ".11     " +
                "..0     " +
                "..3     " +
                "..0*    ");
    }

    @Test
    public void shouldInvalid_whenDidNotGuessBlack() {
        // given
        shouldEmptyField_whenStart();

        assertE("........" +
                "....1.1." +
                "...01100" +
                "..0     " +
                ".11     " +
                "..0     " +
                "..3     " +
                "..0     ");

        // when
        hero.act(4, 3, Elements.WHITE.code());
        game.tick();

        // then
        verify(listener).event(Events.INVALID);
        verifyNoMoreInteractions(listener);

        assertE("........" +
                "....1.1." +
                "...01100" +
                "..0     " +
                ".11 -   " +
                "..0     " +
                "..3     " +
                "..0     ");
    }

    @Test
    public void shouldIgnore_whenBadPoint() {
        // given
        shouldEmptyField_whenStart();

        assertE("........" +
                "....1.1." +
                "...01100" +
                "..0     " +
                ".11     " +
                "..0     " +
                "..3     " +
                "..0     ");

        // when then
        int valid = 3;

        assertActIgnored(valid, game.size() + 1);       // y больше размера всей борды
        assertActProcessed(valid, game.size() - 1 - 3); // в пределах поля
        assertActIgnored(game.size() + 1, valid);       // x больше размера всей борды
        assertActProcessed(game.size() - 1, valid);     // в пределах поля
        assertActIgnored(-1, valid);          // x меньше 0
        assertActIgnored(valid, -1);          // y меньше 0
        assertActProcessed(valid, 4);         // в пределах поля
        assertActIgnored(valid, 5);           // y в области числовой части борды
        assertActIgnored(valid, 6);           // y в области числовой части борды
        assertActIgnored(valid, 7);           // y в области числовой части борды
        assertActIgnored(0, valid);           // x в области числовой части борды
        assertActIgnored(1, valid);           // x в области числовой части борды
        assertActIgnored(2, valid);           // x в области числовой части борды
        assertActProcessed(3, valid);         // в пределах поля
        assertActIgnored(2, 5);               // x&y в области числовой части борды
    }

    private void assertActIgnored(int x, int y) {
        // when
        hero.act(x, y, Elements.WHITE.code());
        game.tick();

        // then
        assertE("........" +
                "....1.1." +
                "...01100" +
                "..0     " +
                ".11     " +
                "..0     " +
                "..3     " +
                "..0     ");
    }

    private void assertActProcessed(int x, int y) {
        // when
        hero.act(x, y, Elements.WHITE.code());
        game.tick();

        // then
        assertNotE(
                "........" +
                "....1.1." +
                "...01100" +
                "..0     " +
                ".11     " +
                "..0     " +
                "..3     " +
                "..0     ");

        // clear
        hero.act(x, y, Elements.UNSET.code());
    }

}
