package GarageStateSwitch;

public class GarageModel {

    /// Maximum speed the door opens or closes
    public static final double MAX_SPEED = 0.5;
    private double doorPosition;               ///< Door position, 0-1, where 0 means closed, 1 means open
    private boolean showUML = true;
    private boolean doorButtonPressed = false;    ///< True if the door button is currently pressed
    private boolean catActive = false;            ///< True if the cat is currently in the sensors
    private boolean sensors = false;              ///< Are sensors implemented?
    private double motorSpeed = 0;             ///< Motor direction, -1 to 1
    private GarageView view;
    /**
     * \brief Constructor
     */
    GarageModel(GarageView g) {
        view = g;
        doorPosition = 0;

        // To ensure the time system is initialized
        motorDirection(0);
    }

    public boolean isDoorButtonPressed() {
        return doorButtonPressed;
    }

    public boolean isShowUML() {
        return showUML;
    }

    public boolean isCatActive() {
        return catActive;
    }

    public double getMotorSpeed() {
        return motorSpeed;
    }

    public void setMotorSpeed(double motorSpeed) {
        this.motorSpeed = motorSpeed;
    }

    /**
     * \brief Calling this function presses the door button
     */
    void pressDoorButton() {
        doorButtonPressed = true;
    }

    /**
     * \brief Calling this function releases the door button
     */
    void releaseDoorButton() {
        doorButtonPressed = false;
    }

    /**
     * \brief The motor speed
     * \param speed A speed from -1 = closing to 1 = opening, where 0 is stopped
     */
    void motorDirection(double dir) {
        motorSpeed = dir;
    }

    /**
     * do I show the cat?
     */
    void toggleCat() {
        if (sensors) {
            catActive = !catActive;
            //view.onDraw(0);
        }
    }

    /**
     * \brief Is the sensor tripped?
     * \returns true if sensor is active (cat is there)
     */
    boolean isSensor() {
        return catActive;
    }

    /**
     * \brief Set the sensors active state
     * \param s true to set sensors active
     */
    void setSensorsActive(boolean s) {
        sensors = s;
        catActive = false;
    }

    /**
     * \brief Get the sensors active state
     * \returns true if sensors are active
     */
    boolean areSensorsActive() {
        return sensors;
    }

    double getDoorPosition() {
        return doorPosition;
    }

    public void setDoorPosition(double doorPosition) {
        this.doorPosition = doorPosition;
    }

    public void onDraw(double delta) {
       view.onDraw(delta);
    }

    public void toggleUMLImage() {
        showUML = !showUML;
    }


}