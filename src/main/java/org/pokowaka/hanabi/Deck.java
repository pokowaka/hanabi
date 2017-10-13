package org.pokowaka.hanabi;

import java.util.Collections;
import java.util.Vector;

/** Created by jansene on 9/28/17. */
public class Deck {
    private Vector<Card> deck = new Vector<>();
    private Vector<Card> discard = new Vector<>();

    public Deck() {
        reset();
    }

    public Deck(Deck other) {
        this.deck.addAll(other.deck);
        this.discard.addAll(other.discard);
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public Card draw() {
        return deck.remove(deck.size() - 1);
    }

    public void discard(Card c) {
        discard.add(c);
    }

    public Vector<Card> DiscardPile() {
        return discard;
    }

    private void addCards(Card.Color color, int nr, int count) {
        for (int i = 0; i < count; i++) {
            Card card = new Card(nr, color);
            deck.add(card);
        }
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public void reset() {
        deck.clear();
        discard.clear();

        for (Card.Color c : Card.Color.values()) {
            if (c == Card.Color.Unknown) continue;

            addCards(c, 1, 3);
            addCards(c, 2, 2);
            addCards(c, 3, 2);
            addCards(c, 4, 2);
            addCards(c, 5, 1);
        }

        shuffle();
    }

    @Override
    public String toString() {
        return "Deck{" + "deck=" + deck.size() + ", discard=" + discard.size() + '}';
    }

    public int size() {
        return deck.size();
    }
}
