package edu.sdsmt.stopthetribblesfinnryan.States;

import android.graphics.Color;

import edu.sdsmt.stopthetribblesfinnryan.Control.GameActivity;
import edu.sdsmt.stopthetribblesfinnryan.Control.StateMachine;
import edu.sdsmt.stopthetribblesfinnryan.Model.Game;

public class HighHungerWithBureaucrat extends State {
    public HighHungerWithBureaucrat(Game game, GameActivity activity, StateMachine machine) {
        super(game, activity, machine);
    }

    @Override
    public void doTask() {
        if (!game.isBureaucratPresent())
            machine.setState(StateMachine.StateEnum.HIGHNO);
        else if (game.getHunger() < 8)
            machine.setState(StateMachine.StateEnum.MIDYES);

        if (game.isLost() || game.isWon())
            activity.endGame(game.isWon());
    }

    @Override
    public void endTask() {

    }

    @Override
    public void startTask() {
        activity.setDistract(game.isBureaucratPresent());
        activity.setFillPaint(Color.DKGRAY);
        activity.setOutlinePaint(Color.RED);
    }

    @Override
    public int maintenanceTask() {
        return 0;
    }
}
