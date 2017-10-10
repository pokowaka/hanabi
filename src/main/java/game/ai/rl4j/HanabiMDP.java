package game.ai.rl4j;

import game.Card;
import game.Game;
import game.Strategy;
import game.action.Action;
import game.action.Discard;
import game.action.Hint;
import game.action.Play;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;

import java.util.Vector;

public class HanabiMDP implements MDP<HanabiState, Integer, DiscreteSpace> {

    final protected DiscreteSpace discreteSpace;
    final protected ObservationSpace<HanabiState> observationSpace;
    private final Game game;
    final protected Vector<Action> actions = new Vector<>();

    public HanabiMDP() {
        int players = 4;
        this.game = new Game(new Strategy[]{null, null, null, null});
        discreteSpace = new DiscreteSpace(8 + (10 * (players - 1)));
        observationSpace = new ArrayObservationSpace<>(new int[]{HanabiState.stateSize(players)});
        initializeActions();
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
    public void close() {

    }

    @Override
    public StepReply<HanabiState> step(Integer action) {
        Action a = actions.get(action);
        double reward = -10; // translate action --> to actual action.
        if (a.isAllowed())  {
            a.setGame(game);
            a.play();
            reward = game.score();
        }
        return new StepReply<>(new HanabiState(game), reward, game.isOver(), null);
    }

    @Override
    public boolean isDone() {
        return game.isOver();
    }

    @Override
    public MDP<HanabiState, Integer, DiscreteSpace> newInstance() {
        return new HanabiMDP();
    }

    private void initializeActions() {
        actions.add(new Discard(game, 0));
        actions.add(new Discard(game, 1));
        actions.add(new Discard(game, 2));
        actions.add(new Discard(game, 3));


        actions.add(new Play(game, 0));
        actions.add(new Play(game, 1));
        actions.add(new Play(game, 2));
        actions.add(new Play(game, 3));

        for (int i = 0; i < 3; i++) {
            actions.add(new Hint(game, i, 1));
            actions.add(new Hint(game, i, 2));
            actions.add(new Hint(game, i, 3));
            actions.add(new Hint(game, i, 4));
            actions.add(new Hint(game, i, 5));


            actions.add(new Hint(game, i, Card.Color.White));
            actions.add(new Hint(game, i, Card.Color.Red));
            actions.add(new Hint(game, i, Card.Color.Blue));
            actions.add(new Hint(game, i, Card.Color.Yellow));
            actions.add(new Hint(game, i, Card.Color.Green));
        }
    }
}
