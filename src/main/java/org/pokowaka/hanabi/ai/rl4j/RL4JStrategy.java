package org.pokowaka.hanabi.ai.rl4j;

import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.pokowaka.hanabi.Game;
import org.pokowaka.hanabi.Strategy;
import org.pokowaka.hanabi.action.Action;

import java.io.IOException;

@Slf4j
public class RL4JStrategy implements Strategy {

    private final IDQN dqn;
    private HanabiPolicy policy;
    private HanabiState state;

    public RL4JStrategy(String file) throws IOException {
        this(DQN.load(file));
    }

    public RL4JStrategy(IDQN dqn) {
        this.dqn  = dqn;
    }

    @Override
    public Action ChooseAction(Game game) {
        if (this.policy == null) {
            this.policy = new HanabiPolicy(dqn, new HanabiSpace(game));
            this.state = new HanabiState(game);
        }

        Action act = policy.nextAction(state);
        System.out.println("Player: " + game.getPlayerIdx() + ":" + game.getCurrent());
        System.out.println("Action: " + act);
        return act;
    }
}
