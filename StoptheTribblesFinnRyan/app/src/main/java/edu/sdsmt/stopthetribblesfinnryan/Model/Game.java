package edu.sdsmt.stopthetribblesfinnryan.Model;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Game {
    private int hungerLevel, dayCount, eatCount, scoreCount, tribbleCount, lastBureau;
    public static final String h = "game.hunger";
    public static final String d = "game.days";
    public static final String e = "game.eat";
    public static final String s = "game.score";
    public static final String l = "game.last";
    public static final String b = "game.bureau";
    private boolean bureau = false;
    private int color;
    private static final Paint paint = new Paint();

    private final ArrayList<Tribble> tribbles = new ArrayList<>();

    public Game() {
        setColor(Color.BLACK);
        reset();
    }

    public void newTurn() {
        dayCount++;
        eatCount = 3;
        if ((dayCount - lastBureau) % 2 == 0)
            bureau = true;

        tribbleCount = (int)Math.ceil(tribbleCount * 1.25);

        int newCount = 3;
        if (tribbleCount > 100)
            newCount = 9;
        else if (tribbleCount > 20)
            newCount = 6;

        tribbles.clear();
        for (int i = 0; i < newCount; i++)
            tribbles.add(new Tribble(i));
    }

    public void reset() {
        dayCount = 1;
        hungerLevel = 0;
        scoreCount = 0;
        tribbleCount = 100;
        eatCount = 3;
        lastBureau = 1;
        bureau = false;

        tribbles.clear();
        for (int i = 0; i < 6; i++)
            tribbles.add(new Tribble(i));
    }

    public void eat() {
        eatCount--;
        if (eatCount < 0)
            eatCount = 0;

        if (hungerLevel == 0)
            tribbleCount *= 2;
        else {
            incHunger(-3);
            tribbleCount += 10;
        }

        int newCount = 3;
        if (tribbleCount > 100)
            newCount = 9;
        else if (tribbleCount > 20)
            newCount = 6;

        tribbles.clear();
        for (int i = 0; i < newCount; i++)
            tribbles.add(new Tribble(newCount + i));
    }

    public void distract() {
        hungerLevel = 10;
        bureau = false;
        lastBureau = dayCount;
    }

    public void collectTribbles(int collect) {
        tribbleCount -= collect;
        incHunger(2);
        scoreCount++;

        int newCount = 3;
        if (tribbleCount > 100)
            newCount = 9;
        else if (tribbleCount > 20)
            newCount = 6;

        tribbles.clear();
        for (int i = 0; i < newCount; i++)
            tribbles.add(new Tribble(i));
    }

    private void incHunger(int inc) {
        hungerLevel += inc;
        if (hungerLevel < 0)
            hungerLevel = 0;
        else if (hungerLevel > 10)
            hungerLevel = 10;
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

    public void saveInstanceState(@NonNull Bundle bundle) {
        bundle.putInt("view.color", color);
        bundle.putInt(h, hungerLevel);
        bundle.putInt(d, dayCount);
        bundle.putInt(e, eatCount);
        bundle.putInt(s, scoreCount);
        bundle.putInt(l, lastBureau);
        bundle.putBoolean(b, bureau);
        bundle.putInt("game.count", tribbleCount);

        bundle.putInt("game.size", tribbles.size());
        for(Tribble tribble : tribbles)
            tribble.saveInstanceState(bundle);
    }

    public void restoreInstanceState(@NonNull Bundle bundle) {
        setColor(bundle.getInt("view.color"));
        dayCount = bundle.getInt(d);
        hungerLevel = bundle.getInt(h);
        scoreCount = bundle.getInt(s);
        eatCount = bundle.getInt(e);
        lastBureau = bundle.getInt(l);
        bureau = bundle.getBoolean(b);
        tribbleCount = bundle.getInt("game.count");

        tribbles.clear();
        for (int i = 0; i < bundle.getInt("game.size"); i++) {
            Tribble tribble = new Tribble(i);
            tribble.restoreInstanceState(bundle);
            tribbles.add(tribble);
        }
    }

    public static Paint getPaint() {
        return paint;
    }

    public int getHunger() {
        return hungerLevel;
    }

    public int getTurns() {
        return dayCount;
    }

    public int getMeals() {
        return eatCount;
    }

    public int getScore() {
        return scoreCount;
    }

    public boolean isBureaucratPresent() {
        return bureau;
    }

    public boolean isLost() {
        return tribbleCount >= 200;
    }

    public boolean isWon() {
        return tribbleCount <= 0;
    }

    public ArrayList<Tribble> getTribbles() {
        return tribbles;
    }

    public int getTribbleCount() {
        return tribbleCount;
    }
}
