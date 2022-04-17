package edu.sdsmt.tutorial7finnryan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import tutorial7finnryan.R;

public class MainActivity extends AppCompatActivity {
    private final ActiveListener activeListener = new ActiveListener();
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.itemGrubby:
                newTo(getString(R.string.grubby), 44.074834, -103.207562);
                return true;

            case R.id.itemHome:
                newTo(getString(R.string.home), 38.928325398654174, -104.70940438833615);
                return true;

            case R.id.itemMcLaury:
                newTo(getString(R.string.mclaury), 44.075104, -103.206819);
                return true;
        }

        return super.onOptionsItemSelected(item);
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
        if (requestCode != NEED_PERMISSIONS)
            return;

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            registerListeners();
            return;
        }

        Toast.makeText(getApplicationContext(), R.string.denied, Toast.LENGTH_SHORT).show();
    }

    private void setUI() {
        viewTo.setText(to);
        viewLatitude.setText("");
        viewLongitude.setText("");
        viewDistance.setText("");

        if (!valid)
            return;

        viewLatitude.setText(String.format(Locale.getDefault(),"%1$6.7fm", (float)latitude));
        viewLongitude.setText(String.format(Locale.getDefault(),"%1$6.7fm", (float)longitude));
        viewDistance.setText(String.format(Locale.getDefault(),"%1$6.1fm", locStart.distanceTo(locEnd)));
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, NEED_PERMISSIONS);
            return;
        }

        if (bestAvailable == null)
            return;

        locationManager.requestLocationUpdates(bestAvailable, 500, 1, activeListener);
        viewProvider.setText(bestAvailable);
        onLocation(locationManager.getLastKnownLocation(bestAvailable));
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

    private void newTo(String address, double lat, double lon) {
        to = address;
        toLatitude = lat;
        toLongitude = lon;
        locEnd.setLatitude(lat);
        locEnd.setLongitude(lon);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(TO, address);
        editor.putString(TOLAT, "" + lat);
        editor.putString(TOLONG, "" + lon);
        editor.apply();

        setUI();
    }

    public void onNew(View view) {
        EditText location = findViewById(R.id.editLocation);
        final String address = location.getText().toString().trim();
        newAddress(address);
    }

    private void newAddress(final String address) {
        if (address.equals(""))
            return;

        new Thread(() -> lookupAddress(address)).start();
    }

    private void lookupAddress(String address) {
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.US);
        boolean exception = false;
        List<Address> locations;

        try {
            locations = geocoder.getFromLocationName(address, 1);
        } catch (IOException ex) {
            locations = null;
            exception = true;
        }

        boolean finalException = exception;
        List<Address> finalLocations = locations;
        this.runOnUiThread(() -> newLocation(address, finalException, finalLocations));
    }

    private void newLocation(String address, boolean exception, List<Address> locations) {
        if (exception) {
            Toast.makeText(MainActivity.this, R.string.exception, Toast.LENGTH_SHORT).show();
            return;
        }

        if (locations == null || locations.size() == 0) {
            Toast.makeText(this, R.string.couldnotfind, Toast.LENGTH_SHORT).show();
            return;
        }

        EditText location = findViewById(R.id.editLocation);
        location.setText("");

        Address a = locations.get(0);
        newTo(address, a.getLatitude(), a.getLongitude());
    }

    private class ActiveListener implements LocationListener {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            onLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LocationListener.super.onStatusChanged(provider, status, extras);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            registerListeners();
        }
    }
}
