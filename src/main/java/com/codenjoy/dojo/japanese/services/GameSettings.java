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


import com.codenjoy.dojo.japanese.model.level.Level;
import com.codenjoy.dojo.japanese.model.level.LevelImpl;
import com.codenjoy.dojo.japanese.model.level.Levels;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.japanese.services.GameSettings.Keys.*;

public final class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements Key {

        LEVELS_COUNT("Levels count"),
        LOOSE_PENALTY("Loose penalty"),
        WIN_SCORE("Win score"),
        INVALID_PIXEL_PENALTY("Invalid pixel penalty"),
        VALID_PIXEL_SCORE("Valid pixel score");

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    public GameSettings() {
        addEditBox(VALID_PIXEL_SCORE.key()).type(Integer.class).def(10);
        addEditBox(INVALID_PIXEL_PENALTY.key()).type(Integer.class).def(1);
        addEditBox(WIN_SCORE.key()).type(Integer.class).def(1000);
        addEditBox(LOOSE_PENALTY.key()).type(Integer.class).def(1000);

        addEditBox(LEVELS_COUNT.key()).type(Integer.class).def(0);
        Levels.setup(this);
    }

    public GameSettings addLevel(int index, Level level) {
        integer(LEVELS_COUNT, index);

        String prefix = levelPrefix(index);
        addEditBox(prefix).multiline().type(String.class).def(level.map());
        return this;
    }

    public String levelMap(int index) {
        String prefix = levelPrefix(index);
        return addEditBox(prefix).type(String.class).getValue();
    }

    private String levelPrefix(int index) {
        return "Levels" + index;
    }

    public int size() {
        // TODO надо сделать чтобы разные уровни имели разные размеры
        // без рефакторинга в codenjoy не справиться
        return new LevelImpl(levelMap(1)).size();
    }

    public Level getLevel(int levelNumber) {
        return new LevelImpl(levelMap(levelNumber));
    }


}
