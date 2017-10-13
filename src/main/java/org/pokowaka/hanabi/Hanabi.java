package org.pokowaka.hanabi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.*;
import org.deeplearning4j.rl4j.learning.Learning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.util.DataManager;
import org.pokowaka.hanabi.ai.rl4j.HanabiMDP;
import org.pokowaka.hanabi.ai.rl4j.HanabiState;
import org.pokowaka.hanabi.ai.rl4j.LearnFactory;
import org.pokowaka.hanabi.ai.rl4j.RL4JStrategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Hanabi {

    public static void train(String fileIn, String fileOut, String net, int players)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        QLearning.QLConfiguration HANABI_QL =
                mapper.readValue(new File(fileIn), QLearning.QLConfiguration.class);

        DQNFactoryStdDense.Configuration HANABI_NET =
                DQNFactoryStdDense.Configuration.builder()
                        .l2(0.01)
                        .numLayer(1)
                        .numHiddenNodes(39)
                        .build();

        // record the training data in rl4j-data in a new folder
        DataManager manager = new DataManager(true);

        HanabiMDP mdp = new HanabiMDP(players);

        Learning<HanabiState, Integer, DiscreteSpace, IDQN> dql = null;

        if (net.equals("new") || Files.notExists(Paths.get(net)))
            dql = LearnFactory.getDefaultHanabiTrainer(mdp, HANABI_NET, HANABI_QL, manager);
        else {
            IDQN dqn = DQN.load(net);
            dql = LearnFactory.getHanabiTrainer(mdp, HANABI_QL, manager, dqn);
        }

        // start the training
        dql.train();

        // get the final policy
        DQNPolicy<HanabiState> pol = (DQNPolicy<HanabiState>) dql.getPolicy();

        // serialize and save (serialization showcase, but not required)
        pol.save(fileOut);
    }

    public static void run(String net, int players) throws IOException {
        RL4JStrategy strategy = new RL4JStrategy(net);
        Strategy strategies[] = new Strategy[players];
        for (int i = 0; i < players; i++) {
            strategies[i] = strategy;
        }
        Game g = new Game(strategies);
        int score = g.play();
        System.out.println("Score: " + score);
    }

    public static void main(String[] args) throws ParseException, IOException {
        // create Options object
        Options options = new Options();

        // add t option
        options.addOption("t", true, "train the network with the given json config");
        options.addOption("nn", true, "initialize with the given neural net");
        options.addOption("o", true, "output of trained network");
        options.addOption("r", true, "run a simulation with the given network");
        options.addOption("p", true, "number of players");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        int players = Integer.parseInt(cmd.getOptionValue("p", "4"));
        if (cmd.hasOption("t")) {
            String file = cmd.getOptionValue("o", "trained.bin");
            train(cmd.getOptionValue("t"), file, cmd.getOptionValue("nn", "new"), players);
        } else if (cmd.hasOption("r")) {
            run (cmd.getOptionValue("r", "dqn.bin"), players);
        } else {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("hanabi", options);
            while (true) {
                train("conf.json", "dqn.bin", "dqn.bin", 2);
                run("dqn.bin", 2);
            }
        }
    }
}
