package GarageStateClass;

import javafx.animation.Animation;

import java.util.Timer;
import java.util.TimerTask;

public class StateMachine {
    public enum StateEnum {Closed, Opening, Open, Closing}
    private StateEnum state = StateEnum.Closed;
    private State[] stateArray = null;

    StateMachine( GarageModel model, StateDiagram stateDiagram ) {

        stateArray = new State[]{
            new StateClosed( model,stateDiagram , this ),
            new StateOpening( model,stateDiagram , this ),
            new StateOpen( model,stateDiagram , this ),
            new StateClosing( model,stateDiagram , this )
        };

        //setup a timer to update the state
        loop = new Timer();
        StartAnimation();

    }

    /**
     * core part of the state machine
     *
     * @param state
     */
    public void setState( StateEnum state ) {
        /*
         * Exit activities
         */
        stateArray[this.state.ordinal()].exitActivity( );

        /*
         * New state
         */
        this.state = state;


        //forward on the state to anything nested state that needs it
        //I could have made a second state machine entirely for the diagram

        /*
         * Entry activities
         */
        stateArray[this.state.ordinal()].entryActivity( );
    }


    StateEnum GetState() {
        return state;
    }

//--------------------------------------------------------------------------------------------------
    //the "push" part of the state machine with events originating outside the machine, but the
    //response changes with the STATE

    /**
     * A button press cause a state change
     */
    void buttonPressed() {
        //forward it on for the state to decide
        stateArray[state.ordinal()].buttonPressed( );
        doMaintenanceTask(0);
    }

    //the "pull" part of the state machine where there can be a MODEL
    // change due to an event that is NOT affected by the state
    /**
     * \brief refresh if the model has been updated
     */
    public void doMaintenanceTask( double delta ) {
        stateArray[state.ordinal()].doTask( delta );
    }


//--------------------------------------------------------------------------------------------------
    //this animation code could be in another class. In this case, the state machine start all
    // the maintenance updates, so it makes sense to make it a nested class

    //this code should, in theory, work in both Java 11 and Android
    //Android preferred version is TimeAnimator or Android's Handler  (NOT Java's Handler)
    private Timer loop;
    private long lastFrame = 0;
    private AnimationTimer updateTimer = new AnimationTimer();
    private boolean ANIMATE_FAST_AS_POSSIBLE = false; //as fast as possible, or standard 30fps
    public void StartAnimation() {
        loop = new Timer();
        updateTimer = new AnimationTimer();
        if(ANIMATE_FAST_AS_POSSIBLE)
            loop.schedule( updateTimer , 0 ); // as fast as possible
        else
            loop.scheduleAtFixedRate( updateTimer , 0, 30 ); //~30 frames per second
    }

    public void StopAnimation() {
        loop.cancel();
        lastFrame = 0;
    }

    //animation maintenance
    public class AnimationTimer extends TimerTask {

        @Override
        public void run() {
            long now = System.currentTimeMillis();
            if( lastFrame == 0 ) {
                lastFrame = now;
            } else {
                double delta = ( now - lastFrame ) / 1000.0;
                lastFrame = now;
                doMaintenanceTask( delta ); //update state maintenance
            }

            if(ANIMATE_FAST_AS_POSSIBLE) {
                loop = new Timer();
                updateTimer = new AnimationTimer();
                loop.schedule( updateTimer, 0 ); // as fast as possible
            }
        }
    }
//--------------------------------------------------------------------------------------------------






}
