package br.ufrj.nce.ubicomp.sensors;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class SensorsList {

    private Context context;
    private SensorManager sensorManager;

    public SensorsList(Context context) {
        this.context = context;

    }

    public List<Sensor> getSensorsList() {
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        return sensorManager.getSensorList(Sensor.TYPE_ALL);
    }
}
