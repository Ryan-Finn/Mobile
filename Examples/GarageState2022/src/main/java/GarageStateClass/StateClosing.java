package GarageStateClass;

public class StateClosing extends State {


    public StateClosing( GarageModel model, StateDiagram diagram, StateMachine machine ) {
        super( model, diagram, machine );
    }

    //stop motor
    @Override
    public void exitActivity() {
        model.motorDirection( 0 );
        machine.StopAnimation();
    }

    //turn on and reverse motor
    @Override
    public void entryActivity() {
        model.motorDirection( -1 );
        machine.StartAnimation();
    }

    @Override
    public void doTask( double delta ) {

        if( model.getDoorPosition( ) == 0 ) {
            machine.setState( StateMachine.StateEnum.Closed );
        }

        // Handle sensor becoming active
        if( model.isSensor( ) ) {
            machine.setState( StateMachine.StateEnum.Opening );
        }

        //the view will handle updating the location of the door
        model.onDraw( delta );
        if (model.isShowUML())
            diagram.setPoint(331, 221);
    }

    @Override
    public void buttonPressed() {
        machine.setState( StateMachine.StateEnum.Opening );
    }
}
