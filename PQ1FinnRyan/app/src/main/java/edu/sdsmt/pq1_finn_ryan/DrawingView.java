package edu.sdsmt.pq1_finn_ryan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawingView extends View {
    private Bitmap bitmap;
    private Paint fillPaint;
    private boolean addY = false;
    private boolean subY = false;
    private boolean addX = false;
    private boolean subX = false;
    private int width = 200;
    private int height = 250;

    public DrawingView(Context context) {
        super(context);
        init(null, 0);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.grubby);
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);

        if (addY) {
            addY(canvas);
        }

        if (subY) {
            subY(canvas);
        }

        if (addX) {
            addX(canvas);
        }

        if (subX) {
            subX(canvas);
        }

        if (width == 200 && height == 250) {
            canvas.drawRect(50, 50, 150, 200, fillPaint);
        }
    }

    private void addY(Canvas canvas) {
        height -= 20;
        canvas.drawRect(width - 150, height - 200, 150, 200, fillPaint);
        addY = false;
    }

    private void subY(Canvas canvas) {
        height += 20;
        canvas.drawRect(width - 150, height - 200, 150, 200, fillPaint);
        subY = false;
    }

    private void addX(Canvas canvas) {
        width -= 20;
        canvas.drawRect(width - 150, height - 200, 150, 200, fillPaint);
        addX = false;
    }

    private void subX(Canvas canvas) {
        width += 20;
        canvas.drawRect(width - 150, height - 200, 150, 200, fillPaint);
        subX = false;
    }

    public void setAddY(boolean addY) {
        this.addY = addY;
        invalidate();
    }

    public void setSubY(boolean subY) {
        this.subY = subY;
        invalidate();
    }

    public void setAddX(boolean addX) {
        this.addX = addX;
        invalidate();
    }

    public void setSubX(boolean subX) {
        this.subX = subX;
        invalidate();
    }
}