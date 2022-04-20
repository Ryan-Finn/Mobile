package edu.sdsmt.stopthetribblesfinnryan.View;

import android.content.Context;
import android.content.res.Resources;
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
    private static final Random random = new Random();
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

        area = new Game(context, random);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        canvas.drawRect(0, 0, getWidth(), getHeight(), fillPaint);
        canvas.drawRect(0, 0, getWidth(), getHeight(), outlinePaint);

        for(Tribble tribble : area.getTribbles()) {
            tribble.draw(canvas, getWidth(), getHeight(), 0, 0);
            tribble.wiggle(random.nextFloat());
        }

        canvas.restore();
        invalidate();
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

    public void saveInstanceState(@NonNull Bundle bundle) {
        area.saveInstanceState(bundle);
    }

    public void restoreInstanceState(@NonNull Bundle bundle) {
        area.restoreInstanceState(bundle);
    }
}
