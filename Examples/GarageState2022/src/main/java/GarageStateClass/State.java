package GarageStateClass;

public abstract class State {
    protected StateDiagram diagram;
    protected GarageModel model;
    protected StateMachine machine;
    public State( GarageModel model, StateDiagram diagram, StateMachine machine){
        this.model = model;
        this.machine = machine;
        this.diagram = diagram;
    }

    public abstract void exitActivity();
    public abstract void entryActivity();
    public abstract void doTask(double delta);
    public abstract void buttonPressed();
}
