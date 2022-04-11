package edu.sdsmt.t4_finn_ryan;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

public class HatterActivity extends AppCompatActivity {
    private HatterView hatterView = null;
    private ColorSelectView colorView = null;
    private Button colorButton = null;
    ActivityResultLauncher<String> resultLauncher;
    public static final int NEED_PERMISSIONS = 1;
    private static final String PARAMETERS = "parameters";

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        hatterView.putToBundle(PARAMETERS, outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hatterView = findViewById(R.id.hatterView);
        colorView = findViewById(R.id.colorSelectView);
        colorButton = findViewById(R.id.buttonColor);
        Spinner spinner = findViewById(R.id.spinnerHat);

        resultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new HandleResult());

        String[] hats = getResources().getStringArray(R.array.hats_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int pos, long id) {
                hatterView.setHat(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        if(savedInstanceState != null) {
            hatterView.getFromBundle(PARAMETERS, savedInstanceState);
            spinner.setSelection(hatterView.getHat());
        }
    }

    public void onPicture(View view) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, NEED_PERMISSIONS);
        }
        resultLauncher.launch("image/*");
    }

    public class HandleResult implements ActivityResultCallback<Uri> {
        @Override
        public void onActivityResult(Uri result) {
            if(result != null) { hatterView.setImageUri(result.toString()); }
        }
    }

    public void onCheck(View view) {
        hatterView.setFeather(!hatterView.getFeather());
    }

    public void onColor(View view) {
        Intent intent = new Intent(this, ColorSelectActivity.class);
        startActivity(intent);
    }
}