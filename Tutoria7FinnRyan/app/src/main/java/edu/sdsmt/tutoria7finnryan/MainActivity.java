package edu.sdsmt.tutoria7finnryan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private SharedPreferences settings;
    private final static String TO = "to";
    private final static String TOLAT = "tolat";
    private final static String TOLONG = "tolong";
    private double latitude, longitude = 0;
    private boolean valid = false;
    private double toLatitude, toLongitude;
    private String to;
    private TextView viewTo, viewLatitude, viewLongitude, viewDistance;
    private Location locStart, locEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        to = settings.getString(TO, "McLaury Building");
        toLatitude = Double.parseDouble(settings.getString(TOLAT, "44.075104"));
        toLongitude = Double.parseDouble(settings.getString(TOLONG, "-103.206819"));

        locStart = new Location("start");
        locEnd = new Location("end");
        locEnd.setLatitude(toLatitude);
        locEnd.setLongitude(toLongitude);

        viewTo = findViewById(R.id.textTo);
        viewLatitude = findViewById(R.id.textLatitude);
        viewLongitude = findViewById(R.id.textLongitude);
        viewDistance = findViewById(R.id.textDistance);
    }

    @Override
    protected void onStart() {
        super.onStart();

        TextView viewProvider = findViewById(R.id.textProvider);
        viewProvider.setText("");

        setUI();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setUI() {
        viewTo.setText(to);
        viewLatitude.setText("");
        viewLongitude.setText("");
        viewDistance.setText("");

        if (valid) {
            viewLatitude.setText(String.format(Locale.getDefault(),"%1$6.7fm", (float)latitude));
            viewLongitude.setText(String.format(Locale.getDefault(),"%1$6.7fm", (float)longitude));

            locStart.setLatitude(latitude);
            locStart.setLongitude(longitude);
            viewDistance.setText(String.format(Locale.getDefault(),"%1$6.1fm", locStart.distanceTo(locEnd)));
        }
    }
}