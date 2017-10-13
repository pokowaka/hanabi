package org.pokowaka.hanabi.ai.rl4j;

import org.pokowaka.hanabi.Game;
import org.pokowaka.hanabi.Strategy;
import org.pokowaka.hanabi.action.Action;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;

public class HanabiMDP implements MDP<HanabiState, Integer, DiscreteSpace> {

    protected final HanabiSpace discreteSpace;
    protected final ObservationSpace<HanabiState> observationSpace;
    private final Game game;

    public HanabiMDP() {
        this(4);
    }

    public HanabiMDP(int players) {
        this.game = new Game(new Strategy[players]);
        discreteSpace = new HanabiSpace(game);
        observationSpace = new ArrayObservationSpace<>(new int[] {HanabiState.stateSize(players)});
    }

    public Game getGame() {
        return game;
    }

    @Override
    public ObservationSpace<HanabiState> getObservationSpace() {
        return observationSpace;
    }

    @Override
    public DiscreteSpace getActionSpace() {
        return discreteSpace;
    }

    @Override
    public HanabiState reset() {
        game.reset();
        return new HanabiState(game);
    }

    @Override
    public void close() {}

    @Override
    public StepReply<HanabiState> step(Integer action) {
        Action a = (Action) discreteSpace.encode(action);
        assert a.isAllowed();
        double reward = 0;
        a.setGame(game);
        a.play();
        if (game.isOver())
            reward = game.score();

        return new StepReply<>(new HanabiState(game), reward, isDone(), null);
    }

    @Override
    public boolean isDone() {
        return game.isOver();
    }

    @Override
    public MDP<HanabiState, Integer, DiscreteSpace> newInstance() {
        return new HanabiMDP();
    }
}
