package game.ai.rl4j;

import game.Card;
import game.Game;
import game.Strategy;
import game.action.Action;
import game.action.Discard;
import game.action.Hint;
import game.action.Play;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;
import java.util.Vector;

public class RL4JStrategy implements Strategy {

    DQNPolicy<HanabiState> policy;
    Game game = null;
    Vector<Action> actions = new Vector<>();

    public RL4JStrategy(String file) throws IOException {
        this(DQNPolicy.load(file));
    }

    public RL4JStrategy(DQNPolicy<HanabiState> policy) {
        this.policy = policy;
    }

    @Override
    public Action ChooseAction(Game game) {
        if (this.game == null) {
            this.game = game;
            initializeActions();
        }
        HanabiState state = new HanabiState(game);
        INDArray arr = Nd4j.create(state.toArray());

        Integer action = policy.nextAction(arr);
        Action act = actions.get(action);
        System.out.println("Player "  + game.getPlayerIdx() +  ", Opted for: " + act);
        return act;
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
