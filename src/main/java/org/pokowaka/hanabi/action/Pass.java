package org.pokowaka.hanabi.action;

/**
 * Created by jansene on 10/5/17.
 */

public class Pass extends Action {

    public static final Pass PASS = new Pass();

    private Pass() {
        super(null);
    }

    @Override
    public boolean isAllowed() {
        return false;
    }

    @Override
    public void play() {

    }
}
