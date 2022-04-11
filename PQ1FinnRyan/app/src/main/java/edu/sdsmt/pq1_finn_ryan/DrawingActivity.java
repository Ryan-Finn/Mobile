package edu.sdsmt.pq1_finn_ryan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DrawingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_drawing);

        if (bundle != null) {
            Button button = findViewById(R.id.addY);
            button.setPressed(bundle.getBoolean("addY"));
            button = findViewById(R.id.subY);
            button.setPressed(bundle.getBoolean("subY"));
            button = findViewById(R.id.addX);
            button.setPressed(bundle.getBoolean("addX"));
            button = findViewById(R.id.subX);
            button.setPressed(bundle.getBoolean("subX"));
        }

        updateUI();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);

        Button button = findViewById(R.id.addY);
        bundle.putBoolean("addY", button.isPressed());
        button = findViewById(R.id.subY);
        bundle.putBoolean("subY", button.isPressed());
        button = findViewById(R.id.addX);
        bundle.putBoolean("addX", button.isPressed());
        button = findViewById(R.id.subX);
        bundle.putBoolean("subX", button.isPressed());
    }

    public void onClick(View view) {
        updateUI();
    }

    private void updateUI() {
        DrawingView view = findViewById(R.id.drawingView);

        Button button = findViewById(R.id.addY);
        view.setAddY(button.isPressed());
        button = findViewById(R.id.subY);
        view.setSubY(button.isPressed());
        button = findViewById(R.id.addX);
        view.setAddX(button.isPressed());
        button = findViewById(R.id.subX);
        view.setSubX(button.isPressed());
    }
}