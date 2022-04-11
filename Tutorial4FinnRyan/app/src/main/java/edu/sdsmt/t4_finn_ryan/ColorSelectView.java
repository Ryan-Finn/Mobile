package edu.sdsmt.t4_finn_ryan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class ColorSelectView extends View {
    private Bitmap imageBitmap = null;
    private float imageScale = 1;
    private float marginLeft = 0;
    private float marginTop = 0;

    public ColorSelectView(Context context) {
        super(context);
        init();
    }

    public ColorSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorSelectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.colors);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(imageBitmap == null) {
            return;
        }

        float wid = getWidth();
        float hit = getHeight();

        float scaleH = wid / imageBitmap.getWidth();
        float scaleV = hit / imageBitmap.getHeight();

        imageScale = Math.min(scaleH, scaleV);

        float iWid = imageScale * imageBitmap.getWidth();
        float iHit = imageScale * imageBitmap.getHeight();

        marginLeft = (wid - iWid) / 2;
        marginTop = (hit - iHit) / 2;

        canvas.save();
        canvas.translate(marginLeft,  marginTop);
        canvas.scale(imageScale, imageScale);
        canvas.drawBitmap(imageBitmap, 0, 0, null);
        canvas.restore();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            touched(event.getX(0), event.getY(0));
        }
        return super.onTouchEvent(event);
    }

    private void touched(float x, float y) {
        y -= marginTop;
        x -= marginLeft;
        x /= imageScale;
        y /= imageScale;

        if(x >= 0 && x < imageBitmap.getWidth() && y >= 0 && y < imageBitmap.getHeight()) {
            int color = imageBitmap.getPixel((int)x, (int)y);
            ColorSelectActivity activity = (ColorSelectActivity)getContext();
            activity.selectColor(color);
        }
    }
}
