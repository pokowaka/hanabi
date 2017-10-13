package org.pokowaka.hanabi;

import org.pokowaka.hanabi.action.Action;

/**
 * Created by jansene on 9/28/17.
 */

public interface Strategy {
    Action ChooseAction(Game game);
}
