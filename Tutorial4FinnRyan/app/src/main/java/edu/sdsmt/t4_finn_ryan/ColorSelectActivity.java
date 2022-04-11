package edu.sdsmt.t4_finn_ryan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ColorSelectActivity extends AppCompatActivity {
    private HatterView hatterView = null;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        hatterView.putToBundle("parameters", outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_select);

        hatterView = findViewById(R.id.hatterView);

        if (savedInstanceState != null) {
            hatterView.getFromBundle("parameters", savedInstanceState);
        }
    }

    public void selectColor(int color) {
        hatterView.setColor(color);
    }
}