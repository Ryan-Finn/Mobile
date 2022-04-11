package edu.sdsmt.stopthetribblesfinnryan.Model;

import android.content.Context;

import java.util.ArrayList;

public class Game {
    private final ArrayList<Tribble> tribbles = new ArrayList<>();

    public Game(Context context) {
        for (int i = 0; i < 21; i++)
            tribbles.add(new Tribble(context, i, 0.2f));
    }

    public ArrayList<Tribble> getTribbles() {
        return tribbles;
    }
}
