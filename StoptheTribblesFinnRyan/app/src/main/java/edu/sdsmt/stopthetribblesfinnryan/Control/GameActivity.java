package edu.sdsmt.stopthetribblesfinnryan.Control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.sdsmt.stopthetribblesfinnryan.R;
import edu.sdsmt.stopthetribblesfinnryan.View.GameView;

public class GameActivity extends AppCompatActivity {
    private GameView view;
    private TextView hunger, days, score, tribbles;
    private Button eat, distract;
    private int hungerLevel, dayCount, eatCount, scoreCount, tribbleCount;
    private boolean bureau;

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
        distract = findViewById(R.id.distract);

        init();
    }

    private void init() {
        bureau = false;
        hungerLevel = 0;
        dayCount = 1;
        scoreCount = 0;
        tribbleCount = 100;
        eatCount = 3;
        updateGUI();
    }

    public void updateGUI() {
        if (hungerLevel < 0)
                hungerLevel = 0;
        else if (hungerLevel > 10)
                hungerLevel = 10;
        String text = getString(R.string.hunger) + hungerLevel;
        hunger.setText(text);

        text = getString(R.string.days) + dayCount;
        days.setText(text);

        score.setText("" + scoreCount);
        tribbles.setText("" + tribbleCount);

        eat.setEnabled(eatCount != 0);
        text = getString(R.string.eat) + eatCount;
        eat.setText(text);

        distract.setEnabled(bureau);

        view.invalidate();
        
        isEndGame();
    }

    public void newDay(View v) {
        ++dayCount;
        eatCount = 3;
        if (dayCount % 2 == 0)
            bureau = true;
        view.newDay();
        updateGUI();
    }

    public void reset(View v) {
        view.reset();
        init();
    }

    public void eat(View v) {
        --eatCount;
        hungerLevel -= 3;
        updateGUI();
    }

    public void distract(View v) {
        hungerLevel = 10;
        bureau = false;
        updateGUI();
    }

    public void collect(View v) {
        hungerLevel += 2;
        updateGUI();
    }
    
    private void isEndGame() {
        if (tribbleCount >= 200 || tribbleCount <= 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
            builder.setTitle(R.string.gameOver);
            builder.setMessage(R.string.win);
            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());
            builder.show();
            finish();
        }
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