package edu.sdsmt.stopthetribblesfinnryan.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Random;

import edu.sdsmt.stopthetribblesfinnryan.Model.Game;
import edu.sdsmt.stopthetribblesfinnryan.Model.Tribble;

public class GameView extends View {
    private Game game;
    public static final Random random = new Random();
    private Paint fillPaint, outlinePaint;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(Color.LTGRAY);

        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(5.0f);

        game = new Game();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        canvas.drawRect(0, 0, getWidth(), getHeight(), fillPaint);
        canvas.drawRect(0, 0, getWidth(), getHeight(), outlinePaint);

        for(Tribble tribble : game.getTribbles()) {
            tribble.draw(canvas, getWidth(), getHeight(), 0, 0);
            tribble.move(random.nextFloat());
        }

        canvas.restore();
        invalidate();
    }

    public void setColor(int color) {
        game.setColor(color);
    }

    public void setFillPaint(int color) {
        fillPaint.setColor(color);
    }

    public void setOutlinePaint(int color) {
        outlinePaint.setColor(color);
    }

    public int getBackgroundColor() {
        return fillPaint.getColor();
    }

    public boolean isBorderOn() {
        return outlinePaint.getColor() == Color.RED;
    }

    public Game getGame() {
        return game;
    }

    public void saveInstanceState(@NonNull Bundle bundle) {
        game.saveInstanceState(bundle);
    }

    public void restoreInstanceState(@NonNull Bundle bundle) {
        game.restoreInstanceState(bundle);
    }
}
