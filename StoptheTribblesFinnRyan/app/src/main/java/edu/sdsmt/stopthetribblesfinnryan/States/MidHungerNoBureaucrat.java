package edu.sdsmt.stopthetribblesfinnryan.States;

import android.graphics.Color;

import edu.sdsmt.stopthetribblesfinnryan.Control.GameActivity;
import edu.sdsmt.stopthetribblesfinnryan.Control.StateMachine;
import edu.sdsmt.stopthetribblesfinnryan.Model.Game;

public class MidHungerNoBureaucrat extends State {
    public MidHungerNoBureaucrat(Game game, GameActivity activity, StateMachine machine) {
        super(game, activity, machine);
    }

    @Override
    public void doTask() {
        if (game.isBureaucratPresent())
            machine.setState(StateMachine.StateEnum.MIDYES);
        else if (game.getHunger() < 4)
            machine.setState(StateMachine.StateEnum.LOWNO);
        else if (game.getHunger() > 7)
            machine.setState(StateMachine.StateEnum.HIGHNO);

        if (game.isLost() || game.isWon())
            activity.endGame(game.isWon());
    }

    @Override
    public void startTask() {
        distract.setEnabled(false);
        activity.setFillPaint(Color.GRAY);
        activity.setOutlinePaint(GameActivity.defaultColor);
    }

    @Override
    public int maintenanceTask() {
        return 15;
    }   // GRADING: COLLECT
}
