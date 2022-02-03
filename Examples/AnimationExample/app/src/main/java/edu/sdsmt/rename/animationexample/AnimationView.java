package edu.sdsmt.rename.animationexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

public class AnimationView extends View {

    // Indicate the center of the items when in portrait mode
    private static final float LINES_CENTER_X_PORT = 0.166f;
    private static final float LINES_CENTER_Y_PORT = 0.166f;

    // Indicate the center of the items when in landscape mode
    private static final float LINES_CENTER_X_LAND = 0.166f;
    private static final float LINES_CENTER_Y_LAND = 0.166f;

    private static final float LINES_RADIUS = 0.15f;

    private static final float LINES_SPIN_RATE = (float)Math.PI / 2;

    // Indicate the center of the items when in portrait mode
    private static final float BOX_CENTER_X_PORT = 0.166f + 0.333f;
    private static final float BOX_CENTER_Y_PORT = 0.166f;

    // Indicate the center of the items when in landscape mode
    private static final float BOX_CENTER_X_LAND = 0.166f;
    private static final float BOX_CENTER_Y_LAND = 0.166f + 0.333f;

    private static final float BOX_SIZE = 0.2f;

    private static final float BOX_SPIN_RATE = -80.0f;

    // Indicate the center of the items when in portrait mode
    private static final float TEXT_CENTER_X_PORT = 0.166f + 0.666f;
    private static final float TEXT_CENTER_Y_PORT = 0.166f;

    // Indicate the center of the items when in landscape mode
    private static final float TEXT_CENTER_X_LAND = 0.166f;
    private static final float TEXT_CENTER_Y_LAND = 0.166f + 0.666f;

    // Height of the text
    private static final float TEXT_SIZE = 0.05f;

    private static final float TEXT_SPIN_RATE = -60.0f;

    private Paint linePaint;

    private Paint boxPaint;

    private Paint textPaint;

    private long lastTime = 0;

    /**
     * Set true if we want to draw spinning lines
     */
    private boolean drawLines = false;

    private boolean drawBox = false;

    private boolean drawText = false;

    private boolean drawGnome = false;

    /**
     * The angle we draw the lines at
     */
    private float linesAngle = 0;

    private float boxAngle = 0;

    private float textAngle = 0;

    private final AnimationBouncyGnome gnome = new AnimationBouncyGnome();

    public AnimationView(Context context) {
        super(context);
        init(context);
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnimationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boxPaint.setColor(0xff008000);
        boxPaint.setStyle(Paint.Style.STROKE);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lastTime = SystemClock.uptimeMillis();

        gnome.init(context);
    }


    /**
     * Draw the view
     * @param canvas Canvas to draw on
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        // Determine the time step
        long time = SystemClock.uptimeMillis();
        float delta = (time - lastTime) * 0.001f;
        lastTime = time;

        // Animation updates
        linesAngle += LINES_SPIN_RATE * delta;
        boxAngle += BOX_SPIN_RATE * delta;
        textAngle += TEXT_SPIN_RATE * delta;

        if(drawLines) {
            drawLines(canvas);
        }

        if(drawBox) {
            drawBox(canvas);
        }

        if(drawText) {
            drawText(canvas);
        }

        if(drawGnome) {
            gnome.update(delta);
            gnome.draw(canvas);
        }

        if(drawLines || drawBox || drawText || drawGnome) {
            postInvalidate();
        }
    }

    private void drawLines(Canvas canvas) {
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        float linesCENTER_X, linesCENTER_Y, linesRadius;

        if(hit > wid) {
            // We are in portrait mode
            linesCENTER_X = LINES_CENTER_X_PORT * wid;
            linesCENTER_Y = LINES_CENTER_Y_PORT * hit;
            linesRadius = LINES_RADIUS * wid;
        } else {
            // We are in landscape mode
            linesCENTER_X = LINES_CENTER_X_LAND * wid;
            linesCENTER_Y = LINES_CENTER_Y_LAND * hit;
            linesRadius = LINES_RADIUS * hit;
        }

        // Loop over the lines
        for(int i=0; i<4;  i++) {
            // Compute the angle
            double a = i / 8.0 * Math.PI * 2 + linesAngle;

            float x1 = linesCENTER_X + (float)Math.cos(a) * linesRadius;
            float y1 = linesCENTER_Y + (float)Math.sin(a) * linesRadius;
            float x2 = linesCENTER_X + (float)Math.cos(a) * -linesRadius;
            float y2 = linesCENTER_Y + (float)Math.sin(a) * -linesRadius;

            canvas.drawLine(x1,  y1,  x2,  y2, linePaint);
        }

    }

    private void drawBox(Canvas canvas) {
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        float boxCENTER_X, boxCENTER_Y, boxSize;

        if(hit > wid) {
            // We are in portrait mode
            boxCENTER_X = BOX_CENTER_X_PORT * wid;
            boxCENTER_Y = BOX_CENTER_Y_PORT * hit;
            boxSize = BOX_SIZE * wid;
        } else {
            // We are in landscape mode
            boxCENTER_X = BOX_CENTER_X_LAND * wid;
            boxCENTER_Y = BOX_CENTER_Y_LAND * hit;
            boxSize = BOX_SIZE * hit;
        }

        canvas.save();
        canvas.translate(boxCENTER_X,  boxCENTER_Y);
        canvas.rotate(boxAngle);
        canvas.drawRect(-boxSize/2, -boxSize/2, boxSize/2, boxSize/2, boxPaint);
        canvas.restore();
    }

    private void drawText(Canvas canvas) {
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        float textCENTER_X, textCENTER_Y, textSize;

        if(hit > wid) {
            // We are in portrait mode
            textCENTER_X = TEXT_CENTER_X_PORT * wid;
            textCENTER_Y = TEXT_CENTER_Y_PORT * hit;
            textSize = TEXT_SIZE * wid;
        } else {
            // We are in landscape mode
            textCENTER_X = TEXT_CENTER_X_LAND * wid;
            textCENTER_Y = TEXT_CENTER_Y_LAND * hit;
            textSize = TEXT_SIZE * hit;
        }

        String textSpinning = "Gnomes Rule!";
        float textWid = textPaint.measureText(textSpinning);

        textPaint.setTextSize(textSize);
        canvas.save();
        canvas.translate(textCENTER_X,  textCENTER_Y);
        canvas.rotate(textAngle);
        canvas.drawText(textSpinning, -textWid/2, textSize/2, textPaint);
        canvas.restore();
    }

    public boolean isDrawBox() {
        return drawBox;
    }

    public void setDrawBox(boolean drawBox) {
        this.drawBox = drawBox;
        invalidate();
    }


    public boolean isDrawLines() {
        return drawLines;
    }

    public void setDrawLines(boolean drawLines) {
        this.drawLines = drawLines;
        invalidate();
    }

    public boolean isDrawGnome() {
        return drawGnome;
    }

    public void setDrawGnome(boolean drawGnome) {
        this.drawGnome = drawGnome;
        invalidate();
    }

    public boolean isDrawText() {
        return drawText;
    }

    public void setDrawText(boolean drawText) {
        this.drawText = drawText;
        invalidate();
    }


}
