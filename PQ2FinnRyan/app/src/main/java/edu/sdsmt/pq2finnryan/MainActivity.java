/*
login: ryan.finn.firebase@gmail.com
password: Psr4&igk3$5EH@Qb
 */

package edu.sdsmt.pq2finnryan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Count");
    private TextView aTxt;
    private TextView bTxt;
    private TextView cTxt;
    public static final String aCount = "main.a";
    public static final String bCount = "main.b";
    public static final String cCount = "main.c";
    public static final String totalCount = "main.total";
    public static final String oneBool = "main.one";
    private int a = 0;
    private int b = 0;
    private int c = 0;
    private int total = 0;
    private boolean one = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aTxt = findViewById(R.id.aTxt);
        bTxt = findViewById(R.id.bTxt);
        cTxt = findViewById(R.id.cTxt);

        updateUI();

        MonitorCloud monitor = MonitorCloud.INSTANCE;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(aCount, a);
        bundle.putInt(bCount, b);
        bundle.putInt(cCount, c);
        bundle.putInt(totalCount, total);
        bundle.putBoolean(oneBool, one);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle bundle) {
        super.onRestoreInstanceState(bundle);

        a = bundle.getInt(aCount);
        b = bundle.getInt(bCount);
        c = bundle.getInt(cCount);
        total = bundle.getInt(totalCount);
        one = bundle.getBoolean(oneBool);

        updateUI();
    }

    /**
     * @param view view
     */
    public void onSave(View view) {
        HashMap<String, Object> res = new HashMap<>();
        if (one) {
            res.put("/One/a", a);
            res.put("/One/b", b);
            res.put("/One/c", c);
            one = false;
        } else {
            res.put("/Two/a", a);
            res.put("/Two/b", b);
            res.put("/Two/c", c);
            one = true;
        }
        dataRef.updateChildren(res);
    }

    public void onLoadFullDatabase() {
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.loadDlg);

                //builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());

                String text;
                if (snapshot.getValue() == null)
                    text = "empty";
                else
                    text = snapshot.getValue().toString();

                builder.setMessage(text);
                builder.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void onReset() {
        a=0;
        b=0;
        c=0;
        updateUI();
    }

    public void onAddA() {
        a++;
        updateUI();
    }
    public void onAddBC() {
        b++;
        c++;
        updateUI();
    }
    public void onSubAC() {
        a--;
        c--;
        updateUI();
    }

    /**
     * Helper function to update the contents of the of the UI
     */
    public void updateUI(){
        aTxt.setText(String.format(Locale.getDefault(), "%d", a));
        bTxt.setText(String.format(Locale.getDefault(), "%d", b));
        cTxt.setText(String.format(Locale.getDefault(), "%d", c));
    }

    /**
     * Helper function to increment the total taps
     */
    public void incrementTotal(){
        total++;
    }

    /**
     * @param view view
     */
    public void loadOne(View view) {
        dataRef.child("One").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.loadOneDlg);

                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());
                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());

                String text = "One: ";
                if (snapshot.getValue() == null)
                    text += "empty";
                else {
                    text += snapshot.getValue().toString();
                    a = Objects.requireNonNull(snapshot.child("a").getValue(int.class));
                    b = Objects.requireNonNull(snapshot.child("b").getValue(int.class));
                    c = Objects.requireNonNull(snapshot.child("c").getValue(int.class));
                    updateUI();
                }

                builder.setMessage(text);
                builder.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /**
     * @param view view
     */
    public void onSaveTotal(View view) {
        HashMap<String, Object> res = new HashMap<>();
        res.put("/Total", total);
        dataRef.updateChildren(res);
    }
}
