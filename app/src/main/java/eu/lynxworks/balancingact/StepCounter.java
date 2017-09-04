package eu.lynxworks.balancingact;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class StepCounter extends AppCompatActivity implements SensorEventListener {
    /*  Creating a SensorManager to control the sensor and an accompanying sensor object
        as the step counter.
     */
    private SensorManager aSensorManager;
    private Sensor aSensor;

    private int STEP_COUNT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        aSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        aSensor = aSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        this.updateDisplay();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            STEP_COUNT++;
            this.updateDisplay();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private String updateDisplay(){
        return (STEP_COUNT + " steps or " + convertStepsMiles() + " miles.");
    }

    private String convertStepsMiles() {
        // Assuming a stride length of 2 1/2 feet, 1 step is 0.0005 miles.
        double distance = 0.0005;
        distance = STEP_COUNT * distance;
        return String.valueOf(distance);
    }

    @Override
    protected void onPause() {
        /*  Registering the listener on resume as it is unregistered whenever the app is
            in the background to save battery.
         */
        super.onPause();
        aSensorManager.unregisterListener(this, aSensor);
    }

    @Override
    protected void onResume() {
        /*  Unregistering the listener reduces battery consumption when the app is in the
            background.
         */
        super.onResume();
        aSensorManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
