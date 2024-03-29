package edu.sdsmt.stopthetribblesfinnryan.States;

import android.graphics.Color;

import edu.sdsmt.stopthetribblesfinnryan.Control.GameActivity;
import edu.sdsmt.stopthetribblesfinnryan.Control.StateMachine;
import edu.sdsmt.stopthetribblesfinnryan.Model.Game;

public class LowHungerNoBureaucrat extends State {
    public LowHungerNoBureaucrat(Game game, GameActivity activity, StateMachine machine) {
        super(game, activity, machine);
    }

    @Override
    public void doTask() {
        if (game.isBureaucratPresent())
            machine.setState(StateMachine.StateEnum.LOWYES);
        else if (game.getHunger() > 3)
            machine.setState(StateMachine.StateEnum.MIDNO); // GRADING: TRANSITION_TO_MID

        if (game.isLost() || game.isWon())  // GRADING: DIALOG
            activity.endGame(game.isWon());
    }

    @Override
    public void startTask() {
        distract.setEnabled(false);   // GRADING: DISABLE
        activity.setFillPaint(Color.LTGRAY);
        activity.setOutlinePaint(GameActivity.defaultColor);
    }

    @Override
    public int maintenanceTask() {
        return 25;
    }   // GRADING: COLLECT
}
