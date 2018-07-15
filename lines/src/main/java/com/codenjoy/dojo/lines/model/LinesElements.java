package com.codenjoy.dojo.lines.model;

import com.codenjoy.dojo.services.printer.CharElements;


public enum LinesElements implements CharElements {

    NONE(' '),       // empty cells after lines dissapear
    RED('R'),       //
    BLUE('B'),
    YELLOW('Y'),
    WHITE('W'),

    DEAD_HERO('X'),  // а это временное явление - трупик моего героя, которое пропадет в следующем такте
    GOLD('$'),       // это то, за чем будет охота
    BOMB('x');       // а это бомба, на которой можно подорваться

    final char ch;

    LinesElements(char ch) {
        this.ch = ch;
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static Elements valueOf(char ch) {
        for (Elements el : Elements.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }
}

