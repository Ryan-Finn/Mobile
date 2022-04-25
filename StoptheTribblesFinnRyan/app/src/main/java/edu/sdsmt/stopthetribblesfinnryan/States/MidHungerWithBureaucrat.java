package edu.sdsmt.stopthetribblesfinnryan.States;

import android.graphics.Color;

import edu.sdsmt.stopthetribblesfinnryan.Control.GameActivity;
import edu.sdsmt.stopthetribblesfinnryan.Control.StateMachine;
import edu.sdsmt.stopthetribblesfinnryan.Model.Game;

public class MidHungerWithBureaucrat extends State {
    public MidHungerWithBureaucrat(Game game, GameActivity activity, StateMachine machine) {
        super(game, activity, machine);
    }

    @Override
    public void doTask() {
        if (!game.isBureaucratPresent())
            machine.setState(StateMachine.StateEnum.HIGHNO);
        else if (game.getHunger() < 4)
            machine.setState(StateMachine.StateEnum.LOWYES);
        else if (game.getHunger() > 7)
            machine.setState(StateMachine.StateEnum.HIGHYES);

        if (game.isLost() || game.isWon())
            activity.endGame(game.isWon());
    }

    @Override
    public void startTask() {
        distract.setEnabled(true);
        activity.setFillPaint(Color.GRAY);
        activity.setOutlinePaint(Color.RED);
    }

    @Override
    public int maintenanceTask() {
        return 5;
    }   // GRADING: COLLECT
}
