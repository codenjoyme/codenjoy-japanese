package com.codenjoy.dojo.japanese;

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


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.japanese.client.Board;
import com.codenjoy.dojo.japanese.client.ai.AISolver;
import com.codenjoy.dojo.japanese.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SmokeTest {

    private Dice dice;

    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 10;

        Dice dice = LocalGameRunner.getDice(
                0, 1, 2, 3, 4, // random numbers
                0, 2, 2, 3, 1,
                0, 1, 4, 1, 2,
                1, 3, 2, 4, 1,
                1, 3, 0, 0, 1,
                0, 0, 1, 0, 2,
                1, 3, 2, 4, 1,
                0, 1, 1, 3, 3,
                0, 2, 0, 0, 2,
                0, 1, 2, 2, 1,
                1, 3, 4, 3, 4,
                1, 3, 2, 4, 1,
                0, 1, 3, 1, 1);

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new LinkedList<Solver>(){{
                    add(new AISolver(dice));
                    add(new AISolver(dice));
                }},
                new LinkedList<>(){{
                    add(new Board());
                    add(new Board());
                }});

        // then
        assertEquals("1:Board:\n" +
                        "1:...........\n" +
                        "1:..467777764\n" +
                        "1:22         \n" +
                        "1:44         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.7         \n" +
                        "1:.5         \n" +
                        "1:.3         \n" +
                        "1:.1         \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(0,1,1)\n" +
                        "2:Board:\n" +
                        "2:...........\n" +
                        "2:..467777764\n" +
                        "2:22         \n" +
                        "2:44         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.7         \n" +
                        "2:.5         \n" +
                        "2:.3         \n" +
                        "2:.1         \n" +
                        "2:\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE:0\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: ACT(3,4,-1)\n" +
                        "2:Fire Event: INVALID\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:...........\n" +
                        "1:..467777764\n" +
                        "1:22         \n" +
                        "1:44         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.7         \n" +
                        "1:.5         \n" +
                        "1:.3         \n" +
                        "1:.1         \n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(2,2,-1)\n" +
                        "2:Board:\n" +
                        "2:...........\n" +
                        "2:..467777764\n" +
                        "2:22         \n" +
                        "2:44         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.7         \n" +
                        "2:.5         \n" +
                        "2:.3         \n" +
                        "2:.1         \n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: ACT(1,0,0)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:...........\n" +
                        "1:..467777764\n" +
                        "1:22         \n" +
                        "1:44         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.7         \n" +
                        "1:.5         \n" +
                        "1:.3         \n" +
                        "1:.1         \n" +
                        "1:\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(4,1,1)\n" +
                        "2:Board:\n" +
                        "2:...........\n" +
                        "2:..467777764\n" +
                        "2:22         \n" +
                        "2:44         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.7         \n" +
                        "2:.5         \n" +
                        "2:.3         \n" +
                        "2:.1         \n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: ACT(1,3,1)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:...........\n" +
                        "1:..467777764\n" +
                        "1:22         \n" +
                        "1:44         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.7         \n" +
                        "1:.5         \n" +
                        "1:.3         \n" +
                        "1:.1         \n" +
                        "1:\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(4,1,0)\n" +
                        "2:Board:\n" +
                        "2:...........\n" +
                        "2:..467777764\n" +
                        "2:22         \n" +
                        "2:44         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.7         \n" +
                        "2:.5         \n" +
                        "2:.3         \n" +
                        "2:.1         \n" +
                        "2:\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: ACT(3,0,-1)\n" +
                        "2:Fire Event: INVALID\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:...........\n" +
                        "1:..467777764\n" +
                        "1:22         \n" +
                        "1:44         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.7         \n" +
                        "1:.5         \n" +
                        "1:.3         \n" +
                        "1:.1         \n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(1,0,-1)\n" +
                        "2:Board:\n" +
                        "2:...........\n" +
                        "2:..467777764\n" +
                        "2:22         \n" +
                        "2:44         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.7         \n" +
                        "2:.5         \n" +
                        "2:.3         \n" +
                        "2:.1         \n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: ACT(1,0,1)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:...........\n" +
                        "1:..467777764\n" +
                        "1:22         \n" +
                        "1:44         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.7         \n" +
                        "1:.5         \n" +
                        "1:.3         \n" +
                        "1:.1         \n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(1,3,1)\n" +
                        "2:Board:\n" +
                        "2:...........\n" +
                        "2:..467777764\n" +
                        "2:22         \n" +
                        "2:44         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.7         \n" +
                        "2:.5         \n" +
                        "2:.3         \n" +
                        "2:.1         \n" +
                        "2:\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: ACT(4,1,-1)\n" +
                        "2:Fire Event: INVALID\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:...........\n" +
                        "1:..467777764\n" +
                        "1:22         \n" +
                        "1:44         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.7         \n" +
                        "1:.5         \n" +
                        "1:.3         \n" +
                        "1:.1         \n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(1,1,-1)\n" +
                        "2:Board:\n" +
                        "2:...........\n" +
                        "2:..467777764\n" +
                        "2:22         \n" +
                        "2:44         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.7         \n" +
                        "2:.5         \n" +
                        "2:.3         \n" +
                        "2:.1         \n" +
                        "2:\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: ACT(3,0,1)\n" +
                        "2:Fire Event: VALID\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:...........\n" +
                        "1:..467777764\n" +
                        "1:22         \n" +
                        "1:44         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.7         \n" +
                        "1:.5         \n" +
                        "1:.3         \n" +
                        "1:.1 -       \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(0,0,1)\n" +
                        "2:Board:\n" +
                        "2:...........\n" +
                        "2:..467777764\n" +
                        "2:22         \n" +
                        "2:44         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.7         \n" +
                        "2:.5         \n" +
                        "2:.3         \n" +
                        "2:.1 -       \n" +
                        "2:\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "2:Scores: 10\n" +
                        "2:Answer: ACT(0,1,1)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:...........\n" +
                        "1:..467777764\n" +
                        "1:22         \n" +
                        "1:44         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.7         \n" +
                        "1:.5         \n" +
                        "1:.3         \n" +
                        "1:.1 -       \n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(2,1,0)\n" +
                        "2:Board:\n" +
                        "2:...........\n" +
                        "2:..467777764\n" +
                        "2:22         \n" +
                        "2:44         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.7         \n" +
                        "2:.5         \n" +
                        "2:.3         \n" +
                        "2:.1 -       \n" +
                        "2:\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "2:Scores: 10\n" +
                        "2:Answer: ACT(3,4,-1)\n" +
                        "2:Fire Event: INVALID\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:...........\n" +
                        "1:..467777764\n" +
                        "1:22         \n" +
                        "1:44         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.9         \n" +
                        "1:.7         \n" +
                        "1:.5         \n" +
                        "1:.3         \n" +
                        "1:.1 -       \n" +
                        "1:\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(4,1,-1)\n" +
                        "2:Board:\n" +
                        "2:...........\n" +
                        "2:..467777764\n" +
                        "2:22         \n" +
                        "2:44         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.9         \n" +
                        "2:.7         \n" +
                        "2:.5         \n" +
                        "2:.3         \n" +
                        "2:.1 -       \n" +
                        "2:\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "2:Scores: 9\n" +
                        "2:Answer: ACT(2,4,0)\n" +
                        "2:Fire Event: VALID\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
