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


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.japanese.client.Board;
import com.codenjoy.dojo.japanese.client.ai.AISolver;
import com.codenjoy.dojo.japanese.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.settings.Parameter;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType implements GameType {

    private final Level level;

    public GameRunner() {
        new Scores(0, settings);
        level = new LevelImpl(getMap());
    }

    protected String getMap() {
        return  "........1................." +
                "........23122.2.34...2...." +
                "........114332321321432..." +
                "........2444431312612111.." +
                "......2312521326622612222." +
                "......34151233222141114141" +
                "....52--*****-----------**" +
                "...143---*-****-------***-" +
                "...252---**--*****---**---" +
                "....37----***--*******----" +
                "...531----*****-***-*-----" +
                "...813--********-*-***----" +
                "..3412***----****-*-**----" +
                ".24133**-****--*-***-***--" +
                "...852-********-*****--**-" +
                "..5161---*****-*-******-*-" +
                "..6223--******-**-**--***-" +
                "..2321-**-----***-**----*-" +
                "..2443**-****-****-***----" +
                "..6142******-*-****--**---" +
                "211231**--*--*-**-***-*---" +
                "..2322---**-***-**---**---" +
                ".21221---**-*-**-**---*---" +
                "..1221---*-**--**-*-------" +
                "....33---***----***-------" +
                "....21--**--------*-------";
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer)score, settings);
    }

    @Override
    public GameField createGame(int levelNumber) {
        return new Japanese(level, getDice());
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String name() {
        return "japanese";
    }

    @Override
    public CharElements[] getPlots() {
        return Elements.values();
    }

    @Override
    public Class<? extends Solver> getAI() {
        return AISolver.class;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return Board.class;
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        return MultiplayerType.SINGLE;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId) {
        return new Player(listener);
    }
}
