package br.ufrj.nce.ubicomp.sensors;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import br.ufrj.nce.ubicomp.connection.SendDataCoAP;
import br.ufrj.nce.ubicomp.utils.Definitions;
import br.ufrj.nce.ubicomp.utils.NewService;

public class Pressure extends NewService implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;

    private SendDataCoAP sendData;

    private SharedPreferences app = null;

    private Intent intent;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {

        super.setupThreadPolicy();

        Toast.makeText(this, "Aferição da pressão atmosférica inicializada.", Toast.LENGTH_SHORT)
                .show();
        Log.d(Definitions.PRESSURE_TAG, "onCreate da Pressure[Service].");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        intent = new Intent();
        intent.setAction(Definitions.PRESSURE_TAG);

        if (sensor == null) {
            this.onDestroy();
        }

        time = java.lang.System.currentTimeMillis() / 1000L;
        app = PreferenceManager.getDefaultSharedPreferences(this);
        // interval=5;

        httpUrl = app.getString("Pressure_httpUrlET", "");
        dataStream = app.getString("Pressure_dataStreamET", "");
        interval = Integer.valueOf(app.getString("Pressure_intervalET", "5"));

        Log.d(Definitions.PRESSURE_TAG,
                "******************************************");
        Log.d(Definitions.PRESSURE_TAG, "Endereço: " + httpUrl);
        Log.d(Definitions.PRESSURE_TAG, "Datastream: " + dataStream);
        Log.d(Definitions.PRESSURE_TAG, "Intervalo: " + interval);
        Log.d(Definitions.PRESSURE_TAG,
                "******************************************");

        sendData = new SendDataCoAP(httpUrl, dataStream, Definitions.PRESSURE_TAG,
                time, interval, false);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "Aferição da Pressão Atmosféria ativada.",
                Toast.LENGTH_SHORT).show();
        Log.d(Definitions.PRESSURE_TAG, "onStart da Pressure[Service].");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isServiceRunning = true;

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Aferição da pressão atmosférica desativada.",
                Toast.LENGTH_SHORT).show();
        if (sensor != null) {
            sensorManager.unregisterListener(this);

            Log.d(Definitions.PRESSURE_TAG, "onDestroy da Pressure[Service].");
            isServiceRunning = false;

            sendData.setStarted(false);
            sendData.interrupt();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(Definitions.PRESSURE_TAG, "Acurácia mudou.");

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        result1 = Double.valueOf(event.values[0]);

        Log.d(Definitions.PRESSURE_TAG, "--->Leitura: " + result1.floatValue()
                + "   <--");
        Log.d(Definitions.PRESSURE_TAG,
                "Tempo: " + (java.lang.System.currentTimeMillis() / 1000L));

        if (isStarted == false) {
            isStarted = true;

            sendData.setStarted(true);
            sendData.setResult(result1.doubleValue());
            sendData.start();

        } else {

            sendData.setResult(result1.doubleValue());

        }

        intent.putExtra("Pressure_result1", result1.doubleValue());
        sendBroadcast(intent);
    }

}
