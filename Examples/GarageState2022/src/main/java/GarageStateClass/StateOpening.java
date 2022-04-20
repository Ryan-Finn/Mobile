package GarageStateClass;

public class StateOpening extends State {


    public StateOpening( GarageModel model, StateDiagram diagram, StateMachine machine ) {
        super( model, diagram, machine );
    }

    //stop motor
    @Override
    public void exitActivity() {
        model.motorDirection( 0 );
        machine.StopAnimation();
    }

    //turn on motor
    @Override
    public void entryActivity() {
        model.motorDirection( 1 );
        machine.StartAnimation();
    }

    @Override
    public void doTask(double delta) {
        if( model.getDoorPosition( ) == 1 ) {
            machine.setState( StateMachine.StateEnum.Open );
        }
        //the view will handle updating the location of the door
        model.onDraw( delta );
        if (model.isShowUML())
            diagram.setPoint(331, 38);
    }

    @Override
    public void buttonPressed() {
        if( !model.isSensor( ) ) {
            machine.setState( StateMachine.StateEnum.Closing );
        }
    }
}
