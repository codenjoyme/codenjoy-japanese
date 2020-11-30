package com.codenjoy.dojo.japanese.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.japanese.model.LevelImpl;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

public final class SettingsWrapper {

    public static SettingsWrapper data;

    private final Parameter<Integer> winScore;
    private final Parameter<Integer> loosePenalty;
    private final Parameter<Integer> validScore;
    private final Parameter<Integer> invalidPenalty;
    private final Parameter<String> levelMap;

    private final Settings settings;

    public static SettingsWrapper setup(Settings settings) {
        return new SettingsWrapper(settings);
    }

    // for testing
    public static SettingsWrapper setup() {
        return setup(new SettingsImpl());
    }

    private SettingsWrapper(Settings settings) {
        data = this;
        this.settings = settings;

        validScore = settings.addEditBox("Valid pixel score").type(Integer.class).def(10);
        invalidPenalty = settings.addEditBox("Invalid pixel penalty").type(Integer.class).def(1);
        winScore = settings.addEditBox("Win score").type(Integer.class).def(1000);
        loosePenalty = settings.addEditBox("Loose penalty").type(Integer.class).def(100);

        levelMap = settings.addEditBox("Level map").multiline().type(String.class)
                .def("........1................." +
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
                    "....21--**--------*-------");
    }

    public int loosePenalty() {
        return loosePenalty.getValue();
    }

    public int winScore() {
        return winScore.getValue();
    }

    public int invalidPenalty() {
        return invalidPenalty.getValue();
    }

    public int validScore() {
        return validScore.getValue();
    }

    public String levelMap() {
        return levelMap.getValue();
    }

    public int getSize() {
        return new LevelImpl(levelMap()).getSize(); // TODO а что если уровней несколько?
    }

    // setters for testing

    public SettingsWrapper loosePenalty(int value) {
        loosePenalty.update(value);
        return this;
    }

    public SettingsWrapper winScore(int value) {
        winScore.update(value);
        return this;
    }

    public SettingsWrapper invalidPenalty(int value) {
        invalidPenalty.update(value);
        return this;
    }

    public SettingsWrapper validScore(int value) {
        validScore.update(value);
        return this;
    }

    public SettingsWrapper levelMap(String value) {
        levelMap.update(value);
        return this;
    }

}
