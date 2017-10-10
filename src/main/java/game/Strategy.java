package game;

import game.action.Action;

import java.util.Set;

/**
 * Created by jansene on 9/28/17.
 */

public interface Strategy {
    Action ChooseAction(Game game);
}
