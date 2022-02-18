package edu.sdsmt.rename.animationexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class AnimationActivity extends AppCompatActivity {

    /*
     * Constant strings for everything we store in the bundle
     */
    private static final String SPINNING_LINES = "spinninglines";
    private static final String SPINNING_BOX = "spinningbox";
    private static final String SPINNING_TEXT = "spinningtext";
    private static final String BOUNCY_GNOME = "bouncygnome";

    /**
     * This function is called when the activity is created
     * If there is a bundle, we are recreating a previous activity
     * and must restore the state.
     * @param bundle bundle to restore from or null if none
     */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.animation_activity);

        if(bundle != null) {
            CheckBox cb = findViewById(R.id.checkSpinningLines);
            cb.setChecked(bundle.getBoolean(SPINNING_LINES));
            cb = findViewById(R.id.checkSpinningBox);
            cb.setChecked(bundle.getBoolean(SPINNING_BOX));
            cb = findViewById(R.id.checkSpinningText);
            cb.setChecked(bundle.getBoolean(SPINNING_TEXT));
            cb = findViewById(R.id.checkBouncyGnome);
            cb.setChecked(bundle.getBoolean(BOUNCY_GNOME));
        }

        updateUI();
    }

    /**
     * Save the instance state
     * @param bundle Bundle to save to
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);

        CheckBox cb = findViewById(R.id.checkSpinningLines);
        bundle.putBoolean(SPINNING_LINES, cb.isChecked());
        cb = findViewById(R.id.checkSpinningBox);
        bundle.putBoolean(SPINNING_BOX, cb.isChecked());
        cb = findViewById(R.id.checkSpinningText);
        bundle.putBoolean(SPINNING_TEXT, cb.isChecked());
        cb = findViewById(R.id.checkBouncyGnome);
        bundle.putBoolean(BOUNCY_GNOME, cb.isChecked());
    }

    /**
     * This function is called when we have a click on any
     * @param view the source view of the event
     */
    public void onClick(View view) {
        updateUI();
    }

    private void updateUI() {
        AnimationView view = findViewById(R.id.animationView);

        CheckBox cb = findViewById(R.id.checkSpinningLines);
        view.setDrawLines(cb.isChecked());
        cb = findViewById(R.id.checkSpinningBox);
        view.setDrawBox(cb.isChecked());
        cb = findViewById(R.id.checkSpinningText);
        view.setDrawText(cb.isChecked());
        cb = findViewById(R.id.checkBouncyGnome);
        view.setDrawGnome(cb.isChecked());
    }

}
