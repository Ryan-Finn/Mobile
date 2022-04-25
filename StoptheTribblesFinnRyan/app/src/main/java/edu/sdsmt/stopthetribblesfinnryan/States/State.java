package edu.sdsmt.stopthetribblesfinnryan.States;

import android.widget.Button;

import edu.sdsmt.stopthetribblesfinnryan.Control.GameActivity;
import edu.sdsmt.stopthetribblesfinnryan.Control.StateMachine;
import edu.sdsmt.stopthetribblesfinnryan.Model.Game;
import edu.sdsmt.stopthetribblesfinnryan.R;

public abstract class State {
    protected final Game game;
    protected final GameActivity activity;
    protected final StateMachine machine;
    protected final Button distract;

    public State(Game game, GameActivity activity, StateMachine machine) {
        this.game = game;
        this.activity = activity;
        this.machine = machine;
        this.distract = activity.findViewById(R.id.distractBtn);
    }

    public abstract void doTask();
    public abstract void startTask();
    public abstract int maintenanceTask();
}
