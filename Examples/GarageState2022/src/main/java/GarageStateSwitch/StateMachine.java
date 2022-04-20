package GarageStateSwitch;

import javafx.animation.AnimationTimer;

public class StateMachine {

States state = States.Closed;
    GarageModel model;;
    AnimationTimer loop;
    private long lastFrame = 0;
    private StateDiagram stateDiagram;

    StateMachine(GarageModel sim){
        model = sim;

        //setup a timer to update the state
        loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastFrame == 0)
                    lastFrame = now;
                else {
                    double delta = (now - lastFrame) / 1000000000.0;
                    lastFrame = now;
                    maintain(delta); //update state maintenence

                }
            }


        };
        loop.start();
    }

    public void setStateDiagram( StateDiagram img){
        stateDiagram = img;
    }

    /**
     * core part of the state machine
     * @param state
     */
    public void setState(States state) {
        /*
         * Exit activities
         */
        switch (this.state) {
            case Opening:
                motorStop();
                break;

            case Closing:
                motorStop();
                break;

            default:
                break;
        }

        /*
         * New state
         */
        this.state = state;

        //forward on the state to anything nested state that needs it
        stateDiagram.setState(this.state);

        /*
         * Entry activites
         */
        switch (this.state) {
            case Opening:
                motorForward();
                break;

            case Closing:
                motorReverse();
                break;

            default:
                break;
        }

    }

    /**
     * A button press cause a state change
     */
    void buttonPressed() {
        switch (state) {
            case Closed:
                setState(States.Opening);
                break;

            case Open:
                if (!model.isSensor()) {
                    setState(States.Closing);
                }
                break;

            case Opening:
                if (!model.isSensor()) {
                    setState(States.Closing);
                }
                break;

            case Closing:
                setState(States.Opening);
                break;

            default:
                break;
        }
    }

    /**
     * \brief The door is now completely open
     */
    private void openComplete() {
        switch (state) {
            case Opening:
                setState(States.Open);
                break;

            default:
                break;
        }

    }

    /**
     * \brief The door is now completely closed
     */
    private void closeComplete() {
        switch (state) {
            case Closing:
                setState(States.Closed);
                break;

            default:
                break;
        }

    }

    /**
     * \brief Called when the sensor is tripped.
     */
    private void whenSensor() {
        switch (state) {
            case Closing:
                setState(States.Opening);
                break;

            default:
                break;
        }
    }

    /**
     * \brief Start the motor in the forward direction
     * <p>
     * Also enables the animation timer
     */
    private void motorForward() {
        model.motorDirection(1);

        //restart the animation loop
        loop.start();
    }

    /**
     * \brief Start the motor in the reverse direction
     * <p>
     * Also enables the animation timer
     */
    private void motorReverse() {
        model.motorDirection(-1);

        //restart the animation loop
        loop.start();
    }

    /**
     * \brief Stop the motor
     * <p>
     * Also shuts down the animation timer
     */
    private void motorStop() {
        model.motorDirection(0);

        //stop the animation loop for efficiency
        loop.stop();
        lastFrame = 0;
    }

    /**
     * \brief Handle timer events
     */
    public void maintain(double delta) {

        if (model.getDoorPosition() == 1) {
            openComplete();
        } else if (model.getDoorPosition() == 0) {
            closeComplete();
        }
        // Handle sensor becoming active
        if (model.isSensor()) {
            whenSensor();
        }

        //the view will handle updating the location of the door
        //view.onDraw(delta);
        model.onDraw(delta);
    }

    /**
     * \brief Handle the View>UML menu option
     */
    void onViewUml() {
        model.toggleUMLImage();

        //force update
        maintain(0);
    }

    /**
     * \brief Handle the View/With Sensor menu option
     */
    void onViewWithsensor()
    {
        model.setSensorsActive(!model.areSensorsActive());

        //force update
        maintain(0);
    }

    States GetState() {
        return state;
    }

    public enum States {Closed, Opening, Open, Closing}



}
