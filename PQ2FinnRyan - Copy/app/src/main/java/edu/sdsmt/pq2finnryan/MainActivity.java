package edu.sdsmt.pq2finnryan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private final DatabaseReference tapRef = FirebaseDatabase.getInstance().getReference().child("tap");
    public static final String LEFT = "activity.left";
    public static final String RIGHT = "activity.right";
    public static final String TOP = "activity.top";
    public static final String BOTTOM = "activity.bottom";
    private int leftCount = 0;
    private int rightCount = 0;
    private int topCount = 0;
    private int bottomCount = 0;
    private TextView leftView;
    private TextView rightView;
    private TextView topView;
    private TextView bottomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainView view = findViewById(R.id.mainView);
        view.init(this);

        leftView = findViewById(R.id.leftCnt);
        rightView = findViewById(R.id.rightCnt);
        topView = findViewById(R.id.topCnt);
        bottomView = findViewById(R.id.bottomCnt);

        UpdateUI();

        MonitorCloud monitor = MonitorCloud.INSTANCE;
        monitor.createUser("FINN", "test@gmail.com", "password");
        monitor.login("FINN", "password");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(LEFT, leftCount);
        bundle.putInt(RIGHT, rightCount);
        bundle.putInt(TOP, topCount);
        bundle.putInt(BOTTOM, bottomCount);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        leftCount = bundle.getInt(LEFT);
        rightCount = bundle.getInt(RIGHT);
        topCount = bundle.getInt(TOP);
        bottomCount = bundle.getInt(BOTTOM);
        UpdateUI();
    }

    private void UpdateUI() {
        NumberFormat numFormat = NumberFormat.getInstance();

        String text = getString(R.string.left) + numFormat.format(leftCount);
        leftView.setText(text);

        text = getString(R.string.right) + numFormat.format(rightCount);
        rightView.setText(text);

        text = getString(R.string.top) + numFormat.format(topCount);
        topView.setText(text);

        text = getString(R.string.bottom) + numFormat.format(bottomCount);
        bottomView.setText(text);
    }

    public void incrementBottom() {
        bottomCount++;
        UpdateUI();
    }

    public void incrementLeft() {
        leftCount++;
        UpdateUI();
    }

    public void incrementRight() {
        rightCount++;
        UpdateUI();
    }

    public void incrementTop() {
        topCount++;
        UpdateUI();
    }

    public void onLoad(View view) {
        tapRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent intent = new Intent(MainActivity.this, ReturnActivity.class);
                intent.putExtra(LEFT, snapshot.child("left").getValue(int.class));
                intent.putExtra(RIGHT, snapshot.child("right").getValue(int.class));
                intent.putExtra(TOP, snapshot.child("top").getValue(int.class));
                intent.putExtra(BOTTOM, snapshot.child("bottom").getValue(int.class));

                MainActivity.this.startActivity(intent);

                leftCount = intent.getIntExtra(LEFT, 0);
                rightCount = intent.getIntExtra(RIGHT, 0);
                topCount = intent.getIntExtra(TOP, 0);
                bottomCount = intent.getIntExtra(BOTTOM, 0);

                UpdateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void onSave(View view) {
        HashMap<String, Object> res = new HashMap<>();
        res.put("/left", leftCount);
        res.put("/right", rightCount);
        res.put("/top", topCount);
        res.put("/bottom", bottomCount);
        tapRef.updateChildren(res);
    }
}