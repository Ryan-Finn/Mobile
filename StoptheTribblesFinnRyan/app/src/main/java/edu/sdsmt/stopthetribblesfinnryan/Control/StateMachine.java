package edu.sdsmt.stopthetribblesfinnryan.Control;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

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
    private final State[] stateArray;

    public StateMachine(Game game, GameActivity activity) {
        stateArray = new State[]{
                new LowHungerNoBureaucrat(game, activity, this),
                new LowHungerWithBureaucrat(game, activity, this),
                new MidHungerNoBureaucrat(game, activity, this),
                new MidHungerWithBureaucrat(game, activity, this),
                new HighHungerNoBureaucrat(game, activity, this),
                new HighHungerWithBureaucrat(game, activity, this)
        };
    }

    public void setState(StateEnum newState) {
        Log.d("STATE END", getCurrentStateName());
        stateArray[state.ordinal()].endTask();
        state = newState;
        Log.d("STATE START", getCurrentStateName());
        stateArray[state.ordinal()].startTask();
    }

    public void onUpdate() {
        Log.d("STATE UPDATE", getCurrentStateName());
        stateArray[state.ordinal()].doTask();
    }

    public int getCollection() {
        Log.d("STATE MAINTENANCE", getCurrentStateName());
        return stateArray[state.ordinal()].maintenanceTask();
    }

    public void saveInstanceState(@NonNull Bundle bundle) {
        bundle.putInt("machine.state", state.ordinal());
    }

    public void restoreInstanceState(@NonNull Bundle bundle) {
        setState(StateEnum.values()[bundle.getInt("machine.state")]);
    }

    public String getCurrentStateName() {
        return stateArray[state.ordinal()].getClass().getName();
    }
}
