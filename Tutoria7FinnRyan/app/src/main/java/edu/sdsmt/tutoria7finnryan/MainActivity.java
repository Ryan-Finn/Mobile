package edu.sdsmt.tutoria7finnryan;

import androidx.appcompat.app.AppCompatActivity;

import android.location.LocationManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager = null;

    private double latitude = 0;
    private double longitude = 0;
    private boolean valid = false;
    private double toLatitude = 0;
    private double toLongitude = 0;
    private String to = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}