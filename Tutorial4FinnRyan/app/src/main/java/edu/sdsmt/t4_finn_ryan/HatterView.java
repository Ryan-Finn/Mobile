package edu.sdsmt.t4_finn_ryan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class HatterView extends View {
    private final Parameters params = new Parameters();
    private Bitmap imageBitmap = null;
    private float imageScale = 1;
    private float marginLeft = 0;
    private float marginTop = 0;
    public static final int HAT_BLACK = 0;
    public static final int HAT_GRAY = 1;
    public static final int HAT_CUSTOM = 2;
    private Bitmap hatBitmap = null;
    private Bitmap featherBitmap = null;
    private Bitmap hatbandBitmap = null;
    private final Touch touch1 = new Touch();
    private final Touch touch2 = new Touch();
    private Paint customPaint;

    public HatterView(Context context) {
        super(context);
        init();
    }

    public HatterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HatterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setHat(HAT_BLACK);
        customPaint = new Paint();
        customPaint.setColorFilter(new LightingColorFilter(params.color, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (imageBitmap == null) { return; }

        float wid = getWidth();
        float hit = getHeight();

        float scaleH = wid / imageBitmap.getWidth();
        float scaleV = hit / imageBitmap.getHeight();
        //float scaleH = 1;
        //float scaleV = 1;

        imageScale =  Math.min(scaleH, scaleV);

        float iWid = imageScale * imageBitmap.getWidth();
        float iHit = imageScale * imageBitmap.getHeight();
        //float iWid = imageScale * wid;
        //float iHit = imageScale * hit;

        marginLeft = (wid - iWid) / 2;
        marginTop = (hit - iHit) / 2;

        canvas.save();
        canvas.translate(marginLeft, marginTop);
        canvas.scale(imageScale, imageScale);
        canvas.drawBitmap(imageBitmap, 0, 0, null);

        canvas.translate(params.hatX,  params.hatY);
        canvas.scale(params.hatScale, params.hatScale);
        canvas.rotate(params.hatAngle);

        if(params.hat == HAT_CUSTOM) {
            canvas.drawBitmap(hatBitmap, 0, 0, customPaint);
        } else {
            canvas.drawBitmap(hatBitmap, 0, 0, null);
        }

        if (params.feather) {
            float factor = hatBitmap.getWidth() / 500.0f;
            canvas.drawBitmap(featherBitmap, 322 * factor, 22 * factor, null);
        }

        if(hatbandBitmap != null) {
            canvas.drawBitmap(hatbandBitmap, 0, 0, null);
        }
        canvas.restore();
    }

    private static class Parameters implements Serializable {
        public String imageUri = null;
        public int hat = HAT_BLACK;
        public float hatX = 0;
        public float hatY = 0;
        public float hatScale = 1;
        public float hatAngle = 0;
        public int color = Color.WHITE;
        public boolean feather = false;
    }

    public void setImageUri(String imageUri) {
        params.imageUri = imageUri;

        InputStream input;
        try {
            input = getContext().getContentResolver().openInputStream( Uri.parse(imageUri) );
            imageBitmap = BitmapFactory.decodeStream(input);
            input.close();
            invalidate();
        } catch (IOException e) {
            Toast.makeText(getContext(), getContext().getString(R.string.no_load), Toast.LENGTH_SHORT).show();
        }
    }

    public int getHat() { return params.hat; }
    public void setHat(int hat) {
        params.hat = hat;
        hatBitmap = null;
        featherBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.feather);
        hatbandBitmap = null;

        switch(hat) {
            case HAT_BLACK:
                hatBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hat_black);
                break;

            case HAT_GRAY:
                hatBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hat_gray);
                break;

            case HAT_CUSTOM:
                hatBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hat_white);
                hatbandBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hat_white_band);
                break;
        }

        invalidate();
    }

    public int getColor() {
        return params.color;
    }
    public void setColor(int color) {
        params.color = color;
        customPaint.setColorFilter(new LightingColorFilter(color, 0));
        invalidate();
    }

    public boolean getFeather() { return params.feather; }
    public void setFeather(boolean bool) {
        params.feather = bool;
        invalidate();
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        int id = event.getPointerId(event.getActionIndex());

        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touch1.id = id;
                touch2.id = -1;
                getPositions(event);
                return true;

            case MotionEvent.ACTION_POINTER_DOWN:
                if (touch1.id >= 0 && touch2.id < 0) {
                    touch2.id = id;
                    getPositions(event);
                    //touch2.copyToLast();
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touch1.clear();
                touch2.clear();
                invalidate();
                return true;

            case MotionEvent.ACTION_POINTER_UP:
                if (id == touch2.id) {
                    touch2.clear();
                } else if (id == touch1.id) {
                    touch2.move(touch1);
                }
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                getPositions(event);
                move();
                return true;
        }

        return super.onTouchEvent(event);
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

    private void getPositions(MotionEvent event) {
        for (int i = 0; i < event.getPointerCount(); i++) {
            int id = event.getPointerId(i);

            float x = (event.getX(i) - marginLeft) / imageScale;
            float y = (event.getY(i) - marginTop) / imageScale;

            if (id == touch1.id) {
                touch1.updatePos(x, y);
            } else if (id == touch2.id) {
                touch2.updatePos(x, y);
            }
        }

        invalidate();
    }

    private void move() {
        if (touch1.id < 0) { return; }

        touch1.computeDeltas();
        params.hatX += touch1.dX;
        params.hatY += touch1.dY;

        if (touch2.id >= 0) {
            float angle1 = angle(touch1.lastX, touch1.lastY, touch2.lastX, touch2.lastY);
            float angle2 = angle(touch1.x, touch1.y, touch2.x, touch2.y);
            float da = angle2 - angle1;
            rotate(da, touch1.x, touch1.y);

            float delLastX = touch2.lastX - touch1.lastX;
            float delLastY = touch2.lastY - touch1.lastY;
            float disLast = delLastX*delLastX + delLastY*delLastY;

            float delX = touch2.x - touch1.x;
            float delY = touch2.y - touch1.y;
            float dis = delX*delX + delY*delY;

            float ds = (float)Math.sqrt(dis / disLast);
            scale(ds, touch1.x, touch1.y);
        }
    }

    public void rotate(float dAngle, float x1, float y1) {
        params.hatAngle += (float)Math.toDegrees(dAngle);

        float ca = (float)Math.cos(dAngle);
        float sa = (float)Math.sin(dAngle);
        float xp = (params.hatX - x1) * ca - (params.hatY - y1) * sa + x1;
        float yp = (params.hatX - x1) * sa + (params.hatY - y1) * ca + y1;

        params.hatX = xp;
        params.hatY = yp;
    }

    private float angle(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float)Math.atan2(dy, dx);
    }

    public void scale(float dScale, float x1, float y1) {
        params.hatScale *= dScale;

        float xp = x1 - (x1 - params.hatX) * dScale;
        float yp = y1 - (y1 - params.hatY) * dScale;

        params.hatX = xp;
        params.hatY = yp;
    }

    public void putToBundle(String key, Bundle bundle) {
        bundle.putSerializable(key, params);
    }
    public void getFromBundle(String key, Bundle bundle) {
        Parameters p = (Parameters)bundle.getSerializable(key);

        params.hatX = p.hatX;
        params.hatY = p.hatY;
        params.hatScale = p.hatScale;
        params.hatAngle = p.hatAngle;

        params.feather = p.feather;

        params.color = p.color;
        customPaint.setColorFilter(new LightingColorFilter(p.color, 0));

        setImageUri(p.imageUri);

        setHat(p.hat);
    }
}