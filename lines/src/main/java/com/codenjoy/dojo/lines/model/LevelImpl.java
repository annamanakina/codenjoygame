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
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import org.omg.CORBA.BAD_CONTEXT;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Полезный утилитный класс для получения объектов на поле из текстового вида.
 */
public class LevelImpl implements Level {
    private final LengthToXY xy;

    private String map;

    public LevelImpl(String map) {
        this.map = map;
        xy = new LengthToXY(getSize());
    }

    /*public LevelImpl(int size) {
        this.map = fillFieldRandom(size);
        xy = new LengthToXY(getSize());
    }*/

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }


    /*public String fillFieldRandom(int size) {
        Elements [] elements = Elements.values();
        int count=0;
        StringBuilder stringBuilder = new StringBuilder();

        do {
            int index = new Random().nextInt(elements.length);
            if (index == 0) continue;
            stringBuilder.append(elements[index]);
            count++;
            System.out.println(elements[index]+" " + count);

        } while(count<size);
        System.out.print("\r\n"+count);

        return stringBuilder.toString();
    }*/

    @Override
    public List<Ball> getBalls() {
      //  List<Ball> list = new LinkedList<>();

        return new LinkedList<Ball>() {{
            addAll(pointsOf(Elements.BLUE).stream()
                    .map(pt -> new Ball(Elements.BLUE, pt)).collect(Collectors.toList()));
            addAll(pointsOf(Elements.GREEN).stream()
                    .map(pt -> new Ball(Elements.GREEN, pt)).collect(Collectors.toList()));
            addAll(pointsOf(Elements.RED).stream()
                    .map(pt -> new Ball(Elements.RED, pt)).collect(Collectors.toList()));
            addAll(pointsOf(Elements.WHITE).stream()
                    .map(pt -> new Ball(Elements.WHITE, pt)).collect(Collectors.toList()));
            addAll(pointsOf(Elements.YELLOW).stream()
                    .map(pt -> new Ball(Elements.YELLOW, pt)).collect(Collectors.toList()));
            addAll(pointsOf(Elements.NONE).stream()
                    .map(pt -> new Ball(Elements.NONE, pt)).collect(Collectors.toList()));
        }
    };
    }


    private List<Point> pointsOf(Elements element) {
        List<Point> result = new LinkedList<>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == element.ch) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }
}
