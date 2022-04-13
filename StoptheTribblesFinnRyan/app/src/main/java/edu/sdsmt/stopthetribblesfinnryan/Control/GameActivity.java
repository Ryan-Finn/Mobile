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
    private int hungerLevel, dayCount, eatCount, scoreCount, tribbleCount, lastBureau;
    public static final String h = "game.hunger";
    public static final String d = "game.days";
    public static final String e = "game.eat";
    public static final String s = "game.score";
    public static final String l = "game.last";
    public static final String b = "game.bureau";
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);

        view.saveInstanceState(bundle);

        bundle.putInt(h, hungerLevel);
        bundle.putInt(d, dayCount);
        bundle.putInt(e, eatCount);
        bundle.putInt(s, scoreCount);
        bundle.putInt(l, lastBureau);
        bundle.putBoolean(b, bureau);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle bundle) {
        super.onRestoreInstanceState(bundle);

        view.restoreInstanceState(bundle);

        hungerLevel = bundle.getInt(h);
        dayCount = bundle.getInt(d);
        eatCount = bundle.getInt(e);
        scoreCount = bundle.getInt(s);
        lastBureau = bundle.getInt(l);
        bureau = bundle.getBoolean(b);

        updateGUI();
    }

    private void init() {
        bureau = false;
        lastBureau = 0;
        hungerLevel = 0;
        dayCount = 1;
        scoreCount = 0;
        tribbleCount = 100;
        eatCount = 3;
        updateGUI();
    }

    public void updateGUI() {
        tribbleCount = view.getTribbleCount();

        if (hungerLevel < 0)
                hungerLevel = 0;
        else if (hungerLevel > 10)
                hungerLevel = 10;
        String text = getString(R.string.hunger) + hungerLevel;
        hunger.setText(text);

        text = getString(R.string.days) + dayCount;
        days.setText(text);

        text = "" + scoreCount;
        score.setText(text);

        text = "" + tribbleCount;
        tribbles.setText(text);

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
        if ((dayCount - lastBureau) % 2 == 0)
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
        view.eat();
        updateGUI();
    }

    public void distract(View v) {
        hungerLevel = 10;
        bureau = false;
        lastBureau = dayCount;
        updateGUI();
    }

    public void collect(View v) {
        hungerLevel += 2;
        ++scoreCount;
        updateGUI();
    }
    
    private void isEndGame() {
        if (tribbleCount >= 200 || tribbleCount <= 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
            builder.setTitle(R.string.gameOver);

            if (tribbleCount <= 0)
                builder.setMessage(R.string.win);
            else
                builder.setMessage(R.string.lose);

            builder.setPositiveButton(R.string.again, (dialog, which) -> {
                dialog.dismiss();
                reset(view);
            });
            builder.setNegativeButton(R.string.quit, (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
            builder.show();
        }
    }
}