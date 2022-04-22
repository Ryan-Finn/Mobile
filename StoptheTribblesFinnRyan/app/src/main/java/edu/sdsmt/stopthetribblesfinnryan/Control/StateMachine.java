package edu.sdsmt.stopthetribblesfinnryan.Control;

import edu.sdsmt.stopthetribblesfinnryan.Model.Game;
import edu.sdsmt.stopthetribblesfinnryan.States.HighHungerNoBureaucrat;
import edu.sdsmt.stopthetribblesfinnryan.States.HighHungerWithBureaucrat;
import edu.sdsmt.stopthetribblesfinnryan.States.LowHungerNoBureaucrat;
import edu.sdsmt.stopthetribblesfinnryan.States.LowHungerWithBureaucrat;
import edu.sdsmt.stopthetribblesfinnryan.States.MidHungerNoBureaucrat;
import edu.sdsmt.stopthetribblesfinnryan.States.MidHungerWithBureaucrat;
import edu.sdsmt.stopthetribblesfinnryan.States.State;

public class StateMachine {
    public enum StateEnum {LOWNO, LOWYES, MIDNO, MIDYES, HIGHNO, HIGHYES}
    private StateEnum state = StateEnum.LOWNO;
    private State[] stateArray;

    public StateMachine(Game game, GameActivity ma_mock) {
        stateArray = new State[]{
                new LowHungerNoBureaucrat(this),
                new LowHungerWithBureaucrat(this),
                new MidHungerNoBureaucrat(this),
                new MidHungerWithBureaucrat(this),
                new HighHungerNoBureaucrat(this),
                new HighHungerWithBureaucrat(this),
        };
    }

    public void onUpdate() {
    }

    public String getCurrentStateName() {
        return "HI";
    }

    public int getCollection() {
        return 1;
    }
}
