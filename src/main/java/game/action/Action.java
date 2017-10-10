package game.action;

import game.Game;

/**
 * Created by jansene on 10/2/17.
 */

public abstract class Action {

    protected Game game;

    public Action(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public abstract void play();

    public abstract boolean isAllowed();

    public void setGame(Game game) {
        this.game = game;
    }
}
