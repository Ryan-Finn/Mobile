package edu.sdsmt.tutoria7finnryan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActiveListener activeListener = new ActiveListener();
    private LocationManager locationManager;
    private SharedPreferences settings;
    private final static String TO = "to";
    private final static String TOLAT = "tolat";
    private final static String TOLONG = "tolong";
    private double latitude, longitude = 0;
    private boolean valid = false;
    private double toLatitude, toLongitude;
    private String to;
    private TextView viewTo, viewLatitude, viewLongitude, viewDistance, viewProvider;
    private Location locStart, locEnd;
    private static final int NEED_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

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
        viewProvider = findViewById(R.id.textProvider);
    }

    @Override
    protected void onStart() {
        super.onStart();

        TextView viewProvider = findViewById(R.id.textProvider);
        viewProvider.setText("");

        setUI();
        registerListeners();
    }

    @Override
    protected void onStop() {
        unregisterListeners();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NEED_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Try registering again
                registerListeners();
            } else {

                // permission denied, boo! Tell the users the app won't work now
                Toast.makeText(getApplicationContext(), R.string.denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUI() {
        viewTo.setText(to);
        viewLatitude.setText("");
        viewLongitude.setText("");
        viewDistance.setText("");

        if (valid) {
            viewLatitude.setText(String.format(Locale.getDefault(),"%1$6.7fm", (float)latitude));
            viewLongitude.setText(String.format(Locale.getDefault(),"%1$6.7fm", (float)longitude));
            viewDistance.setText(String.format(Locale.getDefault(),"%1$6.1fm", locStart.distanceTo(locEnd)));
        }
    }

    private void registerListeners() {
        unregisterListeners();

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);

        String bestAvailable = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            if (bestAvailable != null) {
                locationManager.requestLocationUpdates(bestAvailable, 500, 1, activeListener);
                viewProvider.setText(bestAvailable);
                Location location = locationManager.getLastKnownLocation(bestAvailable);
                onLocation(location);
            }
        else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, NEED_PERMISSIONS);


    }

    private void unregisterListeners() {
        locationManager.removeUpdates(activeListener);
    }

    private void onLocation(Location location) {
        if (location == null)
            return;

        valid = true;
        locStart = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        setUI();
    }

    private class ActiveListener implements LocationListener {
        @Override
        public void onLocationChanged(@NonNull Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LocationListener.super.onStatusChanged(provider, status, extras);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            LocationListener.super.onProviderDisabled(provider);
        }
    }
}
