package edu.sdsmt.stopthetribblesfinnryan.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import edu.sdsmt.stopthetribblesfinnryan.Model.Game;
import edu.sdsmt.stopthetribblesfinnryan.Model.Tribble;
import edu.sdsmt.stopthetribblesfinnryan.R;

public class GameView extends View {
    private Game area;
    private final Touch touch1 = new Touch();
    private final Touch touch2 = new Touch();
    private Paint fillPaint;
    private Paint outlinePaint;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(Color.LTGRAY);

        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(5.0f);
        theme.resolveAttribute(R.attr.primary, typedValue, true);
        outlinePaint.setColor(typedValue.data);

        area = new Game(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        canvas.drawRect(0, 0, getWidth(), getHeight(), fillPaint);
        canvas.drawRect(0, 0, getWidth(), getHeight(), outlinePaint);

        for(Tribble tribble : area.getTribbles())
            tribble.draw(canvas, getWidth(), getHeight(), 0, 0);

        canvas.restore();
    }

    public void newDay() {
        area.newDay();
    }

    public void reset() {
        area.reset();
    }

    public void eat() {
        area.eat();
    }

    public int getTribbleCount() {
        return area.getTribbles().size();
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    private static class Touch {
        public int id = -1;
        public float x = 0;
        public float y = 0;
        public float lastX = 0;
        public float lastY = 0;
        public float dX = 0;
        public float dY = 0;

        public void updatePos(float newX, float newY) {
            lastX = x;
            lastY = y;
            x = newX;
            y = newY;
        }

        public void computeDeltas() {
            dX = x - lastX;
            dY = y - lastY;
        }

        public void clear() {
            id = -1;
            x = 0;
            y = 0;
            lastX = 0;
            lastY = 0;
            dX = 0;
            dY = 0;
        }

        public void move(Touch touch) {
            id = touch.id;
            x = touch.x;
            y = touch.y;
            lastX = touch.lastX;
            lastY = touch.lastY;
            dX = touch.dX;
            dY = touch.dY;
            touch.clear();
        }
    }

    public void saveInstanceState(@NonNull Bundle bundle) {
        area.saveInstanceState(bundle);
    }

    public void restoreInstanceState(@NonNull Bundle bundle) {
        area.restoreInstanceState(bundle);
    }
}
