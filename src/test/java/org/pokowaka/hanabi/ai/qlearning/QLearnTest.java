package org.pokowaka.hanabi.ai.qlearning;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.primitives.Pair;
import org.pokowaka.hanabi.Game;
import org.pokowaka.hanabi.Strategy;
import org.deeplearning4j.rl4j.learning.Learning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.util.DataManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pokowaka.hanabi.ai.rl4j.*;

import java.io.File;
import java.io.IOException;

import static com.fasterxml.jackson.annotation.PropertyAccessor.CREATOR;
import static java.lang.annotation.ElementType.FIELD;

public class QLearnTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void rl4jLearnTest() throws IOException {

        int step = 8000; // 5 minutes
        QLearning.QLConfiguration HANABI_QL =
                new QLearning.QLConfiguration(
                        123,    //Random seed
                        200,    //Max step By epoch
                        8000, //Max step
                        150000, //Max size of experience replay
                        32,     //size of batches
                        500,    //target update (hard)
                        10,     //num step noop warmup
                        0.01,   //reward scaling
                        0.99,   //gamma
                        1.0,    //td-error clipping
                        0.1f,   //min epsilon
                        10000,   //num step for eps greedy anneal
                        true    //double DQN
                );

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(HANABI_QL));

        QLearning.QLConfiguration foo =
                mapper.readValue(mapper.writeValueAsString(HANABI_QL), QLearning.QLConfiguration.class);

        DQNFactoryStdDense.Configuration HANABI_NET =
                DQNFactoryStdDense.Configuration.builder()
                        .l2(0.01)
                        .numLayer(3)
                        .numHiddenNodes(40)
                        .build();


        //record the training data in rl4j-data in a new folder
        DataManager manager = new DataManager(true);

        //define the mdp from toy (toy length)
        HanabiMDP mdp = new HanabiMDP();

        //define the training method
        //Learning<HanabiState, Integer, HanabiSpace, IDQN> dql = new AsyncNStepQLearningDiscreteDense<HanabiState, HanabiSpace>(mdp, HANABI_NET, HANABI_ASYNC_QL, manager);

        Learning<HanabiState, Integer, DiscreteSpace, IDQN> dql = LearnFactory.getDefaultHanabiTrainer(mdp, HANABI_NET,
                HANABI_QL, manager);

        //start the training
        dql.train();

        //get the final policy
        DQNPolicy<HanabiState> pol = (DQNPolicy<HanabiState>) dql.getPolicy();

        //serialize and save (serialization showcase, but not required)
        pol.save("/tmp/hanabipolicy3");

        //close the mdp (close connection)
        mdp.close();

        mdp.close();

    }

    @Test
    public void playTestRec() throws IOException {
        IDQN dqn = new DQN(ModelSerializer.restoreMultiLayerNetwork("/Users/jansene/rl4j-data/2/model/dqn.bin"));
        DQNPolicy<HanabiState> policy = new DQNPolicy<>(dqn);
        RL4JStrategy strategy = new RL4JStrategy("/tmp/hanabipolicy2");
        Game g = new Game(new Strategy[]{strategy, strategy, strategy, strategy});
        int sum = 0;
        g.reset();
        int score = g.play();
        sum += score;
        System.out.println("Score: " + score + " avg: " + ((double) sum / 1));

    }

    @Test
    public void playTest() throws IOException {
        RL4JStrategy strategy = new RL4JStrategy("/tmp/hanabipolicy3");
        Game g = new Game(new Strategy[]{strategy, strategy});
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            g.reset();
            int score = g.play();
            sum += score;
        }
        System.out.println("Score:  avg: " + ((double) sum / 100));

    }
}