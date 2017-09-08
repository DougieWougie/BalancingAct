package eu.lynxworks.balancingact;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    fragment = HomeFragment.instance();
                    break;
                case R.id.nav_exercise:
                    fragment = ExerciseFragment.instance();
                    break;
                case R.id.nav_food:
                    fragment = FoodFragment.instance();
                    break;
            }
            if (fragment == null){
                return false;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*  Function is called when created - almost a constructor for the Activity:
            1. Inflate the XML view associated with this activity
            2. Build and populate a toolbar
            3. Build and populate a bottom navigation bar
            4. Load the first fragment (Home)
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Global global = Global.getGlobalInstance();
        global.setStepCount(0);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, HomeFragment.instance());
        transaction.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Sensor stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case R.id.action_user:
                /*  Start the user activity. */
                Intent userIntent = new Intent(findViewById(android.R.id.content).getContext(), UserActivity.class);
                startActivity(userIntent);
                return true;
            case R.id.action_settings:
                /*  Start the settings activity. */
                return true;
            default:
                /*  Shouldn't really be able to get here but if we do the superclass can
                    handle it!
                 */
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_STEP_DETECTOR) {
            Global global = Global.getGlobalInstance();
            int steps = global.getStepCount();
            steps++;
            global.setStepCount(steps);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
