package edu.sdsmt.stopthetribblesfinnryan.Model;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private final ArrayList<Tribble> tribbles = new ArrayList<>();
    private final Random random;
    private final Context context;

    public Game(Context context, Random random) {
        this.context = context;
        this.random = random;
        reset();
    }

    public void newDay() {
        double newCount = Math.ceil(tribbles.size() * 1.25);
        tribbles.clear();
        for (int i = 0; i < newCount; i++)
            tribbles.add(new Tribble(context, i, random));
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
        bundle.putInt("area.size", tribbles.size());
        for(Tribble tribble : tribbles)
            tribble.saveInstanceState(bundle);
    }

    public void restoreInstanceState(@NonNull Bundle bundle) {
        Tribble tribble;
        tribbles.clear();
        for (int i = 0; i < bundle.getInt("area.size"); i++) {
            tribble = new Tribble(context, i, random);
            tribble.restoreInstanceState(bundle);
            tribbles.add(tribble);
        }
    }

    public ArrayList<Tribble> getTribbles() {
        return tribbles;
    }
}
