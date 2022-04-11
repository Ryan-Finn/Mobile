package edu.sdsmt.stopthetribblesfinnryan.Control;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import edu.sdsmt.stopthetribblesfinnryan.R;

public class GameActivity extends AppCompatActivity {
    private TextView hunger, days;
    private Button eat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}