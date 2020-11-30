package com.codenjoy.dojo.japanese.model.level.levels;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

public class Level1WithoutPixels extends LevelImpl implements Level {

    public Level1WithoutPixels() {
        super(
                "........1................." +
                "........23122.2.34...2...." +
                "........114332321321432..." +
                "........2444431312612111.." +
                "......2312521326622612222." +
                "......34151233222141114141" +
                "....52                    " +
                "...143                    " +
                "...252                    " +
                "....37                    " +
                "...531                    " +
                "...813                    " +
                "..3412                    " +
                ".24133                    " +
                "...852                    " +
                "..5161                    " +
                "..6223                    " +
                "..2321                    " +
                "..2443                    " +
                "..6142                    " +
                "211231                    " +
                "..2322                    " +
                ".21221                    " +
                "..1221                    " +
                "....33                    " +
                "....21                    "
        );
    }
}
