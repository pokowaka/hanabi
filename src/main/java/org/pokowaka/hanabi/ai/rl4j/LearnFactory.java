package org.pokowaka.hanabi.ai.rl4j;

import org.deeplearning4j.rl4j.learning.Learning;
import org.deeplearning4j.rl4j.learning.StepCounter;
import org.deeplearning4j.rl4j.learning.sync.ExpReplay;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscrete;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.policy.EpsGreedy;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.util.DataManager;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class LearnFactory {

    public static Learning<HanabiState, Integer, DiscreteSpace, IDQN> getDefaultHanabiTrainer(
            HanabiMDP mdp,
            DQNFactoryStdDense.Configuration netConf,
            QLearning.QLConfiguration conf,
            DataManager dataManager) {
        DQNFactoryStdDense dqnFactory = new DQNFactoryStdDense(netConf);
        IDQN dqn =
                dqnFactory.buildDQN(
                        mdp.getObservationSpace().getShape(), mdp.getActionSpace().getSize());
        return getHanabiTrainer(mdp, conf, dataManager, dqn);
    }

    @NotNull
    public static Learning<HanabiState, Integer, DiscreteSpace, IDQN> getHanabiTrainer(HanabiMDP mdp, QLearning
            .QLConfiguration conf, DataManager dataManager, IDQN dqn) {
        StepCounter counter = new StepCounter();
        HanabiPolicy policy = new HanabiPolicy(dqn, mdp);
        EpsGreedy greedy =
                new EpsGreedy<>(
                        policy,
                        mdp,
                        conf.getUpdateStart(),
                        conf.getEpsilonNbStep(),
                        new Random(),
                        conf.getMinEpsilon(),
                        counter);
        ExpReplay<Integer> replay =
                new ExpReplay<>(conf.getExpRepMaxSize(), conf.getBatchSize(), conf.getSeed());

        return new QLearningDiscrete<HanabiState>(
                counter, replay, policy, greedy, mdp, dqn, conf, dataManager) {};
    }
}
