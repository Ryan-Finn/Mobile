package edu.sdsmt.stopthetribblesfinnryan.Model;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private final ArrayList<Tribble> tribbles = new ArrayList<>();
    private static final Random random = new Random();
    private final Context context;

    public Game(Context context) {
        this.context = context;
        reset();
    }

    public void newDay() {
        int newCount = (int)Math.ceil(tribbles.size() * 0.25);
        for (int i = 0; i < newCount; i++)
            tribbles.add(new Tribble(context, tribbles.size() + i, random));
    }

    public void reset() {
        tribbles.clear();
        for (int i = 0; i < 100; i++)
            tribbles.add(new Tribble(context, i, random));
    }

    public void eat() {
        for (int i = 0; i < 10; i++)
            tribbles.add(new Tribble(context, tribbles.size() + i, random));
    }

    public void saveInstanceState(@NonNull Bundle bundle) {
        for(Tribble tribble : tribbles)
            tribble.saveInstanceState(bundle);
    }

    public void restoreInstanceState(@NonNull Bundle bundle) {
        for(Tribble tribble : tribbles)
            tribble.restoreInstanceState(bundle);
    }

    public ArrayList<Tribble> getTribbles() {
        return tribbles;
    }
}
