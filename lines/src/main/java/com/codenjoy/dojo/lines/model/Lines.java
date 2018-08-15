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
import com.codenjoy.dojo.lines.services.Events;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;

import javax.lang.model.element.Element;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее,
 * то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о
 * каждом тике игры. Обрати внимание на {Sample#tick()}
 */
public class Lines implements Field {

    private List<Ball> balls;
    private List<Player> players; //игрок один у нас будет
    // private Level level;

    private final int size;
    private Dice dice;

    public Lines(Level level, Dice dice) {
        this.dice = dice;
        size = level.getSize();
        players = new LinkedList<>();
        balls = level.getBalls();
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        for (Player player : players) {
            Hero hero = player.getHero();
            hero.tick();
        }
    }

    public int size() {
        return size;
    }

    @Override
    public String fillFieldRandom() {
        Elements[] elements = Elements.values();
        int count = 0;
        StringBuilder stringBuilder = new StringBuilder();

        do {
            int index = new Random().nextInt(elements.length);
            if (index == 0) continue;
            stringBuilder.append(elements[index]);
            count++;
            System.out.println(elements[index] + " " + count);

        } while (count < balls.size());
        System.out.print("\r\n" + count);

        return stringBuilder.toString();
    }

    /* @Override
    public Point getFreeRandom() {
        int x;
        int y;
        int c = 0;
        do {
            x = dice.next(size);
            y = dice.next(size);
            System.out.println(x + ", "+y);
        } while (!isFree(x, y) && c++ < 100);

        if (c >= 100) {
            return pt(0, 0);
        }

        return pt(x, y);
    }

    @Override
    public boolean isFree(int x, int y) {
        Point pt = pt(x, y);
        return !balls.contains(pt);
  }*/

    private void changeColor(Ball first, Ball next) {
        Elements firstColor = first.getColor();
        // System.out.println(" changeColor first " + first);
        Elements nextColor = next.getColor();
        // System.out.println("changeColor currentBall " + next);
        balls.get(balls.indexOf(next)).setColor(firstColor);
        balls.get(balls.indexOf(first)).setColor(nextColor);

        //TODO burn line
        //burnLine(next);
    }

    private void burnLineRightHorizontal(Ball startBall) {
        //System.out.println("burnLine startBall " + startBall + ", " + startBall.getColor());
        int i = startBall.getX();
        int j = startBall.getY();
        Elements newColor;

        for (int m = i; m < i + 3; m++) {

            int n = j;
            for (++n; n < 10; n++) {
                Point pt = pt(m, n);
                newColor = getBall(pt).getColor();
                getBall(pt(m, n - 1)).setColor(newColor);
                if (n == 9) {
                    getBall(pt(m, n)).setColor(Elements.NONE); //TODO replace later for random
                }
            }
        }
    }

    private void burnLineLeftHorizontal(Ball startBall) {
        Point leftPT = Direction.LEFT.change(startBall);
        Point startPT = Direction.LEFT.change(leftPT);

        int startY = startPT.getY();

        Elements newColor;

      for (startY = startY+1; startY < 10; startY++){
          int startX = startPT.getX();
          int endX = startBall.getX();
          for ( ; startX < endX + 1; startX++){
              Point pt = pt(startX, startY);
              newColor = getBall(pt).getColor();
              getBall(pt(startX, startY - 1)).setColor(newColor);
              if (startY == 9) {
                  getBall(pt(startX, startY)).setColor(Elements.NONE); //TODO replace later for random
              }
          }
      }
    }


    //если три равны между, вертикально
    private void burnLineBetweenVertical(Ball startBall) {
        //System.out.println("burnLine startBall " + startBall + ", " + startBall.getColor());
        int i = startBall.getX();
        int j = startBall.getY();

        Elements newColor;

        int n = j + 2;
        int index = 0;
        for (; n < 10; n++) {
            Point pt = pt(i, n);
            newColor = getBall(pt).getColor();
            index = n-3;
            getBall(pt(i, index)).setColor(newColor);
        }

        ++index;
        for (; index < 10; index++) {
            getBall(pt(i, index)).setColor(Elements.NONE); //TODO replace later for random
        }
    }

    //если три равны вертикально вверх
    private void burnLineUpVertical(Ball startBall) {
        //System.out.println("burnLine startBall " + startBall + ", " + startBall.getColor());
        int i = startBall.getX();
        int j = startBall.getY();

        Elements newColor;

        //берем второй верхний элемент от начального
        int n = j + 3;
        int index = 0;
        System.out.println("burnLineUpVertical n " + n);

        if (n == 10) {
            for (; j < 10; j++) {
                getBall(pt(i, j)).setColor(Elements.NONE); //TODO replace later for random
            }

        } else {
            for (; n < 10; n++) {
                Point pt = pt(i, n);
                newColor = getBall(pt).getColor();
                index = n - 3;
                getBall(pt(i, index)).setColor(newColor);
                System.out.println("burnLineUpVertical index " + index);
              /*  if (n == 9) {
                    n = n-3;
                    for (; n <10; n++){
                    getBall(pt(i, n)).setColor(Elements.NONE); //TODO replace later for random
                    }
                }*/
            }
            ++index;
            for (; index < 10; index++) {
                getBall(pt(i, index)).setColor(Elements.NONE);
            }
        }
        //System.out.println("burnLineUpVertical index " + ++index);

    }

    //если три равны вертикально вниз
    private void burnLineDownVertical(Ball startBall) {
        //System.out.println("burnLine startBall " + startBall + ", " + startBall.getColor());
        int i = startBall.getX();
        int j = startBall.getY();
        j++;

        Elements newColor;

        int index = 0;
            for (; j < 10; j++) {
                Point pt = pt(i, j);
                newColor = getBall(pt).getColor();
                index = j - 3;
                System.out.println("burnLineDownVertical index " + index);
                getBall(pt(i, index)).setColor(newColor);
            }
            ++index;
            for (; index < 10; index++) {
                getBall(pt(i, index)).setColor(Elements.NONE); //TODO replace later for random
            }
    }


    private void burnLineBetweenHorizontal(Ball startBall){
        Point startPT = Direction.LEFT.change(startBall);
        Point endPT = Direction.RIGHT.change(startBall);

        int startX = startPT.getX();
        int endX = endPT.getX();

        int startY = startPT.getY();

        Elements newColor;
        //
        for ( ; startX < ++endX; startX++) {

            for (++startY; startY < 10; startY++) {
                Point pt = pt(startX, startY);
                newColor = getBall(pt).getColor();
                getBall(pt(startX, startY - 1)).setColor(newColor);
                if (startY == 9) {
                    getBall(pt(startX, startY)).setColor(Elements.NONE); //TODO replace later for random
                }
            }
        }
    }



    private boolean isBoader(Point pt) {
        boolean boader = false;
        if (((pt.getX() <= 9) & (pt.getX() >= 0)) & ((pt.getY() <= 9) & (pt.getY() >= 0))) {
            boader = true;
        }
        return boader;
    }

    private Ball getBall(Point pt) {
        return balls.stream()
                .filter(Predicate.isEqual(pt))
                .findFirst().orElseThrow(() -> new UnsupportedOperationException()); //TODO Exception
    }

    private boolean isBallsEqualBetweenVertical(Ball currentBall, Point pt) {
        boolean isEqual = false;
        Point firstPT = Direction.UP.change(pt);
        if (isBoader(firstPT)) {
            Ball firstBall = getBall(firstPT);
            Point nextPT = Direction.DOWN.change(pt);
            if (isBoader(nextPT)) {
                Ball nextBall = getBall(nextPT);
                isEqual = currentBall.isSameColor(firstBall) & firstBall.isSameColor(nextBall);
            }
        }
        return isEqual;
    }

    private boolean isBallsEqualBetweenHorizontal(Ball currentBall, Point pt) {
        boolean isEqual = false;
        Point firstPT = Direction.LEFT.change(pt);
        if (isBoader(firstPT)) {
            Ball firstBall = getBall(firstPT);
            Point nextPT = Direction.RIGHT.change(pt);
            if (isBoader(nextPT)) {
                Ball nextBall = getBall(nextPT);
                isEqual = currentBall.isSameColor(firstBall) & firstBall.isSameColor(nextBall);
            }
        }
        return isEqual;
    }

    private boolean isNextTwoLeftBallsEqualHorizontal(Ball currentBall, Point pt) {
        boolean isEqual = false;
        Point firstPT = Direction.LEFT.change(pt);
        if (isBoader(firstPT)) {
            Ball firstBall = getBall(firstPT);
            Point nextPT = Direction.LEFT.change(firstPT);
            if (isBoader(nextPT)) {
                Ball nextBall = getBall(nextPT);
                isEqual = currentBall.isSameColor(firstBall) & firstBall.isSameColor(nextBall);
            }
        }
        return isEqual;
    }

    private boolean isNextTwoRightBallsEqualHorizontal(Ball currentBall, Point pt) {
        boolean isEqual = false;
        Point firstPT = Direction.RIGHT.change(pt);
        if (isBoader(firstPT)) {
            Ball firstBall = getBall(firstPT);
            Point nextPT = Direction.RIGHT.change(firstPT);
            if (isBoader(nextPT)) {
                Ball nextBall = getBall(nextPT);
                isEqual = currentBall.isSameColor(firstBall) & firstBall.isSameColor(nextBall);
            }
        }
        return isEqual;
    }

    private boolean isNextTwoBallsEqualUpVertical(Ball currentBall, Point pt) {
        boolean isEqual = false;
        Point firstPT = Direction.UP.change(pt);
        if (isBoader(firstPT)) {
            Ball firstBall = getBall(firstPT);
            Point nextPT = Direction.UP.change(firstPT);
            if (isBoader(nextPT)) {
                Ball nextBall = getBall(nextPT);
                isEqual = currentBall.isSameColor(firstBall) & firstBall.isSameColor(nextBall);
            }
        }
        return isEqual;
    }

    private boolean isNextTwoBallsEqualDownVertical(Ball currentBall, Point pt) {
        boolean isEqual = false;
        Point firstPT = Direction.DOWN.change(pt);
        if (isBoader(firstPT)) {
            Ball firstBall = getBall(firstPT);
            Point nextPT = Direction.DOWN.change(firstPT);
            if (isBoader(nextPT)) {
                Ball nextBall = getBall(nextPT);
                isEqual = currentBall.isSameColor(firstBall) & firstBall.isSameColor(nextBall);
            }
        }
        return isEqual;
    }

    @Override
    public void moveBalls(int x, int y) {
        Point pt = pt(x, y);
        Ball currentBall = getBall(pt);
        //System.out.println("moveBalls currentBall " + currentBall);

        Direction.getValues().forEach(direction -> {
            Point nextPT = direction.change(pt);
            if (isBoader(nextPT)) {
                //if (!nextPT.isOutOf(balls.size())) {
                Ball ball = getBall(nextPT);
                //System.out.println("moveBalls ball " + ball +" " +ball.getColor());
                switch (direction) {
                    case LEFT:
                        if (isBallsEqualBetweenVertical(currentBall, ball)) {
                            changeColor(currentBall, ball);
                            burnLineBetweenVertical(ball);
                        }

                        if (isNextTwoLeftBallsEqualHorizontal(currentBall, ball)) {
                            changeColor(currentBall, ball);
                            burnLineLeftHorizontal(ball);
                        }

                        if (isNextTwoBallsEqualUpVertical(currentBall, ball)) {
                            changeColor(currentBall, ball);
                            burnLineUpVertical(ball);
                        }

                        if (isNextTwoBallsEqualDownVertical(currentBall, ball)) {
                            changeColor(currentBall, ball);
                            burnLineDownVertical(ball);
                        }
                        break;

                    case RIGHT:
                        if (isBallsEqualBetweenVertical(currentBall, ball)) {
                            changeColor(currentBall, ball);
                            //TODO заменить на более общий метод
                            burnLineBetweenVertical(ball);
                        }
                        if (isNextTwoRightBallsEqualHorizontal(currentBall, ball)) {
                            changeColor(currentBall, ball);
                            burnLineRightHorizontal(ball);
                        }

                        if (isNextTwoBallsEqualUpVertical(currentBall, ball)) {
                            changeColor(currentBall, ball);
                            burnLineUpVertical(ball);
                        }

                        if (isNextTwoBallsEqualDownVertical(currentBall, ball)) {
                            changeColor(currentBall, ball);
                            burnLineDownVertical(ball);
                        }

                        break;
                    case UP:
                        if (isBallsEqualBetweenHorizontal(currentBall, ball)) {
                            changeColor(currentBall, ball);
                            burnLineBetweenHorizontal(ball);
                        }

                        if (isNextTwoBallsEqualUpVertical(currentBall, ball)) {
                            changeColor(currentBall, ball);
                            burnLineUpVertical(ball);
                        }

                        if (isNextTwoRightBallsEqualHorizontal(currentBall, ball)) {
                            changeColor(currentBall, ball);
                            burnLineRightHorizontal(ball);
                        }

                        if (isNextTwoLeftBallsEqualHorizontal(currentBall, ball)) {
                            changeColor(currentBall, ball);
                            burnLineLeftHorizontal(ball);
                        }

                        break;
                    case DOWN:

                        if (isBallsEqualBetweenHorizontal(currentBall, ball)) {
                            changeColor(currentBall, ball);
                        }

                        if (isNextTwoRightBallsEqualHorizontal(currentBall, ball)) {
                            changeColor(currentBall, ball);
                        }

                        if (isNextTwoLeftBallsEqualHorizontal(currentBall, ball)) {
                            changeColor(currentBall, ball);
                        }

                        if (isNextTwoBallsEqualDownVertical(currentBall, ball)) {
                            changeColor(currentBall, ball);
                        }

                        break;
                }
            }
        });
    }


    @Override
    public void setBall(Elements color, int x, int y) {
       /* Point pt = pt(x, y);
        for (int m=0; m<balls.size(); m++){
           System.out.println("ball index m = " + m+", x: "+balls.get(m).getX() +", y: "+ balls.get(m).getY() +", color "+ balls.get(m).getColor().ch);
        }

        if (balls.contains(pt)) {
            Ball currentBall = balls.get(balls.indexOf(pt));
            Elements currentColor = currentBall.getColor();
           // System.out.println("ball color changed 1 " + balls.get(balls.indexOf(pt)).getColor());
            balls.get(balls.indexOf(pt)).setColor(color);
           // System.out.println("ball color changed 2 " + balls.get(balls.indexOf(pt)).getColor());
            Point adjacentBall = Direction.LEFT.change(currentBall);
            balls.get(balls.indexOf(adjacentBall)).setColor(currentColor);
        }

        System.out.println("-----------------------------------");

        for (int m=0; m<balls.size(); m++){
            System.out.println("ball index m = " + m+", x: "+balls.get(m).getX() +", y: "+ balls.get(m).getY() +", color "+ balls.get(m).getColor().ch);
        }*/
    }

    @Override
    public void removeBall(int x, int y) {
        balls.remove(pt(x, y));
    }

    public List<Hero> getHeroes() {
        return players.stream()
                .map(Player::getHero)
                .collect(toList());
    }


    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    public List<Ball> getBalls() {
        return balls;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = Lines.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return Lines.this.getBalls();
            }
        };
    }
}
