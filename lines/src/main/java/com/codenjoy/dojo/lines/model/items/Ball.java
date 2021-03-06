package com.codenjoy.dojo.lines.model.items;

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


import com.codenjoy.dojo.lines.model.Elements;
import com.codenjoy.dojo.lines.model.Player;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Артефакт: Бомба на поле
 */
public class Ball extends PointImpl implements State<Elements, Player> {

    private Elements color;

    //TODO вернуться к color, может она не так должна иниц.
    public Ball(Elements color, int x, int y) {
        super(x, y);
        this.color = color;
    }

    public Ball(Elements color, Point point) {
        super(point);
        this.color = color;
    }

    public Elements getColor() {
        return color;
    }

    public void setColor(Elements color) {
        this.color = color;
    }

    public boolean isSameColor(Ball anotherBall) {
        return this.color == anotherBall.color;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (color == Elements.BLUE)
            return Elements.BLUE;
        else if (color == Elements.GREEN)
            return Elements.GREEN;
        else if (color == Elements.NONE)
            return Elements.NONE;
        else if (color == Elements.RED)
            return Elements.RED;
        else if (color == Elements.WHITE)
            return Elements.WHITE;
        else if (color == Elements.YELLOW)
            return Elements.YELLOW;
        return null;
    }
}
