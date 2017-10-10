package game;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

/**
 * Created by jansene on 9/28/17.
 */

public class Game {
    private final Random rnd;
    protected final HashMap<Card.Color, Integer> piles = new HashMap<>();
    protected final Vector<Player> players = new Vector<>();
    protected final Deck deck;
    protected int hints = 8;
    protected int red = 3;
    private boolean log = false;
    protected int currentPlayer = 0;

    public Game(Game other) {
        for (Player p : other.players) {
            players.add(new Player(p, this));
        }
        this.deck = new Deck(other.deck);
        this.rnd = other.rnd;
        this.hints = other.hints;
        this.red = other.red;
        this.currentPlayer = other.currentPlayer;
        for (Card.Color c : other.piles.keySet()) {
            piles.put(c, other.piles.get(c));
        }
    }

    public Game(Strategy... strategies) {
        this(new Random(), strategies);
    }

    public Game(Random rnd, Strategy... strategies) {
        this.rnd = rnd;
        this.deck = new Deck();
        for (Strategy strategy : strategies) {
            players.add(new Player(strategy, this));
        }
        reset();
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Game)) return false;

        Game game = (Game) o;

        return new EqualsBuilder()
                .append(hints, game.hints)
                .append(red, game.red)
                .append(currentPlayer, game.currentPlayer)
                .append(piles, game.piles)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(piles)
                .append(deck)
                .append(hints)
                .append(red)
                .append(currentPlayer)
                .toHashCode();
    }

    public void reset() {
        hints = 8;
        red = 3;
        currentPlayer = 0;
        deck.reset();

        for (Card.Color c : Card.Color.values()) {
            if (c == Card.Color.Unknown)
                continue;
            piles.put(c, 0);
        }
        for (Player player : players) {
            player.reset();
        }
        deck.shuffle();

    }

    public int play() {
        while (!isOver()) {
            nextPlayer().play();
            System.out.println("Piles: " + this.piles);
        }
        return score();
    }

    @Override
    public String toString() {
        return "Game{" +
                "code=" + System.identityHashCode(this) +
                ", piles=" + piles +
                ", deck=" + deck +
                ", hints=" + hints +
                ", red=" + red +
                ", currentPlayer=" + currentPlayer +
                '}';
    }

    public boolean isOver() {
        return red <= 0 || deck.isEmpty();
    }

    public Player nextPlayer() {
        currentPlayer = (currentPlayer + 1) % players.size();
        return getCurrent();
    }

    public Player getCurrent() {
        return players.get(currentPlayer % players.size());
    }

    public Player getPlayer(int idx) {
        return players.get((currentPlayer + idx) % players.size());
    }

    public HashMap<Card.Color, Integer> getPiles() {
        return piles;
    }

    public Card draw() {
        return deck.draw();
    }

    public int score() {
        int sum = 0;
        for (int i : piles.values()) {
            sum += i;
        }
        return sum;
    }

    public void play(Card c) {
        assert c != null;
        if (piles.get(c.getColor()) == c.getNumber() - 1) {
            piles.put(c.getColor(), c.getNumber());
            if (c.getNumber() == 5)
                hints = Integer.min(8, hints + 1);
        } else {
            red -= 1;
        }
    }

    public int getHints() {
        return hints;
    }

    public void discard(Card removed) {
        hints = Integer.min(8, hints + 1);
        deck.discard(removed);
    }

    public int getPlayerCount() {
        return players.size();
    }

    public Deck getDeck() {
        return deck;
    }

    public Vector<Player> getPlayers() {
        return players;
    }

    public int getPlayerIdx() {
        return currentPlayer;
    }

    public int getFireworks() {
        return red;
    }

    public void useHint() {
        hints--;
    }
}
