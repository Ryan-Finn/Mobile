package edu.sdsmt.stopthetribblesfinnryan.Model;

import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Game {
    private int tribbleCount;
    private final ArrayList<Tribble> tribbles = new ArrayList<>();

    public Game() {
        reset();
    }

    public void newDay() {
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
        tribbles.clear();
        tribbleCount = 100;
        for (int i = 0; i < 6; i++)
            tribbles.add(new Tribble(i));
    }

    public void eat() {
        tribbleCount += 10;

        int newCount = 3;
        if (tribbleCount > 100)
            newCount = 9;
        else if (tribbleCount > 20)
            newCount = 6;

        tribbles.clear();
        for (int i = 0; i < newCount; i++)
            tribbles.add(new Tribble(newCount + i));
    }

    public void saveInstanceState(@NonNull Bundle bundle) {
        bundle.putInt("area.size", tribbles.size());
        bundle.putInt("area.count", tribbleCount);
        for(Tribble tribble : tribbles)
            tribble.saveInstanceState(bundle);
    }

    public void restoreInstanceState(@NonNull Bundle bundle) {
        tribbleCount = bundle.getInt("area.count");
        Tribble tribble;
        tribbles.clear();
        for (int i = 0; i < bundle.getInt("area.size"); i++) {
            tribble = new Tribble(i);
            tribble.restoreInstanceState(bundle);
            tribbles.add(tribble);
        }
    }

    public ArrayList<Tribble> getTribbles() {
        return tribbles;
    }

    public int getTribbleCount() {
        return tribbleCount;
    }
}
