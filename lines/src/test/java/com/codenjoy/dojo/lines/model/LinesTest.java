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

import java.util.Random;

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

   // private String copyRandomString;
   // private String randomString = copyRandomString = fillFieldRandom(100);

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

    /*private String fillFieldRandom(int size) {
        Elements[] elements = Elements.values();
        int count = 0;
        StringBuilder stringBuilder = new StringBuilder();

        do {
            int index = new Random().nextInt(elements.length);
            if (index == 0) continue;
            stringBuilder.append(elements[index]);
            count++;
        } while (count < size);
       // System.out.println("\r\n" + count);
       // System.out.print(stringBuilder.toString());
        return stringBuilder.toString();
    }*/

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


   /* private void givenRandomField(int size) {
        Level level = new LevelImpl(size);
        game = new Lines(level, dice);
        game.fillFieldRandom();
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        hero = game.getHeroes().get(0);
    }*/

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

    //fill the game field by random elements
    //because of random this case does not always work as expected
    /*@Test
    public void fillGameFieldRandomly() {
        givenFl(randomString);
        System.out.println("randomString " + randomString);
        hero.act(5, 3);
        game.tick();

        assertE(copyRandomString);
        System.out.println("copyRandomString " + copyRandomString);
    }*/


    //test burnline Method (right)
    @Test
    public void shouldChangeColorWhenThreeSameColorInLine_right() {
        givenFl(" B YRBW   " +
                       " Y BWWG   " +
                       " BYGBWW   " +
                       " YRBYBB   " +
                       "  RY G    " +
                       "   Y      " +
                       "G YBR     " +
                       "RGWBRYGBWR" +
                       "G BGYWRYRW" +
                       "     WRBRW");

        hero.act(3, 6);
        game.tick();

        assertE(" B Y      " +
                         " Y BRBW   " +
                         " BYGWWG   " +
                         " YRYBWW   " +
                         "  RY G    " +
                         "   Y      " +
                         "G YBR     " +
                         "RGWBRYGBWR" +
                         "G BGYWRYRW" +
                         "     WRBRW");
    }

    //additional test burnline Method (right)
    @Test
    public void shouldChangeColorWhenThreeSameColorInLine_right2() {
        givenFl(" B YRBW   " +
                       " Y BWWG   " +
                       " BYGBWW   " +
                       " YRBYBB   " +
                       "  RY G    " +
                       "   Y      " +
                       "G YBR     " +
                       "RGGWGGBWR " +
                       "G BGYWRYRW" +
                       "     WRBRW");

        hero.act(2, 2);
        game.tick();

        assertE(" B    W   " +
                         " Y YRBG   " +
                         " BYBWWW   " +
                         " YRGBWB   " +
                         "  RBYB    " +
                         "   Y G    " +
                         "G YY      " +
                         "RGWBR BWR " +
                         "G BGYWRYRW" +
                         "     WRBRW");
    }



    //test burnline Method (right)
    @Test
    public void shouldChangeColorWhenThreeSameColorInLine_rightBetweenVertical() {
        givenFl( " B YRBW   " +
                        " Y BWWG   " +
                        " BYGBWW   " +
                        " YRBYBB   " +
                        "  RYBG    " +
                        "   Y      " +
                        "G YBR     " +
                        "RGWBRYGBWR" +
                        "G BGYWRYRW" +
                        "     WRBRW");

        hero.act(3, 6);
        game.tick();

        assertE(" B Y BW   " +
                         " Y B WG   " +
                         " BYG WW   " +
                         " YRYRBB   " +
                         "  RYWG    " +
                         "   Y      " +
                         "G YBR     " +
                         "RGWBRYGBWR" +
                         "G BGYWRYRW" +
                         "     WRBRW");
    }

    //test burnline Method (right)
    @Test
    public void shouldChangeColorWhenThreeSameColorInLine_rightUpVertical() {
        givenFl( " B YRBW   " +
                        " Y BWWG   " +
                        " BYGBWW   " +
                        " YRBWBB   " +
                        "  RYBG    " +
                        "   Y      " +
                        "G YBR     " +
                        "RGWBRYGBWR" +
                        "G BGYWRYRW" +
                        "     WRBRW");

        hero.act(4, 6);
        game.tick();

        assertE(" B YR W   " +
                         " Y BW G   " +
                         " BYGB W   " +
                         " YRBBBB   " +
                         "  RYBG    " +
                         "   Y      " +
                         "G YBR     " +
                         "RGWBRYGBWR" +
                         "G BGYWRYRW" +
                         "     WRBRW");
    }

    //test burnline Method once more(right) case UpVertical
    @Test
    public void shouldChangeColorWhenThreeSameColorInLine_rightUpVertical2() {
        givenFl( " B YRBW   " +
                        " Y BWWG   " +
                        " BYGBWW   " +
                        " YRBWBB   " +
                        "  RYBG    " +
                        "   Y      " +
                        "G YBR     " +
                        "RGWBRYGBWR" +
                        "G BGYWRYRW" +
                        "     WRBRW");

        hero.act(2, 1); //21
        game.tick();

        assertE(" B  RBW   " +
                         " Y  WWG   " +
                         " BY BWW   " +
                         " YRYWBB   " +
                         "  RBBG    " +
                         "   G      " +
                         "G YBR     " +
                         "RGWYRYGBWR" +
                         "G GYYWRYRW" +
                         "     WRBRW");
    }

    //test burnline Method once more(right) case UpVertical
    @Test
    public void shouldChangeColorWhenThreeSameColorInLine_rightUpVertical3() {
        givenFl( " B  RBW Y " +
                        " Y  WWG Y " +
                        " BY BWWY  " +
                        " YRYWBB   " +
                        "  RBBG    " +
                        "   G      " +
                        "G YBR     " +
                        "RGWYRYGBWR" +
                        "G GYYWRYRW" +
                        "     WRBRW");

        hero.act(7, 7);
        game.tick();

        assertE(" B  RBW   " +
                         " Y  WWG   " +
                         " BY BWW   " +
                         " YRYWBB   " +
                         "  RBBG    " +
                         "   G      " +
                         "G YBR     " +
                         "RGWYRYGBWR" +
                         "G GYYWRYRW" +
                         "     WRBRW");
    }

    @Test
    public void shouldChangeColorWhenThreeSameColorInLine_rightDownVertical() {
        givenFl( " B  WBY Y " +
                        " Y  WWG Y " +
                        " BY BWWY  " +
                        " YRYWBB   " +
                        "  RBBB    " +
                        "  YWG     " +
                        "G YYR     " +
                        "RGWYRYGBWR" +
                        "G GYYWRYRW" +
                        "     WRBRW");

        hero.act(4, 7);
        game.tick();

        assertE(" B  W Y Y " +
                         " Y  W G Y " +
                         " BY W WY  " +
                         " YRYWBB   " +
                         "  RBBW    " +
                         "  YWG     " +
                         "G YYR     " +
                         "RGWYRYGBWR" +
                         "G GYYWRYRW" +
                         "     WRBRW");
    }


    @Test
    public void shouldChangeColorWhenThreeSameColorInLine_UpBetweenHorizontal() {
        givenFl( " B  WBY Y " +
                        " Y  WWG Y " +
                        " BY BWWY  " +
                        " YRYWBB   " +
                        "  RBBB    " +
                        "  YWG     " +
                        "G YYR     " +
                        "RGWYRYGBWR" +
                        "G GYYWRYRW" +
                        "     WRBRW");

        hero.act(4, 7);
        game.tick();

        assertE(" B  W Y Y " +
                         " Y  W G Y " +
                         " BY W WY  " +
                         " YRYWBB   " +
                         "  RBBW    " +
                         "  YWG     " +
                         "G YYR     " +
                         "RGWYRYGBWR" +
                         "G GYYWRYRW" +
                         "     WRBRW");
    }

    @Test
    public void shouldChangeColorWhenThreeSameColorInLine_UpVertical() {
        givenFl( " B  WBY Y " +
                        " Y  WWG Y " +
                        " BY WWWY  " +
                        " YRYWBB   " +
                        "  RBBB    " +
                        "  YWG     " +
                        "G YYRY    " +
                        "RGWYRYGBWR" +
                        "G GYYWRYRW" +
                        "     YRBRW");

        hero.act(5, 0);
        game.tick();

        assertE(" B  W Y Y " +
                         " Y  W G Y " +
                         " BY W WY  " +
                         " YRYWBB   " +
                         "  RBBW    " +
                         "  YWGW    " +
                         "G YYRB    " +
                         "RGWYRBGBWR" +
                         "G GYY RYRW" +
                         "     WRBRW");
    }

    @Test
    public void shouldChangeColorWhenThreeSameColorInLine_UpRightHorizontal() {
        givenFl( " B  WBY Y " +
                        " YB WGG Y " +
                        " BY  WWY  " +
                        " YRYWYB   " +
                        "  RBBB  W " +
                        "  YWG   Y " +
                        "G GYYY  R " +
                        "RGYWRYGBWR" +
                        "G GBYWRYRW" +
                        "     YRBRW");

        hero.act(2, 2);
        game.tick();

        assertE(" B   BY Y " +
                         " Y  WGG Y " +
                         " BB WWWY  " +
                         " YY  YB   " +
                         "  RYWB  W " +
                         "  RBB   Y " +
                         "G YWGY  R " +
                         "RGGWRYGBWR" +
                         "G GBYWRYRW" +
                         "     YRBRW");
    }

    @Test
    public void shouldChangeColorWhenThreeSameColorInLine_UpLeftHorizontal() {
        givenFl( " B  WBY Y " +
                        " YB WGG Y " +
                        " BY  WWY  " +
                        " YRYWYB   " +
                        "  RBBB  W " +
                        "  YWG   Y " +
                        "G GYYY  R " +
                        "RGYWRYYBWR" +
                        "G GBYWRYRW" +
                        "     YRBRW");

        hero.act(7, 1);
        game.tick();

        assertE(" B  W   Y " +
                         " YB WBY Y " +
                         " BY  GG   " +
                         " YRYWWWY  " +
                         "  RBBYB W " +
                         "  YWGB  Y " +
                         "G GYY   R " +
                         "RGYWRY  WR" +
                         "G GBYWRBRW" +
                         "     YRBRW");
    }

    @Test
    public void shouldChangeColorWhenThreeSameColorInLine_LeftLeftHorizontal() {
        givenFl( " B  WBY Y " +
                        " YB WGG Y " +
                        " BY  WWY  " +
                        " YRBBYB   " +
                        "  RBBB  W " +
                        "  YWG   Y " +
                        "G GYYY  R " +
                        "RGYWRYYBWR" +
                        "G GBYWRYRW" +
                        "     YRBRW");

        hero.act(6, 6);
        game.tick();

        assertE(" B    Y Y " +
                         " YB WBG Y " +
                         " BY WGWY  " +
                         " YR  WY   " +
                         "  RBBB  W " +
                         "  YWG   Y " +
                         "G GYYY  R " +
                         "RGYWRYYBWR" +
                         "G GBYWRYRW" +
                         "     YRBRW");
    }


    // на бомбе я взрываюсь
    /*@Test
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
