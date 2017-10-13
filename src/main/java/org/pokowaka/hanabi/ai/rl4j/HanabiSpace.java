package org.pokowaka.hanabi.ai.rl4j;

import org.pokowaka.hanabi.Card;
import org.pokowaka.hanabi.Game;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.pokowaka.hanabi.action.*;

import java.util.Random;
import java.util.Vector;

public class HanabiSpace extends DiscreteSpace {
    private final Vector<Action> actions = new Vector<>();
    private final Game game;
    private final Random rnd = new Random();

    public HanabiSpace(Game g) {
        super(0);
        this.game = g;
        initializeActions();
    }

    @Override
    public Integer randomAction() {
        Action a = Pass.PASS;
        int idx = 0;
        while (!a.isAllowed()) {
            idx = rnd.nextInt(actions.size());
            a = actions.get(idx);
        }
        assert a.isAllowed();
        return idx;
    }

    @Override
    public void setSeed(int seed) {
        rnd.setSeed(seed);
    }

    @Override
    public Object encode(Integer action) {
        return actions.get(action);
    }

    @Override
    public int getSize() {
        return actions.size();
    }

    @Override
    public Integer noOp() {
        return -1;
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

        for (int i = 0; i < game.getPlayerCount(); i++) {
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
