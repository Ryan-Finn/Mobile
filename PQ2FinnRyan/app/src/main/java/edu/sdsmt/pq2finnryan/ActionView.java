package edu.sdsmt.pq2finnryan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class ActionView extends View {
    private Paint basic;
    private Paint area;
    private float aSize;
    private float bSize;
    private float cSize;
    private float resetSize;
    private float loadSize;
    private MainActivity activity;
    private float fontHeight = 30;
    private final Touch touch1 = new Touch();
    private final Touch touch2 = new Touch();

    public ActionView(Context context) {
        super(context);
        init(context);
    }
    public ActionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public ActionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Helper function to keep a reference to teh activity and setup the paint objects and locations
     * @param c the calling activity
     */
    private void init(Context c) {
        activity = (MainActivity) c;
        basic = new Paint();
        area = new Paint();

        //calculate text sizes
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        fontHeight = fontHeight * (metrics.densityDpi / 160f);
        basic.setTextSize(fontHeight);
        aSize = basic.measureText(c.getString(R.string.addA)) / 2;
        bSize = basic.measureText(c.getString(R.string.addBC)) / 2;
        cSize = basic.measureText(c.getString(R.string.subAC)) / 2;
        resetSize = basic.measureText(c.getString(R.string.reset)) / 2;
        loadSize = basic.measureText(c.getString(R.string.load)) / 2;
    }

    @Override
    public void onDraw(Canvas c) {
        float rowHeight = getHeight() * 0.33f;
        float colWidth = getWidth() * 0.33f;

        //draw "buttons"
        area.setColor(Color.rgb(200, 200, 200));
        c.drawRect(0, 0, colWidth, rowHeight, area);
        area.setColor(Color.rgb(220, 220, 220));
        c.drawRect(colWidth, 0, colWidth * 2, rowHeight, area);
        area.setColor(Color.rgb(200, 200, 200));
        c.drawRect(colWidth * 2, 0, getWidth(), rowHeight, area);
        area.setColor(Color.rgb(230, 230, 230));
        c.drawRect(0, rowHeight, getWidth(), rowHeight, area);
        area.setColor(Color.rgb(180, 180, 180));
        c.drawRect(0, rowHeight * 2, getWidth(), getHeight(), area);

        //draw button text
        c.drawText(activity.getString(R.string.addA), colWidth * 0.5f - aSize, rowHeight * 0.5f + fontHeight * 0.5f, basic);
        c.drawText(activity.getString(R.string.addBC), colWidth * 1.5f - bSize, rowHeight * 0.5f + fontHeight * 0.5f, basic);
        c.drawText(activity.getString(R.string.subAC), colWidth * 2.5f - cSize, rowHeight * 0.5f + fontHeight * 0.5f, basic);
        c.drawText(activity.getString(R.string.reset), getWidth() * 0.5f - resetSize, rowHeight * 1.5f + fontHeight * 0.5f, basic);
        c.drawText(activity.getString(R.string.load), getWidth() * 0.5f - loadSize, rowHeight * 2.5f + fontHeight * 0.5f, basic);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int id = event.getPointerId(event.getActionIndex());

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touch1.id = id;
                touch2.id = -1;
                getPositions(event);
                activity.incrementTotal();
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:
                buttonPress();
                if (id == touch2.id)
                    touch2.clear();
                else if (id == touch1.id)
                    touch2.move(touch1);
                return true;

            case MotionEvent.ACTION_MOVE:
                getPositions(event);
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
    }

    private void buttonPress() {
        float rowHeight = getHeight() * 0.33f;
        float colWidth = getWidth() * 0.33f;

        if (touch1.y <= rowHeight)
            if (touch1.x <= colWidth)
                activity.onAddA();
            else if (touch1.x <= 2 * colWidth)
                activity.onAddBC();
            else
                activity.onSubAC();
        else if (touch1.y <= 2 * rowHeight)
            activity.onReset();
        else
            activity.onLoadFullDatabase();
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
