package edu.sdsmt.pq2finnryan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

public class MainView extends View {
    private MainActivity activity;
    private final Touch touch1 = new Touch();
    private final Touch touch2 = new Touch();
    public Paint left;
    public Paint right;
    public Paint top;
    public Paint bottom;

    public MainView(Context context) {
        super(context);
        init();
    }
    public MainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public MainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(MainActivity activity) {
        this.activity = activity;
    }

    private void init() {
        setBackgroundColor(Color.GRAY);

        left = new Paint();
        left.setARGB(155, 255, 0, 0);

        right = new Paint();
        right.setARGB(155, 0, 255, 0);

        top = new Paint();
        top.setARGB(155, 0, 0, 255);

        bottom = new Paint();
        bottom.setARGB(155, 255, 255, 0);
    }

    @Override
    public void onDraw(Canvas c) {
        int width = getWidth();
        int height = getHeight();

        c.drawRect(0, 0, width / 4.0f, height, left);
        c.drawRect(3.0f * width / 4, 0, width, height, right);
        c.drawRect(0, 0, width, height / 4.0f, top);
        c.drawRect(0, 3.0f * height / 4, width, height, bottom);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        int id = event.getPointerId(event.getActionIndex());

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touch1.id = id;
                touch2.id = -1;
                getPositions(event);
                inc();
                return true;

            case MotionEvent.ACTION_POINTER_DOWN:
                if (touch1.id >= 0 && touch2.id < 0) {
                    touch2.id = id;
                    getPositions(event);
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:
                if (id == touch2.id)
                    touch2.clear();
                else if (id == touch1.id)
                    touch2.move(touch1);
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                getPositions(event);
                //move();
                return true;
        }
        return false;
    }

    private void getPositions(MotionEvent event) {
        for (int i = 0; i < event.getPointerCount(); i++)
            if (event.getPointerId(i) == touch1.id)
                touch1.updatePos(event.getX(i), event.getY(i));
            else if (event.getPointerId(i) == touch2.id)
                touch2.updatePos(event.getX(i), event.getY(i));
        //invalidate();
    }

    private void inc() {
        if (touch1.id < 0)
            return;

        touch1.computeDeltas();

        int width = getWidth();
        int height = getHeight();

        if (touch1.x <= width / 4.0f)
            activity.incrementLeft();
        else if (touch1.x > 3.0f * width / 4)
            activity.incrementRight();

        if (touch1.y <= height / 4.0f)
            activity.incrementTop();
        else if (touch1.y > 3.0f * height / 4)
            activity.incrementBottom();
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
}
