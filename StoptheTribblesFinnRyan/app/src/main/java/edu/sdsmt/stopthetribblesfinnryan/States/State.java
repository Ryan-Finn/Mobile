package edu.sdsmt.stopthetribblesfinnryan.States;

import edu.sdsmt.stopthetribblesfinnryan.Control.GameActivity;
import edu.sdsmt.stopthetribblesfinnryan.Control.StateMachine;
import edu.sdsmt.stopthetribblesfinnryan.Model.Game;

public abstract class State {
    protected Game game;
    protected GameActivity activity;
    protected StateMachine machine;

    public State(Game game, GameActivity activity, StateMachine machine) {
        this.game = game;
        this.activity = activity;
        this.machine = machine;
    }

    public abstract void doTask();
    public abstract void endTask();
    public abstract void startTask();
    public abstract int maintenanceTask();
}
