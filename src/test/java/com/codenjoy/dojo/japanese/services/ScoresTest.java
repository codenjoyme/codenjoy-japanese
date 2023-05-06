package com.codenjoy.dojo.japanese.services;

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


import com.codenjoy.dojo.japanese.TestGameSettings;
import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.utils.scorestest.AbstractScoresTest;
import org.junit.Test;

import static com.codenjoy.dojo.japanese.services.GameSettings.Keys.*;

public class ScoresTest extends AbstractScoresTest {

    @Override
    public GameSettings settings() {
        return new TestGameSettings();
    }

    @Override
    protected Class<? extends ScoresMap<?>> scores() {
        return Scores.class;
    }

    @Override
    protected Class<? extends Enum> eventTypes() {
        return Event.class;
    }

    @Test
    public void shouldCollectScores() {
        assertEvents("100:\n" +
                "VALID > +10 = 110\n" +
                "VALID > +10 = 120\n" +
                "VALID > +10 = 130\n" +
                "INVALID > -1 = 129\n" +
                "INVALID > -1 = 128\n" +
                "INVALID > -1 = 127\n" +
                "INVALID > -1 = 126\n" +
                "WIN > +100 = 226\n" +
                "WIN > +100 = 326\n" +
                "LOSE > -100 = 226");
    }

    @Test
    public void shouldNotLessThanZero() {
        assertEvents("0:\n" +
                "LOSE > +0 = 0");
    }

    @Test
    public void shouldCleanScore() {
        assertEvents("100:\n" +
                "WIN > +100 = 200\n" +
                "WIN > +100 = 300\n" +
                "(CLEAN) > -300 = 0\n" +
                "WIN > +100 = 100\n" +
                "WIN > +100 = 200");
    }

    @Test
    public void shouldCollectScores_whenWin() {
        // given
        settings.integer(WIN_SCORE, 100);

        // when then
        assertEvents("100:\n" +
                "WIN > +100 = 200\n" +
                "WIN > +100 = 300");
    }

    @Test
    public void shouldCollectScores_whenLose() {
        // given
        settings.integer(LOSE_PENALTY, -100);

        // when then
        assertEvents("100:\n" +
                "LOSE > -100 = 0\n" +
                "LOSE > +0 = 0");
    }

    @Test
    public void shouldCollectScores_whenValidPixel() {
        // given
        settings.integer(VALID_PIXEL_SCORE, 10);

        // when then
        assertEvents("100:\n" +
                "VALID > +10 = 110\n" +
                "VALID > +10 = 120");
    }

    @Test
    public void shouldCollectScores_whenInvalidPixel() {
        // given
        settings.integer(INVALID_PIXEL_PENALTY, -1);

        // when then
        assertEvents("100:\n" +
                "INVALID > -1 = 99\n" +
                "INVALID > -1 = 98");
    }
}