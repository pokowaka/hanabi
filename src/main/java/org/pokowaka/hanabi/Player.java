package org.pokowaka.hanabi;

import org.pokowaka.hanabi.action.Action;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

/** Created by jansene on 9/28/17. */
public class Player {
    private final Vector<Card> hand = new Vector<>();
    private final Strategy strategy;
    private final Game game;
    private final Vector<Card> knowsColor = new Vector();
    private final Vector<Card> knowsNumber = new Vector();

    public Player(Player other, Game game) {
        this.hand.addAll(other.hand);
        this.strategy = other.strategy;
        this.knowsColor.addAll(other.knowsColor);
        this.knowsNumber.addAll(other.knowsNumber);
        this.game = game;
    }

    public Player(Strategy strategy, Game game) {
        this.strategy = strategy;
        this.game = game;
        reset();
    }

    public void reset() {
        hand.clear();
        knowsNumber.clear();
        knowsColor.clear();
        for (int i = 0; i < 4; i++) {
            hand.add(game.draw());
        }

        Collections.sort(hand);
    }

    public void play() {
        Action action = strategy.ChooseAction(this.game);
        action.play();
    }

    public Card getCard(int card) {
        return hand.elementAt(card);
    }

    public boolean knowsColor(int card) {
        return knowsColor.contains(getCard(card));
    }

    public boolean knowsNumber(int card) {
        return knowsNumber.contains(getCard(card));
    }

    public void revealColor(Card.Color c) {
        for (Card card : hand) {
            if (card.getColor() == c && !knowsColor.contains(card)) {
                knowsColor.add(card);
            }
        }
    }

    public void revealNumber(int nr) {
        for (Card card : hand) {
            if (card.getNumber() == nr && !knowsNumber.contains(card)) {
                knowsNumber.add(card);
            }
        }
    }

    public void discardCard(int card) {
        Card removed = hand.remove(card);
        knowsColor.remove(removed);
        knowsNumber.remove(removed);
        hand.add(game.draw());
        Collections.sort(hand);
    }

    @Override
    public String toString() {
        return "Player{"
                + "hand="
                + hand
                + ", knowsColor="
                + knowsColor
                + ", knowsNumber="
                + knowsNumber
                + '}';
    }

    public Iterator<Card> getCards() {
        return hand.iterator();
    }
}
