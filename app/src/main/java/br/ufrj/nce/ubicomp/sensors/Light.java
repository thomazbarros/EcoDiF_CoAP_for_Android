package br.ufrj.nce.ubicomp.sensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Handler;
import android.os.IBinder;

import android.preference.PreferenceManager;

import android.util.Log;

import android.widget.Toast;

import br.ufrj.nce.ubicomp.utils.NewService;
import br.ufrj.nce.ubicomp.utils.Definitions;
import br.ufrj.nce.ubicomp.connection.SendDataCoAP;

public class Light extends NewService implements SensorEventListener {

    public static Handler lightServiceHandler = null;

    private SensorManager sensorManager;
    private Sensor sensor;

    // private SendDataToEcoDiF sendDataToEcodif;
    private SendDataCoAP sendData;

    private SharedPreferences app = null;

    protected Intent intent;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {

        super.setupThreadPolicy();

        Toast.makeText(this, "Iniciando aferição da luminosidade.", Toast.LENGTH_SHORT)
                .show();
        Log.d(Definitions.LIGHT_TAG, "onCreate da Light[Service].");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        intent = new Intent();
        intent.setAction(Definitions.LIGHT_TAG);

        if (sensor == null) {
            this.onDestroy();
        }

        time = java.lang.System.currentTimeMillis() / 1000L;
        app = PreferenceManager.getDefaultSharedPreferences(this);

        httpUrl = app.getString("Light_httpUrlET", "");
        dataStream = app.getString("Light_dataStreamET", "");
        interval = Integer.valueOf(app.getString("Light_intervalET", "5"));

        Log.d(Definitions.LIGHT_TAG,
                "******************************************");
        Log.d(Definitions.LIGHT_TAG, "Endereço: " + httpUrl);
        Log.d(Definitions.LIGHT_TAG, "Datastream: " + dataStream);
        Log.d(Definitions.LIGHT_TAG, "Intervalo: " + interval);
        Log.d(Definitions.LIGHT_TAG,
                "******************************************");

        sendData = new SendDataCoAP(httpUrl, dataStream, Definitions.LIGHT_TAG,
                time, interval, false);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "Aferição da luminosidade inicializada.",
                Toast.LENGTH_SHORT).show();
        Log.d(Definitions.LIGHT_TAG, "onStart da Light[Service].");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isServiceRunning = true;

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Aferição da luminosidade desativada.",
                Toast.LENGTH_SHORT).show();
        if (sensor != null) {
            sensorManager.unregisterListener(this);

            Log.d(Definitions.LIGHT_TAG, "onDestroy da Light[Service].");
            isServiceRunning = false;

            sendData.setStarted(false);
            sendData.interrupt();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(Definitions.LIGHT_TAG, "Acurácia mudou.");

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub

        result1 = Double.valueOf(event.values[0]);
        Log.d(Definitions.LIGHT_TAG, "--->Leitura: " + result1.floatValue()
                + "   <--");
        Log.d(Definitions.LIGHT_TAG,
                "Tempo: " + (java.lang.System.currentTimeMillis() / 1000L));

        if (isStarted == false) {
            isStarted = true;

            sendData.setStarted(true);
            sendData.setResult(result1.doubleValue());
            sendData.start();

        } else {

            sendData.setResult(result1.doubleValue());

        }

        intent.putExtra("Light_result1", result1.doubleValue());
        sendBroadcast(intent);

    }

}