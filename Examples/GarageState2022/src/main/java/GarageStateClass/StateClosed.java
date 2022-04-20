package GarageStateClass;

public class StateClosed extends State {


    public StateClosed( GarageModel model, StateDiagram diagram, StateMachine machine ) {
        super( model, diagram, machine );
    }

    @Override
    public void exitActivity() {
        //nothing to do
    }

    @Override
    public void entryActivity() {

    }

    @Override
    public void doTask( double delta ) {
        // Handle sensor becoming active
        if( model.isSensor( ) ) {
            machine.setState( StateMachine.StateEnum.Opening );
        }

        //the view will handle updating the location of the door
        model.onDraw( delta );
        diagram.setPoint( 108, 110 );


    }

    @Override
    public void buttonPressed() {
        machine.setState( StateMachine.StateEnum.Opening );
    }

}
