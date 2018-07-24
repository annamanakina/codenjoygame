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


import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.lines.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class LinesTest {

    private Lines game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        Level level = new LevelImpl(board);
        game = new Lines(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        hero = game.getHeroes().get(0);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    // есть карта со мной
    @Test
    public void shouldFieldAtStart() {
        givenFl("          " +
                       "          " +
                       "  R W     " +
                       "          " +
                       "     G    " +
                       "          " +
                       "          " +
                       "          " +
                       "          " +
                       "          ");

        assertE("          " +
                         "          " +
                         "  R W     " +
                         "          " +
                         "     G    " +
                         "          " +
                         "          " +
                         "          " +
                         "          " +
                         "          ");
    }


    //move ball to the left
    @Test
    public void shouldMoveBall_left_checkIfNextBetweenHasTheSameColor() {
        givenFl(" B Y      " +
                       " Y B W    " +
                       " BYGBWW   " +
                       "   BY     " +
                       "   Y G    " +
                       "          " +
                       "          " +
                       "          " +
                       "          " +
                       "          ");

        hero.act(4, 7);
        game.tick();

        assertE(" B Y      " +
                         " Y B W    " +
                         " BYBGWW   " +
                         "   BY     " +
                         "   Y G    " +
                         "          " +
                         "          " +
                         "          " +
                         "          " +
                         "          ");
    }

    @Test
    public void shouldMoveBall_left_checkIfNextTwoHasTheSameColor_horizontal() {
        givenFl(" B Y      " +
                       " Y B W    " +
                       " BYGBWW   " +
                       "   BY     " +
                       "   Y G    " +
                       "          " +
                       "          " +
                       "          " +
                       "          " +
                       "          ");

        hero.act(4, 7);
        game.tick();

        assertE(" B Y      " +
                         " Y B W    " +
                         " BYBGWW   " +
                         "   BY     " +
                         "   Y G    " +
                         "          " +
                         "          " +
                         "          " +
                         "          " +
                         "          ");
    }

    @Test
    public void shouldMoveBall_left_up_checkIfNextTwoHasTheSameColor() {
        givenFl(" B Y      " +
                       " Y B W    " +
                       " BYGBWW   " +
                       "   BY     " +
                       "   Y G    " +
                       "          " +
                "          " +
                "          " +
                "          " +
                "          ");

        hero.act(4, 7);
        game.tick();

        assertE(" B Y      " +
                         " Y B W    " +
                         " BYBGWW   " +
                         "   BY     " +
                         "   Y G    " +
                         "          " +
                         "          " +
                         "          " +
                         "          " +
                         "          ");
    }

    //can move element if next two down elements have the same color
    @Test
    public void shouldMoveBall_left_down_checkIfNextTwoHasTheSameColor() {
        givenFl(" B Y      " +
                " Y Y W    " +
                " BYGBWW   " +
                "   BY     " +
                "   Y G    " +
                "          " +
                "          " +
                "          " +
                "          " +
                "          ");

        hero.act(4, 7);
        game.tick();

        assertE(" B Y      " +
                " Y Y W    " +
                " BYGBWW   " +
                "   BY     " +
                "   Y G    " +
                "          " +
                "          " +
                "          " +
                "          " +
                "          ");
    }

    //can move element and check if element is not going off the border
    @Test
    public void shouldMoveBall_inAllDirectionsAndCheckIfIsNotBoader() {
        givenFl(" B Y      " +
                " Y B W    " +
                " BYGBWW   " +
                "   BY     " +
                "   Y G    " +
                "          " +
                "G         " +
                "RG        " +
                "G         " +
                "          ");

        hero.act(1, 2);
        game.tick();

        assertE(" B Y      " +
                " Y B W    " +
                " BYGBWW   " +
                "   BY     " +
                "   Y G    " +
                "          " +
                "G         " +
                "GR        " +
                "G         " +
                "          ");
    }

    
    @Test
    public void shouldMoveBall_inAllDirections() {
        givenFl(" B Y      " +
                " Y B W    " +
                " BYGBWW   " +
                " YYBY     " +
                "  RY G    " +
                "   Y      " +
                "G YBR     " +
                "RGWBRYGBWR" +
                "G BGYWRYRW" +
                "     WRBRW");

        hero.act(9, 2);
        game.tick();

        assertE(" B Y      " +
                " Y B W    " +
                " BYGBWW   " +
                " YYBY     " +
                "  RY G    " +
                "   Y      " +
                "G YBR     " +
                "RGWBRYGBRW" +
                "G BGYWRYRW" +
                "     WRBRW");
    }




    // если небыло команды я никуда не иду
   /* @Test
    public void shouldStopWhenNoMoreRightCommand() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // я останавливаюсь возле границы
    @Test
    public void shouldStopWhenWallRight() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                        "☼   ☼" +
                        "☼  ☺☼" +
                        "☼   ☼" +
                        "☼☼☼☼☼");
    }

    @Test
    public void shouldStopWhenWallLeft() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldStopWhenWallUp() {
        givenFl("☼☼☼☼☼" +
                "☼ ☼ ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼ ☼ ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldStopWhenWallDown() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼ ☼ ☼" +
                "☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼ ☼ ☼" +
                "☼☼☼☼☼");
    }

    // я могу оставить бомбу
    @Test
    public void shouldMakeBomb() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.act();
        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ x ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");
    }

    // на бомбе я взрываюсь
    @Test
    public void shouldDieOnBomb() {
        shouldMakeBomb();

        assertTrue(hero.isAlive());

        hero.up();
        game.tick();
        verify(listener).event(Events.LOOSE);

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ X ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        assertFalse(hero.isAlive());
    }

    // я могу оставить бомб сколько хочу
    @Test
    public void shouldMakeBombTwice() {
        shouldMakeBomb();

        hero.act();
        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ x ☼" +
                "☼ x☺☼" +
                "☼☼☼☼☼");
    }

    // я могу собирать золото и получать очки
    // новое золото появится в рендомном месте
    @Test
    public void shouldGetGold() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺$☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        dice(1, 3);
        hero.right();
        game.tick();
        verify(listener).event(Events.WIN);

        assertE("☼☼☼☼☼" +
                "☼$  ☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // выполнения команд left + act не зависят от порядка - если они сделаны в одном тике, то будет дырка слева без перемещения
    @Test
    public void shouldMakeBomb2() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.down();
        hero.act();
//        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ x ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");
    }

    // проверить, что если новому обекту не где появится то программа не зависает - там бесконечный цикл потенциальный есть
    @Test(timeout = 1000)
    public void shouldNoDeadLoopWhenNewObjectCreation() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺$☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        dice(2, 2);
        hero.right();
        game.tick();
        verify(listener).event(Events.WIN);

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ $☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // я не могу ставить две бомбы на одной клетке
    @Test
    public void shouldMakeOnlyOneBomb() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.act();
        game.tick();

        hero.act();
        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ x ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");

        dice(1, 2);
        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ X ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        game.newGame(player);
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }*/
}
