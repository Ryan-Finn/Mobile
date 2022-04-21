package edu.sdsmt.stopthetribblesfinnryan.Model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.Random;

import edu.sdsmt.stopthetribblesfinnryan.View.GameView;

public class Tribble {
    private final int width, height, id;
    private float x, y, relX, relY, ang;
    private static final float scale = 0.5f;
    private boolean toggle = false;

    public Tribble(int id) {
        this.width = GameView.getBitmap().getWidth();
        this.height = GameView.getBitmap().getHeight();
        this.id = id;
        Random rand = GameView.random;
        this.relX = rand.nextFloat();
        this.relY = rand.nextFloat();
        this.ang = 2 * rand.nextFloat() * (float)Math.PI;
    }

    public void draw(Canvas canvas, float canvas_width, float canvas_height, float canvasX, float canvasY) {
        x = canvasX + width * scale / 2;
        y = canvasY + height * scale / 2;

        x += relX * (canvas_width - width * scale);
        y += relY * (canvas_height - height * scale);

        float window_aspect = canvas.getWidth() / (float) canvas.getHeight();
        float canvas_aspect = canvas_width / canvas_height;
        float second_scale = scale * canvas_aspect / window_aspect;

        canvas.save();

        canvas.translate(x, y);
        canvas.scale(second_scale, second_scale);
        canvas.rotate(ang * 180 / (float)Math.PI);
        canvas.translate(-width / 2f, -height / 2f);
        canvas.drawBitmap(GameView.getBitmap(), 0, 0, null);

        canvas.restore();
    }

    public void move(float rand) {
        if (rand < 0.02)
            toggle = !toggle;

        if (toggle)
            ang += 0.005 * Math.PI;
        else
            ang -= 0.005 * Math.PI;

        float moveX = 0.002f * (float)Math.cos(ang);
        float moveY = 0.002f * (float)Math.sin(ang);

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
        bundle.putFloat("tribble." + id + ".ang", ang);
    }

    public void restoreInstanceState(@NonNull Bundle bundle) {
        x = bundle.getFloat("tribble." + id + ".x");
        y = bundle.getFloat("tribble." + id + ".y");
        relX = bundle.getFloat("tribble." + id + ".relX");
        relY = bundle.getFloat("tribble." + id + ".relY");
        ang = bundle.getFloat("tribble." + id + ".ang");
    }
}
