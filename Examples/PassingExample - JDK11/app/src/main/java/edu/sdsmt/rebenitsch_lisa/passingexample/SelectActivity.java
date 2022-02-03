package edu.sdsmt.rebenitsch_lisa.passingexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectActivity extends AppCompatActivity {

    //tag to identify information being passed to THIS activity
    public final static String EXTRA_MESSAGE = "edu.sdsmt.myapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        // Get the message from the intent (the activity that started up this activity)
        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        TextView temp = findViewById(R.id.sentIn);
        temp.setText(message);
    }


    public void onClick(View view)
    {

        Button temp = (Button)view;
        Intent resultIntent = new Intent();

        //grab the clicked button's text, and tag it for the caller activity to later find
        resultIntent.putExtra(MainActivity.RETURN_MESSAGE , temp.getText());

        //say everything went OK
        setResult(Activity.RESULT_OK, resultIntent);

        //stop this activity and return
        finish();
    }
}
