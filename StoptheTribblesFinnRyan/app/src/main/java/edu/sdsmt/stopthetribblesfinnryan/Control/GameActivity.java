package edu.sdsmt.stopthetribblesfinnryan.Control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import edu.sdsmt.stopthetribblesfinnryan.R;
import edu.sdsmt.stopthetribblesfinnryan.View.GameView;

public class GameActivity extends AppCompatActivity {
    private GameView view;
    private TextView hunger, days;
    private Button eat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = this.findViewById(R.id.GameArea);

        hunger = findViewById(R.id.hunger);
        days = findViewById(R.id.days);
        eat = findViewById(R.id.eat);

        String text = hunger.getText().toString() + "0";
        hunger.setText(text);

        text = days.getText().toString() + "1";
        days.setText(text);

        text = eat.getText().toString() + "3";
        eat.setText(text);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void updateGUI() {

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