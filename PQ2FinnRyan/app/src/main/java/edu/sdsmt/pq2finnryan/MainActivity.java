/*
login: ryan.finn.firebase@gmail.com
password: Psr4&igk3$5EH@Qb
 */

package edu.sdsmt.pq2finnryan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView aTxt;
    private TextView bTxt;
    private TextView cTxt;
    public static final String aCount = "main.a";
    public static final String bCount = "main.b";
    public static final String cCount = "main.c";
    public static final String totalCount = "main.total";
    private int a = 0;
    private int b = 0;
    private int c = 0;
    private int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aTxt = findViewById(R.id.aTxt);
        bTxt = findViewById(R.id.bTxt);
        cTxt = findViewById(R.id.cTxt);

        updateUI();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(aCount, a);
        bundle.putInt(bCount, b);
        bundle.putInt(cCount, c);
        bundle.putInt(totalCount, total);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle bundle) {
        super.onRestoreInstanceState(bundle);

        a = bundle.getInt(aCount);
        b = bundle.getInt(bCount);
        c = bundle.getInt(cCount);
        total = bundle.getInt(totalCount);

        updateUI();
    }

    /**
     * TODO save to Firebase
     * @param view
     */
    public void onSave(View view) {

    }

    /**
     * TODO make a dialog box titled "Database Contents",
     *  and the message is the entire contents of the database
     */
    public void onLoadFullDatabase() {

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
     * TODO make a dialog box titled "Database ONE Contents",
     * and the message is the "One" branch contents if available.
     * If not available, the message is "One: empty"
     *
     * @param view
     */
    public void loadOne(View view) {

    }

    /**
     * TODO save the total taps (in variable total)
     * @param view
     */
    public void onSaveTotal(View view) {
    }
}
