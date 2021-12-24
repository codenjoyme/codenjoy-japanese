package com.codenjoy.dojo.japanese.services;

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


import com.codenjoy.dojo.japanese.TestGameSettings;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.event.ScoresImpl;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.japanese.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    public void lose() {
        scores.event(Event.LOSE);
    }

    public void win() {
        scores.event(Event.WIN);
    }

    public void valid() {
        scores.event(Event.VALID);
    }

    public void invalid() {
        scores.event(Event.INVALID);
    }

    @Before
    public void setup() {
        settings = new TestGameSettings();
        givenScores(0);
    }

    @Test
    public void shouldCollectScores() {
        // given
        givenScores(140);

        // when
        valid();
        valid();
        valid();

        invalid();
        invalid();
        invalid();
        invalid();

        win();
        win();

        lose();

        // then
        assertEquals(140
                    + 3 * settings.integer(VALID_PIXEL_SCORE)
                    + 4 * settings.integer(INVALID_PIXEL_PENALTY)
                    + 2 * settings.integer(WIN_SCORE)
                    + settings.integer(LOSE_PENALTY),
                scores.getScore());
    }

    private void givenScores(int score) {
        scores = new ScoresImpl<>(score, new Scores(settings));
    }

    @Test
    public void shouldStillZeroAfterDead() {
        // given
        givenScores(0);

        // when
        lose();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        // given
        givenScores(0);
        win();

        // when
        scores.clear();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldLose() {
        // given
        givenScores(1400);

        // when
        lose();
        lose();

        // then
        assertEquals(1400
                    + 2 * settings.integer(LOSE_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldWin() {
        // given
        givenScores(140);

        // when
        win();
        win();

        // then
        assertEquals(140
                    + 2 * settings.integer(WIN_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldValidPixel() {
        // given
        givenScores(140);

        // when
        valid();
        valid();

        // then
        assertEquals(140
                    + 2 * settings.integer(VALID_PIXEL_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldInvalidPixel() {
        // given
        givenScores(140);

        // when
        invalid();
        invalid();

        // then
        assertEquals(140
                    + 2 * settings.integer(INVALID_PIXEL_PENALTY),
                scores.getScore());
    }
}