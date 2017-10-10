package game.ai.rl4j;


import game.Card;
import game.Game;
import game.Player;
import org.deeplearning4j.rl4j.space.Encodable;

import java.util.Vector;

public class HanabiState implements Encodable {

    static final int PILES = 2 * (Card.Color.values().length - 1);
    private final Game game;

    double encoded[];

    public HanabiState(Game g) {
        this.game = g;
        this.encoded = new double[stateSize(g.getPlayerCount())];
    }

    public static int stateSize(int playerCount) {
        return 4 + PILES + playerCount * 8;
    }

    @Override
    public double[] toArray() {
        int i = 0;
        int currentPlayer = this.game.getPlayerIdx();
        this.encoded[i++] = this.game.getHints();
        this.encoded[i++] = this.game.getFireworks();
        this.encoded[i++] = currentPlayer;
        this.encoded[i++] = this.game.getDeck().size();

        for (Card.Color c : this.game.getPiles().keySet()) {
            this.encoded[i++] = c.getValue();
            this.encoded[i++] = this.game.getPiles().get(c);
        }

        Vector<Player> players = this.game.getPlayers();
        for (int j = 1; j < players.size(); j++) {
            Player p = players.elementAt((currentPlayer + j) % players.size());
            for (int k = 0; k < 4; k++) {
                Card c = p.getCard(k);
                this.encoded[i++] = c.getColor().getValue();
                this.encoded[i++] = c.getNumber();
            }
        }

        Player current = this.game.getCurrent();
        for (int k = 0; k < 4; k++) {
            Card c = current.getCard(k);
            this.encoded[i++] = current.knowsColor(k) ? c.getColor().getValue() : 0;
            this.encoded[i++] = current.knowsNumber(k) ? c.getNumber() : 0;
        }

        return encoded;
    }
}
