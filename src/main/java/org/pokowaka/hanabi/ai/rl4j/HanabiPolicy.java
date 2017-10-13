package org.pokowaka.hanabi.ai.rl4j;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.policy.Policy;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.pokowaka.hanabi.action.Action;

import java.util.Random;
import java.util.Vector;

/**
 * An {@link HanabiPolicy} finds the best valid action in the game. It does this by evaluating the
 * neural network and picking the action which has the highest score and is valid.
 */
@Slf4j
public class HanabiPolicy extends DQNPolicy<HanabiState> {
    private final DiscreteSpace actionSpace;
    private Random rnd = new Random();

    public HanabiPolicy(IDQN dqn, HanabiMDP mdp) {
        this(dqn, mdp.getActionSpace());
    }

    public HanabiPolicy(IDQN dqn, DiscreteSpace space) {
        super(dqn);
        this.actionSpace = space;
    }

    public Integer nextAction(INDArray input) {
        INDArray output = getNeuralNet().output(input);

        double bestScore = Double.MIN_VALUE;
        int bestAction = 0;
        int size = output.size(1);

        // Find the best valid action
        for (int i = 0; i < size; i++) {
            Action act = (Action) actionSpace.encode(i);
            double score = output.getDouble(i);
            if (act.isAllowed() && bestScore <= score)
                bestAction = (score == bestScore && rnd.nextFloat() < 0.5) ? bestAction : i;
        }

        return bestAction;
    }

    public Action nextAction(HanabiState state) {
        INDArray arr = Nd4j.create(state.toArray());
        return (Action) actionSpace.encode(nextAction(arr));
    }
}
