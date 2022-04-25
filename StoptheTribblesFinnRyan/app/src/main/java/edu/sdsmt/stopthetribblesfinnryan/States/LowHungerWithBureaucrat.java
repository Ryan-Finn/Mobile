package edu.sdsmt.stopthetribblesfinnryan.States;

import android.graphics.Color;

import edu.sdsmt.stopthetribblesfinnryan.Control.GameActivity;
import edu.sdsmt.stopthetribblesfinnryan.Control.StateMachine;
import edu.sdsmt.stopthetribblesfinnryan.Model.Game;

public class LowHungerWithBureaucrat extends State {
    public LowHungerWithBureaucrat(Game game, GameActivity activity, StateMachine machine) {
        super(game, activity, machine);
    }

    @Override
    public void doTask() {
        if (!game.isBureaucratPresent())    // GRADING: NO_BUREAUCRAT
            machine.setState(StateMachine.StateEnum.HIGHNO);
        else if (game.getHunger() > 3)
            machine.setState(StateMachine.StateEnum.MIDYES); // GRADING: TRANSITION_TO_MID

        if (game.isLost() || game.isWon())
            activity.endGame(game.isWon());
    }

    @Override
    public void startTask() {
        distract.setEnabled(true);   // GRADING: ENABLE
        activity.setFillPaint(Color.LTGRAY);
        activity.setOutlinePaint(Color.RED);
    }

    @Override
    public int maintenanceTask() {
        return 10;
    }   // GRADING: COLLECT
}
