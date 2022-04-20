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
    private final int id;
    private final int width;
    private final int height;
    private static final float scale = 0.2f;
    private float x, y, relX, relY;

    public Tribble(Context context, int id, Random rand) {
        this.relX = rand.nextFloat();
        this.relY = rand.nextFloat();
        this.id = id;
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.tribble);
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    public void draw(Canvas canvas, float canvas_width, float canvas_height, float canvasX, float canvasY) {
        x = canvasX + width * scale;
        y = canvasY + height * scale;

        x += relX * (canvas_width - 2 * width * scale);
        y += relY * (canvas_height - 2 * height * scale);

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

    public void wiggle(float rand) {
        double ang = 2 * rand * Math.PI;
        float moveX = 0.001f * (float)Math.cos(ang);
        float moveY = 0.001f * (float)Math.sin(ang);

        if (relX + moveX < 0 || relX + moveX > 1)
            relX -= moveX;
        else
            relX += moveX;

        if (relY + moveY < 0 || relY + moveY > 1)
            relY -= moveY;
        else
            relY += moveY;
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
}
