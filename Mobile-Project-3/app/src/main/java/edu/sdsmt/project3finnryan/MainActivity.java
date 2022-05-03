package edu.sdsmt.project3finnryan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private MainView view;

    private double latitude, longitude = 0;
    private boolean valid = false;
    private TextView viewProvider, viewLatitude, viewLongitude;
    private static final int NEED_PERMISSIONS = 1;
    private static int surfaceRotation;
    private static float x, y, z = 0;

    private AccelListener accelListener;
    private ActiveListener activeListener;
    private OrientationEventListener orientationListener;

    private Sensor accelSensor;
    private SensorManager sensorManager;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        view = this.findViewById(R.id.mainView);

        viewProvider = findViewById(R.id.textProvider);
        viewLatitude = findViewById(R.id.textLatitude);
        viewLongitude = findViewById(R.id.textLongitude);

        activeListener = new ActiveListener();
        orientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_UI) {
            @Override
            public void onOrientationChanged(int i) {
                getOrientation();
            }
        };

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
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
    public void onResume() {
        super.onResume();
        orientationListener.enable();

        sensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelSensor != null) {
            accelListener = new AccelListener();
            sensorManager.registerListener(accelListener, accelSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        orientationListener.disable();

        if (accelSensor != null) {
            sensorManager.unregisterListener(accelListener);
            accelListener = null;
            accelSensor = null;
        }
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
        viewLatitude.setText("");
        viewLongitude.setText("");

        if (!valid)
            return;

        viewLatitude.setText(String.format(Locale.getDefault(),"%1$6.7fm", (float)latitude));
        viewLongitude.setText(String.format(Locale.getDefault(),"%1$6.7fm", (float)longitude));
        view.setStartLocation((float)latitude, (float)longitude);
        view.setLocation((float)latitude, (float)longitude);
    }

    private void getOrientation() {
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        surfaceRotation = wm.getDefaultDisplay().getRotation();
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
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        setUI();
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

    private class AccelListener implements SensorEventListener {
        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {}

        @Override
        public void onSensorChanged(SensorEvent event) {
            float a = 0.95f;
            float ax, ay, az;
            x = (1 - a) * event.values[0] + a * x;
            y = (1 - a) * event.values[1] + a * y;
            z = (1 - a) * event.values[2] + a * z;

            switch(surfaceRotation) {
                default:
                    ax = x;
                    ay = y;
                    az = z;
                    break;
                case Surface.ROTATION_90:
                    ax = -y;
                    ay = x;
                    az = z;
                    break;
                case Surface.ROTATION_180:
                    ax = -x;
                    ay = -y;
                    az = z;
                    break;
                case Surface.ROTATION_270:
                    ax = y;
                    ay = -x;
                    az = z;
                    break;
            }

            view.setScale(az / 10f);
        }
    }
}
