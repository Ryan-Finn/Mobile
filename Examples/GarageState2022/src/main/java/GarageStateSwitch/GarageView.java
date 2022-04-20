package GarageStateSwitch;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class GarageView extends Pane {

    /// Diagram drawing X location
    final int DIAGRAM_X = 10;
    /// Diagram drawing Y location
    final int DIAGRAM_Y = 150;

    /// The position of the cat
    final Point CAT_POSITION = new Point(400, 422);
    final Point DOOR_POSITION = new Point(100, 65);
    private StateDiagram stateDiagram;
    private Rectangle regionButton;        ///< Region with the open/close button
    private Rectangle regionCat;           ///< Region to click for the cat image
    private GarageModel model;
    private Canvas canvas = null;
    private Image garage;       ///< Garage image
    private Image garageFore;   ///< Garage foreground image
    private Image door;         ///< Garage door image
    private Image doorLight;    ///< Door button light image
    private Image cat;          ///< Cat image

    /**
     * needed to make the scene builder happy
     */
    public GarageView() {
        super();
        initialize();
    }

    /**
     * needed to make the scene builder happy
     */
    public GarageView(Node... children) {
        super(children);
        initialize();
    }

    public void setSim(GarageModel sim) {
        model = sim;
    }

    public StateDiagram getStateDiagram() {
        return stateDiagram;
    }

    public void setStateDiagram( StateDiagram stateDiagram) {
        this.stateDiagram = stateDiagram;
    }

    public void initialize() {
        stateDiagram = new StateDiagram();
        canvas = new Canvas(200, 200);
        regionButton = new Rectangle(710, 153, 50, 200);
        regionButton.setFill(Color.TRANSPARENT);
        regionCat = new Rectangle(100, 400, 469, 85);
        regionCat.setFill(Color.TRANSPARENT);

        getChildren().add(canvas);
        getChildren().add(regionButton);
        getChildren().add(regionCat);

        //
        // Load the view graphics
        //
        garage = new Image("Garage.png");
        garageFore = new Image("GarageFore.png");
        door = new Image("Door.png");
        doorLight = new Image("DoorLight.png");
        cat = new Image("Jennifer.png");
    }

    /**
     * This forces the drawing area to use the full area
     */
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        final double x = snappedLeftInset();
        final double y = snappedTopInset();

        final double w = snapSizeX(getWidth()) - x - snappedRightInset();
        final double h = snapSizeY(getHeight()) - y - snappedBottomInset();

        canvas.setLayoutX(x);
        canvas.setLayoutY(y);
        canvas.setWidth(w);
        canvas.setHeight(h);
    }


    public void onDraw(double elapsed) {
        GraphicsContext graphics = canvas.getGraphicsContext2D();

        graphics.setFill(Color.BLACK);

        // Handle any motor activity
        if (model.getMotorSpeed() != 0) {

            model.setDoorPosition(model.getDoorPosition() + model.getMotorSpeed() * GarageModel.MAX_SPEED * elapsed);
            if (model.getDoorPosition() > 1) {
                model.setDoorPosition(1);
            } else if (model.getDoorPosition() < 0) {
                model.setDoorPosition(0);
            }
        }

        // onDraw the background image
        graphics.drawImage(garage, 0, 0,
                garage.getWidth(), garage.getHeight());

        if (model.isCatActive()) {
            // onDraw the cat
            graphics.drawImage(cat, CAT_POSITION.x, CAT_POSITION.y,
                    cat.getWidth(), cat.getHeight());
        }

        // onDraw the door
        graphics.drawImage(door, DOOR_POSITION.x,
                (int) (DOOR_POSITION.y - model.getDoorPosition() * door.getHeight()),
                door.getWidth(), door.getHeight());

        // onDraw the foreground image
        graphics.drawImage(garageFore, 0, 0,
                garageFore.getWidth(), garageFore.getHeight());

        if (model.isDoorButtonPressed()) {
            // onDraw the door light
            graphics.drawImage(doorLight, 0, 0,
                    doorLight.getWidth(), doorLight.getHeight());
        }

        if (model.isShowUML())
            stateDiagram.onDraw(graphics, DIAGRAM_X, DIAGRAM_Y, model.areSensorsActive());

    }

    public void setGarageButtonEvent(EventHandler<MouseEvent> garageButtonClick) {
        regionButton.addEventFilter(MouseEvent.MOUSE_PRESSED, garageButtonClick);
        regionButton.addEventFilter(MouseEvent.MOUSE_RELEASED, garageButtonClick);
    }

    public void setCatPlaceEvent(EventHandler<MouseEvent> catAreaClick) {
        regionCat.addEventFilter(MouseEvent.MOUSE_CLICKED, catAreaClick);
    }

}





