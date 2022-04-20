package GarageStateClass;

public class StateOpen extends State {


    public StateOpen( GarageModel model, StateDiagram diagram, StateMachine machine ) {
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
        //the view will handle updating the location of the door
        model.onDraw( delta );
        if (model.isShowUML())
            diagram.setPoint(602, 110);
    }

    @Override
    public void buttonPressed() {
        if( !model.isSensor( ) ) {
            machine.setState( StateMachine.StateEnum.Closing );
        }
    }
}
