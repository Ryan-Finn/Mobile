package edu.sdsmt.finn_ryan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Random;

public class Puzzle {
    private static final Random random = new Random();
    private final View view;
    final static float SCALE_IN_VIEW = 0.9f;
    private final Paint fillPaint;
    private final Paint outlinePaint;
    private final Bitmap puzzleComplete;
    public final ArrayList<PuzzlePiece> pieces = new ArrayList<>();
    private int puzzleSize;
    private float scaleFactor;
    private int marginX;
    private int marginY;
    private PuzzlePiece dragging = null;
    private float lastRelX;
    private float lastRelY;
    private final static String LOCATIONS = "Puzzle.locations";
    private final static String IDS = "Puzzle.ids";

    public Puzzle(View v, Context context) {
        view = v;
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(0xffcccccc);

        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setColor(Color.BLUE);
        outlinePaint.setStrokeWidth(5.0f);

        puzzleComplete = BitmapFactory.decodeResource(context.getResources(), R.drawable.grubby);
        pieces.add(new PuzzlePiece(context, R.drawable.grubby1, 0.264f, 0.175f));
        pieces.add(new PuzzlePiece(context, R.drawable.grubby2, 0.701f, 0.239f));
        pieces.add(new PuzzlePiece(context, R.drawable.grubby3, 0.751f, 0.522f));
        pieces.add(new PuzzlePiece(context, R.drawable.grubby4, 0.335f, 0.477f));
        pieces.add(new PuzzlePiece(context, R.drawable.grubby5, 0.662f, 0.792f));
        pieces.add(new PuzzlePiece(context, R.drawable.grubby6, 0.254f, 0.818f));

        shuffle();
    }

    public void draw(Canvas canvas) {
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();
        int minDim = Math.min(wid, hit);
        puzzleSize = (int)(minDim * SCALE_IN_VIEW);

        marginX = (wid - puzzleSize) / 2;
        marginY = (hit - puzzleSize) / 2;

        canvas.drawRect(marginX, marginY, marginX + puzzleSize, marginY + puzzleSize, fillPaint);
        canvas.drawRect(marginX, marginY, marginX + puzzleSize, marginY + puzzleSize, outlinePaint);

        scaleFactor = (float)puzzleSize / (float)puzzleComplete.getWidth();

        for(PuzzlePiece piece : pieces) {
            piece.draw(canvas, marginX, marginY, puzzleSize, scaleFactor);
        }
    }

    private boolean onTouched(float x, float y) {
        for (int p = pieces.size()-1; p >= 0; p--) {
            if(pieces.get(p).hit(x, y, puzzleSize, scaleFactor)) {
                dragging = pieces.get(p);
                lastRelX = x;
                lastRelY = y;
                pieces.remove(p);
                pieces.add(dragging);
                view.invalidate();
                return true;
            }
        }

        return false;
    }

    private boolean onReleased() {
        if(dragging != null) {
            if (dragging.maybeSnap()) {
                pieces.remove(dragging);
                pieces.add(0, dragging);

                view.invalidate();

                if(isDone()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    ShuffleListener listener = new ShuffleListener();

                    builder.setTitle(R.string.hurrah);
                    builder.setMessage(R.string.completed_puzzle);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setNegativeButton(R.string.shuffle, listener);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
            dragging = null;
            return true;
        }

        return false;
    }

    private class ShuffleListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            shuffle();
            view.invalidate();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        float relX = (event.getX() - marginX) / puzzleSize;
        float relY = (event.getY() - marginY) / puzzleSize;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return onTouched(relX, relY);

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return onReleased();

            case MotionEvent.ACTION_MOVE:
                if(dragging != null) {
                    dragging.move(relX - lastRelX, relY - lastRelY);
                    view.invalidate();
                    lastRelX = relX;
                    lastRelY = relY;
                    return true;
                }
                break;
        }

        return false;
    }

    public boolean isDone() {
        for(PuzzlePiece piece : pieces) {
            if(!piece.isSnapped()) {
                return false;
            }
        }

        return true;
    }

    public void shuffle() {
        for(PuzzlePiece piece : pieces) {
            piece.shuffle(random);
        }
    }

    public void saveInstanceState(Bundle bundle) {
        float [] locations = new float[pieces.size() * 2];
        int [] ids = new int[pieces.size()];

        for (int i = 0;  i < pieces.size(); i++) {
            PuzzlePiece piece = pieces.get(i);
            locations[i*2] = piece.getX();
            locations[i*2+1] = piece.getY();
            ids[i] = piece.getId();
        }

        bundle.putFloatArray(LOCATIONS, locations);
        bundle.putIntArray(IDS,  ids);
    }

    public void loadInstanceState(Bundle bundle) {
        float [] locations = bundle.getFloatArray(LOCATIONS);
        int [] ids = bundle.getIntArray(IDS);

        for (int i = 0;  i < ids.length - 1; i++) {
            for(int j = i+1;  j < pieces.size(); j++) {
                if(ids[i] == pieces.get(j).getId()) {
                    PuzzlePiece t = pieces.get(i);
                    pieces.set(i, pieces.get(j));
                    pieces.set(j, t);
                }
            }
        }

        for(int i=0;  i<pieces.size(); i++) {
            PuzzlePiece piece = pieces.get(i);
            piece.setX(locations[i*2]);
            piece.setY(locations[i*2+1]);
        }
    }
}
