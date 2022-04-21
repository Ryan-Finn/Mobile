/*
____ * Grading tags completed
__X__ ** Done in the WYSIWYG


Tier 1: Base Main Activity  15
____ All items there
__X__ Correct starting values/text
__X__ Different landscape layouts with all items present

Tier 2: View and Events  21
__X__ Background starts with LTGRAY
__X__ Game area has majority of screen
__X__ Tribbles are present
____ Collect works properly with 3 consecutive presses
__X__ Reset button works with tribbles, hunger, and score

Tier 3a: State Machine/Event Rules
____ Reset button still works with all required values
____ Eat button affects hunger and tribbles properly
____ Eat button limited to 3 times a day
____ New day resets the Eat and increases tribbles
____ Bureaucrat shows up at correct times.
____ Distract removes bureaucrat and increases hunger
____ Distract properly enabled/disabled  *
____ Collection removes tribbles
____ Collection affects hunger properly
____ Game lost/win triggered appropriately *
____ Dialog boxes correct
____ Bureaucrat red border when needed *

Tier 3b: Floating Action  18
____ All buttons there
____ Icons set and distinguishable
____ Opens/closes properly
____ Tribble color updated

Tier 3c: Layout ** 26
____ Custom’s View’s aspect ratio constant
__X__ Relative size of objects in view maintained
__X__ Works in required screen sizes

Tier 3d: Game Area Content  12
____ Correct grey color according to hunger
__X__ Correct number of tribbles according to count

Tier 3e: Rotation 20
__X__ Required state saved on rotation

Tier 4: Extensions  30
Extension 1: 2b 5pts: updates on newDay, eat, and reset
Extension 2: 2d 25pts
etc.
 */

package edu.sdsmt.stopthetribblesfinnryan.Control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.sdsmt.stopthetribblesfinnryan.R;
import edu.sdsmt.stopthetribblesfinnryan.View.GameView;

public class GameActivity extends AppCompatActivity {
    private StateMachine sm;
    private GameView view;
    private TextView hunger, days, score, tribbles;
    private Button eat, distract;
    private Animation fromBottom, toBottom;
    private FloatingActionButton fabDefault, fabC1, fabC2;
    private int hungerLevel, dayCount, eatCount, scoreCount, tribbleCount, lastBureau;
    public static final String h = "game.hunger";
    public static final String d = "game.days";
    public static final String e = "game.eat";
    public static final String s = "game.score";
    public static final String l = "game.last";
    public static final String b = "game.bureau";
    private boolean bureau, clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = this.findViewById(R.id.gameView);

        days = findViewById(R.id.daysCnt);
        hunger = findViewById(R.id.hungerCnt);
        score = findViewById(R.id.scoreCnt);
        tribbles = findViewById(R.id.tribbleCnt);

        eat = findViewById(R.id.eatBtn);
        distract = findViewById(R.id.distractBtn);

        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

        FloatingActionButton fab = findViewById(R.id.fab);
        fabDefault = findViewById(R.id.fabDefault);
        fabC1 = findViewById(R.id.fabColorOne);
        fabC2 = findViewById(R.id.fabColorTwo);
        fabDefault.setImageTintList(ColorStateList.valueOf(Color.BLACK));
        fabC1.setImageTintList(ColorStateList.valueOf(Color.BLUE));
        fabC2.setImageTintList(ColorStateList.valueOf(Color.RED));

        fab.setOnClickListener(v -> onClick());
        fabDefault.setOnClickListener(v -> {
            onClick();
            view.filterBitmap(Color.BLACK);
        });
        fabC1.setOnClickListener(v -> {
            onClick();
            view.filterBitmap(Color.BLUE);
        });
        fabC2.setOnClickListener(v -> {
            onClick();
            view.filterBitmap(Color.RED);
        });

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

        dayCount = bundle.getInt(d);
        hungerLevel = bundle.getInt(h);
        scoreCount = bundle.getInt(s);
        eatCount = bundle.getInt(e);
        lastBureau = bundle.getInt(l);
        bureau = bundle.getBoolean(b);

        updateGUI();
    }

    private void init() {
        dayCount = 1;
        hungerLevel = 0;
        scoreCount = 0;
        tribbleCount = 100;
        eatCount = 3;
        lastBureau = 0;
        bureau = false;
        updateGUI();
    }

    private void onClick() {
        clicked = !clicked;
        fabDefault.setEnabled(clicked);
        fabC1.setEnabled(clicked);
        fabC2.setEnabled(clicked);
        if (clicked) {
            fabDefault.setVisibility(View.VISIBLE);
            fabC1.setVisibility(View.VISIBLE);
            fabC2.setVisibility(View.VISIBLE);
            fabDefault.startAnimation(fromBottom);
            fabC1.startAnimation(fromBottom);
            fabC2.startAnimation(fromBottom);
        } else {
            fabDefault.startAnimation(toBottom);
            fabC1.startAnimation(toBottom);
            fabC2.startAnimation(toBottom);
            fabDefault.setVisibility(View.INVISIBLE);
            fabC1.setVisibility(View.INVISIBLE);
            fabC2.setVisibility(View.INVISIBLE);
        }
    }

    public void updateGUI() {
        String text = "" + dayCount;
        days.setText(text);

        if (hungerLevel < 0)
                hungerLevel = 0;
        else if (hungerLevel > 10)
                hungerLevel = 10;
        text = "" + hungerLevel;
        hunger.setText(text);

        text = "" + scoreCount;
        score.setText(text);

        tribbleCount = view.getTribbleCount();
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