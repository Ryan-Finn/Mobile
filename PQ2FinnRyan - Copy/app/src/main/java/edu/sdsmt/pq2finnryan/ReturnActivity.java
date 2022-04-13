package edu.sdsmt.pq2finnryan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

public class ReturnActivity extends AppCompatActivity {
    private int leftCount, rightCount, topCount, bottomCount = 0;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        intent = getIntent();
        leftCount = intent.getIntExtra(MainActivity.LEFT, 0);
        rightCount = intent.getIntExtra(MainActivity.RIGHT, 0);
        topCount = intent.getIntExtra(MainActivity.TOP, 0);
        bottomCount = intent.getIntExtra(MainActivity.BOTTOM, 0);

        UpdateUI();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(MainActivity.LEFT, leftCount);
        bundle.putInt(MainActivity.RIGHT, rightCount);
        bundle.putInt(MainActivity.TOP, topCount);
        bundle.putInt(MainActivity.BOTTOM, bottomCount);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        leftCount = bundle.getInt(MainActivity.LEFT);
        rightCount = bundle.getInt(MainActivity.RIGHT);
        topCount = bundle.getInt(MainActivity.TOP);
        bottomCount = bundle.getInt(MainActivity.BOTTOM);
        UpdateUI();
    }

    public void UpdateUI() {
        TextView tapView = findViewById(R.id.tap);
        NumberFormat numFormat = NumberFormat.getInstance();

        String text = getString(R.string.left) + numFormat.format(leftCount);
        text += "\n" + getString(R.string.right) + numFormat.format(rightCount);
        text += "\n" + getString(R.string.top) + numFormat.format(topCount);
        text += "\n" + getString(R.string.bottom) + numFormat.format(bottomCount);
        tapView.setText(text);
    }

    public void onReturn(View v) {
        intent.putExtra(MainActivity.LEFT, leftCount);
        intent.putExtra(MainActivity.RIGHT, rightCount);
        intent.putExtra(MainActivity.TOP, topCount);
        intent.putExtra(MainActivity.BOTTOM, bottomCount);
        finish();
    }
}
