package GarageStateSwitch;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    //view view acts as a onther part of the controller
    public GarageView garage;
    public CheckMenuItem sensorMenu;
    public CheckMenuItem sensorState;

    //model
    private StateMachine state;
    private GarageModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set up all the links
        model = new GarageModel(garage);
        state = new StateMachine(model);
        state.setStateDiagram(garage.getStateDiagram());
        garage.setSim(model);
        state.setState(StateMachine.States.Closed);

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
        state.onViewWithsensor();
    }

    /**
     * menu option to toggle the state machine
     * @param actionEvent
     */
    public void onStateMachine(ActionEvent actionEvent) {
        state.onViewUml();
    }

    public class GarageButtonClick implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY) {
                if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    model.pressDoorButton();
                    state.buttonPressed();
                }else
                {
                    model.releaseDoorButton();;
                }
                state.maintain(0);
            }
        }

    }

    public class CatAreaClick implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY) {
                model.toggleCat();
                state.maintain(0);
            }
        }
    }
}
