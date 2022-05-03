package edu.sdsmt.project3finnryan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MainView extends View {
    private final static int factor = 1000000000;
    private float startLat, startLong, latitude, longitude = 0;
    private Paint fillPaint, outlinePaint, penPaint;
    private Bitmap canvasBitmap = null;
    private boolean newLoc = true;
    private float x, y, scale, prevScale = -1;

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

    private void init() {
        scale = 1;

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(Color.WHITE);

        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(5.0f);
        outlinePaint.setColor(Color.BLACK);

        penPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        penPaint.setStrokeWidth(10.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (canvasBitmap == null)
            canvasBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

        float prevX = x;
        float prevY = y;
        if (x == -1) {
            prevX = factor * (startLat - latitude) / getWidth() + getWidth() / 2f;
            prevY = factor * (startLong - longitude) / getWidth() + getHeight() / 2f;
        }

        x = factor * (startLat - latitude) / getWidth() + getWidth() / 2f;
        y = factor * (startLong - longitude) / getWidth() + getHeight() / 2f;

        canvas.drawRect(0, 0, getWidth(), getHeight(), fillPaint);
        canvas.drawRect(0, 0, getWidth(), getHeight(), outlinePaint);

        if (newLoc && scale >= 0.45f)
            makeBitmap(prevX, prevY);
        canvas.drawBitmap(canvasBitmap, 0, 0, null);

        canvas.save();
        canvas.translate(x, y);
        canvas.drawCircle(0, 0, scale * 50f, penPaint);
        canvas.restore();

        newLoc = false;
    }

    private void makeBitmap(float prevX, float prevY) {
        int sz = 16;
        float X, Y, R;
        int w = canvasBitmap.getWidth();
        int h = canvasBitmap.getHeight();
        int[] mapDest = new int[w * h];

        canvasBitmap.getPixels(mapDest, 0, w, 0, 0, w, h);

        for (int k = 0; k <= sz; k++) {
            X = ((sz - k) * prevX + k * x) / sz;
            Y = ((sz - k) * prevY + k * y) / sz;
            R = 50f * ((sz - k) * prevScale + k * scale) / sz;

            for (int i = 0; i < w; i++)
                for (int j = 0; j < h; j++)
                    if ((i - X) * (i - X) + (j - Y) * (j - Y) <= R * R)
                        mapDest[j * w + i] = Color.BLACK;
        }

        canvasBitmap = Bitmap.createBitmap(mapDest, w, h, Bitmap.Config.ARGB_8888);
    }

    public void setScale(float scale) {
        penPaint.setStyle(Paint.Style.STROKE);
        penPaint.setColor(Color.GRAY);
        this.scale = scale;

        if (scale < 0.25f)
            this.scale = 0.25f;
        else if (scale >= 0.95f)
            this.scale = 1;

        if (scale >= 0.45f) {
            penPaint.setStyle(Paint.Style.FILL);
            penPaint.setColor(Color.BLACK);
        }

        invalidate();
    }

    public void setStartLocation(float latitude, float longitude) {
        if (startLat != 0 && startLong != 0)
            return;

        startLat = latitude;
        startLong = longitude;
        this.latitude = latitude;
        this.longitude = longitude;
        prevScale = 0;
    }

    public void setLocation(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

        newLoc = !newLoc;
        invalidate();
        prevScale = scale;
    }
}
