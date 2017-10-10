package game.ai.qlearning;

import game.Game;
import game.Strategy;
import game.ai.rl4j.HanabiMDP;
import game.ai.rl4j.HanabiState;
import game.ai.rl4j.RL4JStrategy;
import org.deeplearning4j.rl4j.learning.Learning;
import org.deeplearning4j.rl4j.learning.async.nstep.discrete.AsyncNStepQLearningDiscrete;
import org.deeplearning4j.rl4j.learning.async.nstep.discrete.AsyncNStepQLearningDiscreteDense;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.space.Box;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.util.DataManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class QLearnTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void rl4jLearnTest() throws IOException {

        int step = 80000; // 5 minutes
        int totaal = step * 8;
        QLearning.QLConfiguration HANABI_QL =
                new QLearning.QLConfiguration(
                        123,   //Random seed
                        100000,//Max step By epoch
                        totaal, //Max step
                        10000, //Max size of experience replay
                        32,    //size of batches
                        100,   //target update (hard)
                        0,     //num step noop warmup
                        0.05,  //reward scaling
                        0.99,  //gamma
                           10.0,  //td-error clipping
                        0.1f,  //min epsilon
                        2000,  //num step for eps greedy anneal
                        true   //double DQN
                );


        AsyncNStepQLearningDiscrete.AsyncNStepQLConfiguration HANABI_ASYNC_QL =
                new AsyncNStepQLearningDiscrete.AsyncNStepQLConfiguration(
                        123,        //Random seed
                        100000,     //Max step By epoch
                        totaal,      //Max step
                        8,          //Number of threads
                        5,          //t_max
                        100,        //target update (hard)
                        0,          //num step noop warmup
                        0.1,        //reward scaling
                        0.99,       //gamma
                        10.0,       //td-error clipping
                        0.1f,       //min epsilon
                        2000        //num step for eps greedy anneal
                );


        DQNFactoryStdDense.Configuration HANABI_NET =
                DQNFactoryStdDense.Configuration.builder()
                        .l2(0.01).learningRate(1e-2).numLayer(3).numHiddenNodes(16).build();


        //record the training data in rl4j-data in a new folder
        DataManager manager = new DataManager(true);

        //define the mdp from toy (toy length)
        HanabiMDP mdp = new HanabiMDP();

        //define the training method
        Learning<HanabiState, Integer, DiscreteSpace, IDQN> dql = new AsyncNStepQLearningDiscreteDense<HanabiState>(mdp, HANABI_NET, HANABI_ASYNC_QL, manager);

        //Learning<HanabiState, Integer, DiscreteSpace, IDQN> dql = new QLearningDiscreteDense<HanabiState>(mdp, HANABI_NET, HANABI_QL, manager);

        //start the training
        dql.train();

        //get the final policy
        DQNPolicy<HanabiState> pol = (DQNPolicy<HanabiState>) dql.getPolicy();

        //serialize and save (serialization showcase, but not required)
        pol.save("/tmp/hanabipolicy2");

        //close the mdp (close connection)
        mdp.close();

        mdp.close();

    }

    @Test
    public void playTest() throws IOException {
        RL4JStrategy strategy = new RL4JStrategy("/tmp/hanabipolicy2");
        Game g = new Game(new Strategy[]{strategy, strategy, strategy, strategy});
        int sum = 0;
        g.reset();
        int score = g.play();
        sum += score;
        System.out.println("Score: " + score + " avg: " + ((double) sum / 1));

    }
}