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

    private void changeColor(Ball first, Ball next){
        Elements firstColor = first.getColor();
        System.out.println(" changeColor first " +first );
        Elements nextColor = next.getColor();
        System.out.println("changeColor currentBall " + next);
        balls.get(balls.indexOf(next)).setColor(firstColor);
        balls.get(balls.indexOf(first)).setColor(nextColor);
        //TODO burn line
    }

    private Ball getBall(Point pt){
        return balls.stream()
                .filter(Predicate.isEqual(pt))
                .findFirst().orElseThrow(() -> new UnsupportedOperationException()); //TODO Exception
    }

    private boolean isBallsEqualBetweenVertical(Point pt){
        Point firstPT = Direction.UP.change(pt);
        Ball firstBall = getBall(firstPT);
        System.out.println("isBallsEqualBetweenVertical  ball "+firstBall +", color " + firstBall.getColor());
        Point nextPT = Direction.DOWN.change(pt);
        Ball nextBall = getBall(nextPT);
        System.out.println("isBallsEqualBetweenVertical  ball "+nextBall +", color " + nextBall.getColor());
        System.out.println("\r\n");
        return firstBall.isSameColor(nextBall);
    }

    private boolean isNextTwoBallsEqualHorizontal(Point pt){
        Point firstPT = Direction.LEFT.change(pt);
        Ball firstBall = getBall(firstPT);
        System.out.println("isNextTwoBallsEqualHorizontal  ball "+firstBall +", color " + firstBall.getColor());
        Point nextPT = Direction.LEFT.change(firstPT);
        Ball nextBall = getBall(nextPT);
        System.out.println("isNextTwoBallsEqualHorizontal  ball "+nextBall +", color " + nextBall.getColor());
        System.out.println("\r\n");
        return firstBall.isSameColor(nextBall);
    }

    private boolean isNextTwoBallsEqualUpVertical(Point pt){
        Point firstPT = Direction.UP.change(pt);
        Ball firstBall = getBall(firstPT);
        System.out.println("isNextTwoBallsEqualUpVertical  ball "+firstBall +", color " + firstBall.getColor());
        Point nextPT = Direction.UP.change(firstPT);
        Ball nextBall = getBall(nextPT);
        System.out.println("isNextTwoBallsEqualUpVertical  ball "+nextBall +", color " + nextBall.getColor());
        System.out.println("\r\n");
        return firstBall.isSameColor(nextBall);
    }

    private boolean isNextTwoBallsEqualDownVertical(Point pt){
        Point firstPT = Direction.DOWN.change(pt);
        Ball firstBall = getBall(firstPT);
        System.out.println("isNextTwoBallsEqualDownVertical  ball "+firstBall +", color " + firstBall.getColor());
        Point nextPT = Direction.DOWN.change(firstPT);
        Ball nextBall = getBall(nextPT);
        System.out.println("isNextTwoBallsEqualDownVertical  ball "+nextBall +", color " + nextBall.getColor());
        System.out.println("\r\n");
        return firstBall.isSameColor(nextBall);
    }


    @Override
    public void moveBalls(int x, int y) {
        Point pt = pt(x, y);
        Ball currentBall = getBall(pt);
        System.out.println(" moveBalls currentBall " +currentBall +", color " + currentBall.getColor());

        Direction.getValues().forEach(direction -> {
            Ball ball = getBall(direction.change(pt));
            System.out.println(" moveBalls ball "  +ball+", color " + ball.getColor());

            //просмотр элементов только относительно текущей позиции 4,7
            if (direction == Direction.LEFT) {
                   if (isBallsEqualBetweenVertical(ball)) {
                        changeColor(currentBall, ball);

                        System.out.println("isBallsEqualBetweenVertical changeColor currentBall "+ currentBall +", color " + currentBall.getColor());
                        System.out.println("isBallsEqualBetweenVertical changeColor ball "+ball +", color " + ball.getColor());
                        System.out.println("\r\n");
                   }

                    if ( isNextTwoBallsEqualHorizontal(ball)) {
                        System.out.println("isNextTwoBallsEqualHorizontal changeColor " );
                        changeColor(currentBall, ball);
                    }

                    if ( isNextTwoBallsEqualUpVertical(ball)){
                        changeColor(currentBall, ball);
                        System.out.println(" isNextTwoBallsEqualUpVertical true" );
                    }

                    if ( isNextTwoBallsEqualDownVertical(ball)){
                    changeColor(currentBall, ball);
                    System.out.println(" isNextTwoBallsEqualDownVertical true" );
                    }
            }
           // System.out.println(" direction " +direction.toString() );
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

    private void burnLine(){
        //TODO проверить сначала если рядом два элемента такого же цвета
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
