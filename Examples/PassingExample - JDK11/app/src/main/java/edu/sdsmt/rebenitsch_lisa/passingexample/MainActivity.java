package edu.sdsmt.rebenitsch_lisa.passingexample;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    //a tag to identify information being sent back this THIS activity
    public final static String RETURN_MESSAGE = "edu.sdsmt.myapp.RETURN_MESSAGE";

    private EditText input;
    private TextView output;

    //launcher for a custom request from a activity
    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //any target
        ActivityResultContracts.StartActivityForResult contract =
                new ActivityResultContracts.StartActivityForResult();
        resultLauncher =
                registerForActivityResult(contract, (result)->
                   { int resultCode = result.getResultCode();
                    if (resultCode == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String value = getString(R.string.returned) + data.getStringExtra(RETURN_MESSAGE);
                        output.setText( value);
                    }});

        input = findViewById(R.id.input);
        output = findViewById(R.id.output);
    }

    public void onSelection(View view)
    {
        // start up a new intent, saying it came from THIS instance, and
        //it should create a new SelectActivity
        Intent intent = new Intent(this, SelectActivity.class);

        //get the input text, and tag it so SelectActivity can find it
        String message = input.getText().toString();
        intent.putExtra(SelectActivity.EXTRA_MESSAGE,  message);
        resultLauncher.launch(intent);
    }

}
