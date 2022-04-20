package GarageStateClass;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    //view view acts as a other part of the controller
    @FXML
    private GarageView garage;

    //model
    private StateMachine state;
    private GarageModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set up all the links
        model = new GarageModel(garage);
        state = new StateMachine(model, garage.getStateDiagram());
        //state.setStateDiagram(garage.getStateDiagram());
        garage.setSim(model);
        state.setState( StateMachine.StateEnum.Closed);

        //give the buttons and cliack area's their controllers
        garage.setGarageButtonEvent(new GarageButtonClick());
        garage.setCatPlaceEvent(new CatAreaClick());
    }

    public void onClose(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
     * menu option to toggle the sensor
     * @param actionEvent
     */
    public void onSensor(ActionEvent actionEvent) {
        model.setSensorsActive( !model.areSensorsActive( ) );

        //if using the Observe pattern, the above call would have trigger
        //the event in the state machine class
        state.doMaintenanceTask( 0 );
    }

    /**
     * menu option to toggle the state machine
     * @param actionEvent
     */
    public void onStateMachine(ActionEvent actionEvent) {
        model.toggleUMLImage( );

        //if using the Observe pattern, the above call would have trigger
        //the event in the state machine class
        state.doMaintenanceTask( 0 );
    }

    public class GarageButtonClick implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY) {
                if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    model.pressDoorButton();

                    //if using the Observe pattern, the above call would have trigger
                    //the event in the state machine class
                    state.buttonPressed();
                }else
                {
                    model.releaseDoorButton();
                }
            }
        }

    }

    public class CatAreaClick implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY) {
                model.toggleCat();
                state.doMaintenanceTask(0);
            }
        }
    }
}
