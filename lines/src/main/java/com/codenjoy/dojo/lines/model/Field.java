package com.codenjoy.dojo.lines.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.lines.model.items.Ball;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GameField;

import java.util.List;

/**
 * Так случилось что доска знает про героя, а герой про доску.
 * И чтобы герой не знал про всю доску, я ему даю вот эту часть доски.
 */
public interface Field extends GameField<Player> {

    Point getFreeRandom();

 //   boolean isInLine(int x, int y);

    void setBall(Elements color, int x, int y);

    void removeBall(int x, int y);

    List<Ball> getBalls();

    //void setBall(Elements color, int x, int y);

    //void removeBall(int x, int y);
}
