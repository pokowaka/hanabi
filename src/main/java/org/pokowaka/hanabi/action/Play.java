package org.pokowaka.hanabi.action;

import org.pokowaka.hanabi.Card;
import org.pokowaka.hanabi.Game;
import org.pokowaka.hanabi.Player;

/** Created by jansene on 10/2/17. */
public class Play extends Action {
    int card;

    public Play(Game g, int c) {
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
        Card crdFromHand = player.getCard(this.card);
        player.discardCard(this.card);
        game.play(crdFromHand);
    }

    @Override
    public String toString() {
        return "Play{" + game.getCurrent().getCard(this.card) + '}';
    }
}
