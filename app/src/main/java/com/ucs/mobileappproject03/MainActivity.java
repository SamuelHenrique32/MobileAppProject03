package com.ucs.mobileappproject03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.ucs.mobileappproject03.bd.BDSQLiteHelper;
import com.ucs.mobileappproject03.bd.Store;
import com.ucs.mobileappproject03.localization.GPSClass;
import com.ucs.mobileappproject03.localization.HeatmapsDemoActivity;
import com.ucs.mobileappproject03.localization.StepsClass;
import com.ucs.mobileappproject03.pedometer.StepDetector;
import com.ucs.mobileappproject03.pedometer.StepListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener, StepListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    public boolean saveGPSRegistersAsArray = true;

    //-------pedometer configurations-------
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private int numSteps;
    private int numStepsAux;
    private int numStepsAuxJr;
    TextView stepsQtt;
    //-------pedometer configurations-------

    public BDSQLiteHelper bd;
    private static final String DATABASE_NAME = "mobileAppProject03DB";
    private static final String DATABASE_NAME_TEST = "mobileAppProject03DBTest";


    private final int STEPS_TO_SAVE_REGISTER = 10;
    private final int STEPS_TO_SAVE_STEPS = 100;
    private final long MILLISECONDSBYDAY = 86400000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, (R.string.open), (R.string.close));

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        actionBarDrawerToggle.syncState();

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.container_fragment, new MainFragment());

        fragmentTransaction.commit();

        bd = new BDSQLiteHelper(this.getBaseContext(), DATABASE_NAME);
        Store.bd = bd;
        Store.bdTestSteps = new BDSQLiteHelper(this.getBaseContext(), DATABASE_NAME_TEST);

        //loadMapButton = findViewById(R.id.button);

        //-------pedometer configurations-------
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        stepsQtt = (TextView) findViewById(R.id.stepsQtt);
        //-------pedometer configurations-------

        askForPermissions();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        drawerLayout.closeDrawer(GravityCompat.START);

        if(menuItem.getItemId() == R.id.home){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new MainFragment());
            fragmentTransaction.commit();
        }

        if(menuItem.getItemId() == R.id.another){
            if((Store.objects == null) || (Store.objects.size() == 0)){
                Toast.makeText(this, "Aguarde a Aquisição de Dados do GPS", Toast.LENGTH_LONG).show();
            } else{
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new FragmentSecond());
                fragmentTransaction.commit();
            }
        }

        if(menuItem.getItemId() == R.id.statistics){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new StatisticsFragment());
            fragmentTransaction.commit();
        }

        return true;
    }

    private void askForPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else
            configurarServico();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    configurarServico();
                } else {
                    Toast.makeText(this, "Não vai funcionar!!!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void configurarServico(){
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location)
                {
                    atualizar(location);
                }
                public void onStatusChanged(String provider, int status, Bundle extras) { }
                public void onProviderEnabled(String provider) { }
                public void onProviderDisabled(String provider) { }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }catch(SecurityException ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void atualizar(Location location)
    {
        Double latPoint = location.getLatitude();
        Double lngPoint = location.getLongitude();

        long currentTime = Calendar.getInstance().getTime().getTime();

        GPSClass gps = new GPSClass();
        gps.setLatitude(latPoint.toString());
        gps.setLongitude(lngPoint.toString());
        gps.setData(currentTime);

        if(saveGPSRegistersAsArray){
            Store.objects = bd.getAllgps();
            saveGPSRegistersAsArray = false;
        }

        //if(numStepsAux >= STEPS_TO_SAVE_REGISTER)
        if((numStepsAux <= STEPS_TO_SAVE_REGISTER) && (!Store.inTest))
        {
            numStepsAux = 0;
            bd.addPosition(gps);
            Store.bd = bd;
            Store.objects = bd.getAllgps();
        }

    }

    //-------pedometer configurations-------
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            simpleStepDetector.updateAccel(event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs)
    {
        numSteps++;
        numStepsAux++;
        numStepsAuxJr++;

        if(numStepsAux > STEPS_TO_SAVE_REGISTER)
        {
            configurarServico();
        }

        if(numStepsAuxJr > STEPS_TO_SAVE_STEPS)
        {
            long currentTime = Calendar.getInstance().getTime().getTime();

            StepsClass steps = new StepsClass();
            steps.setPassos(numStepsAuxJr);
            steps.setData(currentTime);

            bd.addSteps(steps);
            Store.bd = bd;

            numStepsAuxJr = 0;
        }
    }
    //-------pedometer configurations-------
}