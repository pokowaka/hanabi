package game.action;


import game.Card;
import game.Game;
import game.Player;

import java.util.Iterator;

/**
 * Created by jansene on 10/2/17.
 */

public class Hint extends Action {
    private final int player;
    private final int number;
    private final Card.Color color;

    public Hint(Game g, int player, int number) {
        super(g);
        this.player = player;
        this.number = number;
        this.color = Card.Color.Unknown;
    }

    public Hint(Game g, int player, Card.Color c) {
        super(g);
        this.player = player;
        this.number = -1;
        this.color = c;
    }

    @Override
    public boolean isAllowed() {
        return !game.isOver() && game.getHints() > 0 && game.getPlayerIdx() != player;
    }

    @Override
    public void play() {
        game.useHint();
        Player p = game.getPlayer(player);
        if (this.color == Card.Color.Unknown) {
            p.revealNumber(number);
        } else {
            p.revealColor(this.color);
        }
    }

    @Override
    public String toString() {
        return "Hint{" +
                "player=" + player +
                ", number=" + number +
                ", color=" + color +
                '}';
    }
}
