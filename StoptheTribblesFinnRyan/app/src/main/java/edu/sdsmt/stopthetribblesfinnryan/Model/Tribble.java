package edu.sdsmt.stopthetribblesfinnryan.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.Random;

import edu.sdsmt.stopthetribblesfinnryan.R;

public class Tribble {
    private final Bitmap bitmap;
    private float relX = -1;
    private float relY = -1;
    private float x = -1;
    private float y = -1;
    private final int id;
    private final int width;
    private final int height;
    private final float scale;

    public Tribble(Context context, int id, float scale) {
        this.id = id;
        this.scale = scale;
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tribble);
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    public void draw(Canvas canvas, float canvas_width, float canvas_height, float canvasX, float canvasY) {
        x = canvasX + relX * canvas_width;
        y = canvasY + relY * canvas_height;
        float window_aspect = canvas.getWidth() / (float) canvas.getHeight();
        float canvas_aspect = canvas_width / canvas_height;
        float second_scale = scale * canvas_aspect / window_aspect;

        canvas.save();

        canvas.translate(x, y);
        canvas.scale(second_scale, second_scale);
        canvas.translate(-width / 2f, -height / 2f);
        canvas.drawBitmap(bitmap, 0, 0, null);

        canvas.restore();
    }

    public void shuffle(float canvas_width, float canvas_height, float canvasX, float canvasY, Random rand) {
        relX = rand.nextFloat();
        relY = rand.nextFloat();

        x = canvasX + relX * canvas_width;
        y = canvasY + relY * canvas_height;

        double minX = canvasX + width * scale / 2.0;
        double maxX = canvasX + canvas_width - width * scale / 2.0;
        double minY = canvasY + height * scale / 2.0;
        double maxY = canvasY + canvas_height - height * scale / 2.0;

        while (maxX <= x || x <= minX) {
            relX = rand.nextFloat();
            x = canvasX + relX * canvas_width;
        }
        while (maxY <= y || y <= minY) {
            relY = rand.nextFloat();
            y = canvasY + relY * canvas_height;
        }
    }

    public void saveInstanceState(@NonNull Bundle bundle) {
        bundle.putFloat("tribble." + id + ".x", x);
        bundle.putFloat("tribble." + id + ".y", y);
        bundle.putFloat("tribble." + id + ".relX", relX);
        bundle.putFloat("tribble." + id + ".relY", relY);
    }

    public void restoreInstanceState(@NonNull Bundle bundle) {
        x = bundle.getFloat("tribble." + id + ".x");
        y = bundle.getFloat("tribble." + id + ".y");
        relX = bundle.getFloat("tribble." + id + ".relX");
        relY = bundle.getFloat("tribble." + id + ".relY");
    }

    public float getRelX() {
        return relX;
    }

    public float getRelY() {
        return relY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public float getRadius() {return (float) this.width * scale / 2;}

    public void setRelX(float x) {
        this.relX = x;
    }

    public void setRelY(float y) {
        this.relY = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
