package edu.sdsmt.stopthetribblesfinnryan.Control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.sdsmt.stopthetribblesfinnryan.R;
import edu.sdsmt.stopthetribblesfinnryan.View.GameView;

public class GameActivity extends AppCompatActivity {
    private GameView view;
    private TextView hunger, days, score, tribbles;
    private Button eat;
    private int hungerLevel, dayCount, eatCount;
    private boolean buear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = this.findViewById(R.id.GameArea);

        hunger = findViewById(R.id.hunger);
        days = findViewById(R.id.days);
        score = findViewById(R.id.score);
        tribbles = findViewById(R.id.count);
        eat = findViewById(R.id.eat);

        init();
    }

    private void init() {
        hungerLevel = 0;
        String text = getString(R.string.hunger) + hungerLevel;
        hunger.setText(text);

        dayCount = 1;
        text = getString(R.string.days) + dayCount;
        days.setText(text);

        score.setText("0");
        tribbles.setText("100");

        eatCount = 3;
        text = getString(R.string.eat) + eatCount;
        eat.setText(text);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void updateGUI() {
        String text = getString(R.string.hunger) + hungerLevel;
        hunger.setText(text);

        eat.setEnabled(eatCount != 0);
        text = getString(R.string.eat) + eatCount;
        eat.setText(text);

        view.invalidate();
    }

    public void newDay(View v) {
        String text = getString(R.string.days) + (++dayCount);
        days.setText(text);
        eatCount = 3;
        view.newDay();
        updateGUI();
    }

    public void reset(View v) {
        init();
        view.reset();
        updateGUI();
    }

    public void eat(View v) {
        if (eatCount > 0)
            --eatCount;
        updateGUI();
    }

    public void distract(View v) {
        hungerLevel = 10;
    }

    public void collect(View v) {

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        //view.saveInstanceState(bundle);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        //view.loadInstanceState(bundle);
        updateGUI();
    }
}