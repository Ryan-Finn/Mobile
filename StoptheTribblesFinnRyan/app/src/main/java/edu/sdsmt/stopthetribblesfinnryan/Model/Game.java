package edu.sdsmt.stopthetribblesfinnryan.Model;

import android.content.Context;

import java.util.ArrayList;

public class Game {
    private final ArrayList<Tribble> tribbles = new ArrayList<>();
    private final Context context;

    public Game(Context context) {
        this.context = context;
        reset();
    }

    public void newDay() {
        int newCount = (int)Math.ceil(tribbles.size() * 0.25);
        for (int i = 0; i < newCount; i++)
            tribbles.add(new Tribble(context, tribbles.size() + i, 0.1f));
    }

    public void reset() {
        tribbles.clear();
        for (int i = 0; i < 100; i++)
            tribbles.add(new Tribble(context, i, 0.1f));
    }

    public void eat() {
        for (int i = 0; i < 10; i++)
            tribbles.add(new Tribble(context, tribbles.size() + i, 0.1f));
    }

    public ArrayList<Tribble> getTribbles() {
        return tribbles;
    }
}
