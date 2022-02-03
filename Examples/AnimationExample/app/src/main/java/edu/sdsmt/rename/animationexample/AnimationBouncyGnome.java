package edu.sdsmt.rename.animationexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * The incredible Bouncy Gnome animation
 * @author Charles B. Owen
 */
public class AnimationBouncyGnome {
    // Indicate dimensions in portrait mode
    private static final float LINES_CENTER_X_PORT = 0.5f;
    private final static float GNOME_HEIGHT_PORT = 0.6f;

    // Indicate dimensions in landscape mode
    private static final float LINES_CENTER_X_LAND = 0.666f;
    private final static float GNOME_HEIGHT_LAND = 0.8f;

    /**
     * How long does it take the gnome to complete one cycle?
     */
    private final static float GNOME_SPEED = 0.5f;

    /**
     * What is the greatest percentage to add to the height?
     */
    private final static float HEIGHT_MAX = 0.2f;

    /**
     * What is the maximu X skew?
     */
    private final static float SKEW_MAX = 0.15f;

    /**
     * The bitmap for the gnome
     */
    private Bitmap gnome;

    /**
     * This value increments at a rate of 1 per cycle
     */
    private float gnomeCycle = 0;

    /**
     * Initialization. Loads the gnome bitmap
     * @param context A context to load the bitmap from
     */
    public void init(Context context) {
        gnome = BitmapFactory.decodeResource(context.getResources(), R.drawable.gnome_pensive);
    }

    /**
     * Update the animation in time.
     * @param delta Amount of time since the last update
     */
    public void update(float delta) {
        gnomeCycle += GNOME_SPEED * delta;
    }

    /**
     * Draw the bouncy gnome
     * @param canvas Canvas to draw on
     */
    public void draw(Canvas canvas) {
        /*
         * Compute dimensions
         */
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        float centerX, bottomY, size;

        if(hit > wid) {
            // We are in portrait mode
            centerX = LINES_CENTER_X_PORT * wid;
            size = GNOME_HEIGHT_PORT * hit;
        } else {
            // We are in landscape mode
            centerX = LINES_CENTER_X_LAND * wid;
            size = GNOME_HEIGHT_LAND * hit;
        }

        bottomY = hit;
        float scale = size / gnome.getHeight();

        /*
         * Compute the height and skew for this animation cycle
         */

        // This value goes from 0 to 1 in a cycle
        float cos = -(float)Math.sin(gnomeCycle * 2 * Math.PI * 2)/2 + 0.5f;

        // This value goes from -1 to 1 at half the rate
        float sin = (float)Math.cos(gnomeCycle * Math.PI * 2);

        float heightScale = cos * HEIGHT_MAX + 1;
        float skew = sin * SKEW_MAX;

        canvas.save();
        canvas.translate(centerX, bottomY);
        canvas.skew(skew, 0);
        canvas.scale(1, heightScale);
        canvas.scale(scale,  scale);
        canvas.drawBitmap(gnome, (int)(-gnome.getWidth()/2), -gnome.getHeight(), null);
        canvas.restore();
    }

}
