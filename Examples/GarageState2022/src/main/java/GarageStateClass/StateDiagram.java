package GarageStateClass;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;

public class StateDiagram {


    Image UML1;     ///< UML diagram 1
    Image UML2;     ///< UML diagram 2
    Image ptr;      ///< State Pointer
    Point p = new Point(0, 0);

    StateMachine.StateEnum state;

    StateDiagram()
    {
        UML1 = new Image( "uml1.png");
        UML2 = new Image( "uml2.png");
        ptr = new Image( "JenniferHead.png");

    }


    void setState( StateMachine.StateEnum s) {state = s; }

    /*
    draw the diagram at the location specified
     */
    public void onDraw(GraphicsContext graphics, int x, int y, boolean mSensors) {

        //decide which version to draw
        if (mSensors)
        {
            graphics.drawImage(UML2, x, y,
                    UML2.getWidth(), UML2.getHeight());
        }
        else
        {
            graphics.drawImage(UML1, x, y,
                    UML1.getWidth(), UML1.getHeight());
        }

        //draw the cat pointer
        graphics.drawImage(ptr,
                (int)(x + p.x - ptr.getWidth() / 2),
                (int)(y + p.y - ptr.getHeight() / 2),
                ptr.getWidth(), ptr.getHeight());
    }


    public void setPoint( int x, int y ) {
        p.x = x;
        p.y = y;
    }
}
