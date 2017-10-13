package org.pokowaka.hanabi.action;

import org.pokowaka.hanabi.Card;
import org.pokowaka.hanabi.Game;
import org.pokowaka.hanabi.Player;

/** Created by jansene on 10/2/17. */
public class Discard extends Action {
    int card;

    public Discard(Game g, int c) {
        super(g);
        this.card = c;
    }

    @Override
    public boolean isAllowed() {
        return !game.isOver();
    }

    @Override
    public void play() {
        Player player = game.getCurrent();
        Card cardInHand = player.getCard(card);
        game.discard(cardInHand);
        player.discardCard(card);
    }

    @Override
    public String toString() {
        return "discard{" + game.getCurrent().getCard(this.card) + '}';
    }
}
