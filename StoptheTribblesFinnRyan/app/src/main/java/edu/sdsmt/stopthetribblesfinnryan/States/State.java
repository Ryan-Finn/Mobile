package edu.sdsmt.stopthetribblesfinnryan.States;

import edu.sdsmt.stopthetribblesfinnryan.Control.StateMachine;

public abstract class State {
    protected StateMachine machine;
    public State(StateMachine machine){
        this.machine = machine;
    }

    public abstract void exitActivity();
    public abstract void entryActivity();
    public abstract void doTask(double delta);
    public abstract void buttonPressed();
}
