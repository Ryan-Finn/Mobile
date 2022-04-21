package edu.sdsmt.stopthetribblesfinnryan.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Random;

import edu.sdsmt.stopthetribblesfinnryan.Model.Game;
import edu.sdsmt.stopthetribblesfinnryan.Model.Tribble;
import edu.sdsmt.stopthetribblesfinnryan.R;

public class GameView extends View {
    private Game area;
    public static final Random random = new Random();
    private static Bitmap bitmap;
    private Bitmap masterBitmap;
    private Paint fillPaint;
    private Paint outlinePaint;
    private int filter = Color.BLACK;

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

        masterBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.tribble);
        filterBitmap(filter);
        area = new Game();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        canvas.drawRect(0, 0, getWidth(), getHeight(), fillPaint);
        canvas.drawRect(0, 0, getWidth(), getHeight(), outlinePaint);

        for(Tribble tribble : area.getTribbles()) {
            tribble.draw(canvas, getWidth(), getHeight(), 0, 0);
            tribble.move(random.nextFloat());
        }

        canvas.restore();
        invalidate();
    }

    public void filterBitmap(int color) {
        filter = color;
        int w = masterBitmap.getWidth();
        int h = masterBitmap.getHeight();
        int[] mapSrcColor = new int[w * h];
        int[] mapDestColor = new int[w * h];
        float[] filterHSV = new float[3];
        float[] pixelHSV = new float[3];

        masterBitmap.getPixels(mapSrcColor, 0, w, 0, 0, w, h);

        Color.colorToHSV(color, filterHSV);

        int index = 0;
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++) {
                if ((mapSrcColor[index] & 0xff000000) != 0) {
                    Color.colorToHSV(mapSrcColor[index], pixelHSV);

                    pixelHSV[0] = filterHSV[0];
                    if (color == Color.BLACK) {
                        pixelHSV[1] = 0;
                        pixelHSV[2] -= 0.67f;
                        if (pixelHSV[2] < 0)
                            pixelHSV[2] = 0;
                    }

                    mapDestColor[index] = Color.HSVToColor(pixelHSV);
                }
                index++;
            }

        bitmap = Bitmap.createBitmap(mapDestColor, w, h, Bitmap.Config.ARGB_8888);
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
        return area.getTribbleCount();
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public void saveInstanceState(@NonNull Bundle bundle) {
        bundle.putInt("view.filter", filter);
        area.saveInstanceState(bundle);
    }

    public void restoreInstanceState(@NonNull Bundle bundle) {
        filterBitmap(bundle.getInt("view.filter"));
        area.restoreInstanceState(bundle);
    }
}
