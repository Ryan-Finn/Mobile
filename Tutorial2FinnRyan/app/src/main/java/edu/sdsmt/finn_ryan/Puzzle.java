package edu.sdsmt.finn_ryan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class Puzzle {
    final static float SCALE_IN_VIEW = 0.9f;
    private final Paint fillPaint;
    private final Paint outlinePaint;
    private final Bitmap puzzleComplete;
    public final ArrayList<PuzzlePiece> pieces = new ArrayList<>();

    public Puzzle(Context context) {
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(0xffcccccc);

        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setColor(Color.BLUE);
        outlinePaint.setStrokeWidth(5.0f);

        puzzleComplete = BitmapFactory.decodeResource(context.getResources(), R.drawable.grubby);
        pieces.add(new PuzzlePiece(context, R.drawable.grubby1, 0.264f, 0.175f));
    }

    public void draw(Canvas canvas) {
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();
        int minDim = Math.min(wid, hit);
        int puzzleSize = (int)(minDim * SCALE_IN_VIEW);

        int marginX = (wid - puzzleSize) / 2;
        int marginY = (hit - puzzleSize) / 2;

        canvas.drawRect(marginX, marginY, marginX + puzzleSize, marginY + puzzleSize, fillPaint);
        canvas.drawRect(marginX, marginY, marginX + puzzleSize, marginY + puzzleSize, outlinePaint);

        float scaleFactor = (float)puzzleSize / (float)puzzleComplete.getWidth();

        for(PuzzlePiece piece : pieces) {
            piece.draw(canvas, marginX, marginY, puzzleSize, scaleFactor);
        }

        /* *
        canvas.save();
        canvas.translate(marginX, marginY);
        canvas.scale(scaleFactor, scaleFactor);
        canvas.drawBitmap(puzzleComplete, 0, 0, null);
        canvas.restore();
        /**/
    }
}
