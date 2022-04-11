package edu.sdsmt.finn_ryan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class PuzzlePiece {
    private final Bitmap piece;
    private final float x = 0.5f;
    private final float y = 0.25f;
    private final float finalX;
    private final float finalY;

    public PuzzlePiece(Context context, int id, float finalX, float finalY) {
        this.finalX = finalX;
        this.finalY = finalY;

        piece = BitmapFactory.decodeResource(context.getResources(), id);
    }

    public void draw(Canvas canvas, int marginX, int marginY, int puzzleSize, float scaleFactor) {
        canvas.save();

        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(marginX + x * puzzleSize, marginY + y * puzzleSize);

        // Scale it to the right size
        canvas.scale(scaleFactor, scaleFactor);

        // This magic code makes the center of the piece at 0, 0
        canvas.translate(-piece.getWidth() / 2f, -piece.getHeight() / 2f);

        // Draw the bitmap
        canvas.drawBitmap(piece, 0, 0, null);
        canvas.restore();
    }
}
