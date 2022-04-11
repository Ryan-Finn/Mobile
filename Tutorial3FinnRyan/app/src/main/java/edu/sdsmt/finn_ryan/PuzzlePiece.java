package edu.sdsmt.finn_ryan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.Random;

public class PuzzlePiece {
    private final Bitmap piece;
    private float x = 0.5f;
    private float y = 0.25f;
    private final float finalX;
    private final float finalY;
    final static float SNAP_DISTANCE = 0.05f;
    private final int id;

    public PuzzlePiece(Context context, int id, float finalX, float finalY) {
        this.finalX = finalX;
        this.finalY = finalY;
        this.id = id;

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

    public boolean hit(float testX, float testY, int puzzleSize, float scaleFactor) {
        // Make relative to the location and size to the piece size
        int pX = (int)((testX - x) * puzzleSize / scaleFactor) + piece.getWidth() / 2;
        int pY = (int)((testY - y) * puzzleSize / scaleFactor) + piece.getHeight() / 2;

        if(pX < 0 || pX >= piece.getWidth() || pY < 0 || pY >= piece.getHeight()) {
            return false;
        }

        // We are within the rectangle of the piece.
        // Are we touching actual picture?
        return (piece.getPixel(pX, pY) & 0xff000000) != 0;
    }

    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }

    public boolean maybeSnap() {
        if (Math.abs(x - finalX) < SNAP_DISTANCE && Math.abs(y - finalY) < SNAP_DISTANCE) {
            x = finalX;
            y = finalY;
            return true;
        }

        return false;
    }

    public boolean isSnapped() {
        return x == finalX && y == finalY;
    }

    public void shuffle(Random rand) {
        x = rand.nextFloat();
        y = rand.nextFloat();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }
}
