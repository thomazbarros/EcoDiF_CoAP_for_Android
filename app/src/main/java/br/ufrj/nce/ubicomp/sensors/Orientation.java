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

import br.ufrj.nce.ubicomp.connection.SendDataCoAP;
import br.ufrj.nce.ubicomp.utils.Definitions;
import br.ufrj.nce.ubicomp.utils.NewService;

public class Orientation extends NewService implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;

    private SendDataCoAP sendX, sendY, sendZ;

    private SharedPreferences app = null;

    private Intent intent;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {

        super.setupThreadPolicy();

        Toast.makeText(this, "Aferição da orientação inicializando.", Toast.LENGTH_SHORT)
                .show();
        Log.d(Definitions.ORIENTATION_TAG, "onCreate da Orientation[Service].");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        intent = new Intent();
        intent.setAction(Definitions.ORIENTATION_TAG);

        if (sensor == null) {
            this.onDestroy();
        }

        time = java.lang.System.currentTimeMillis() / 1000L;
        app = PreferenceManager.getDefaultSharedPreferences(this);

        httpUrl = app.getString("Orientation_xhttpUrlET", "");
        dataStream = app.getString("Orientation_xdataStreamET", "");
        interval = Integer
                .valueOf(app.getString("Orientation_intervalET", "5"));

        sendX = new SendDataCoAP(httpUrl, dataStream, Definitions.ORIENTATION_TAG,
                time, interval);

        Log.d(Definitions.ORIENTATION_TAG,
                "******************************************");
        Log.d(Definitions.ORIENTATION_TAG, "Endereço: " + httpUrl);
        Log.d(Definitions.ORIENTATION_TAG, "Datastream: " + dataStream);
        Log.d(Definitions.ORIENTATION_TAG, "Intervalo: " + interval);
        Log.d(Definitions.ORIENTATION_TAG,
                "******************************************");

        httpUrl = app.getString("Orientation_yhttpUrlET", "");
        dataStream = app.getString("Orientation_ydataStreamET", "");
        sendY = new SendDataCoAP(httpUrl, dataStream, Definitions.ORIENTATION_TAG,
                time, interval);

        Log.d(Definitions.ORIENTATION_TAG,
                "******************************************");
        Log.d(Definitions.ORIENTATION_TAG, "Endereço: " + httpUrl);
        Log.d(Definitions.ORIENTATION_TAG, "Datastream: " + dataStream);
        Log.d(Definitions.ORIENTATION_TAG, "Intervalo: " + interval);
        Log.d(Definitions.ORIENTATION_TAG,
                "******************************************");

        httpUrl = app.getString("Orientation_zhttpUrlET", "");
        dataStream = app.getString("Orientation_zdataStreamET", "");
        sendZ = new SendDataCoAP(httpUrl, dataStream, Definitions.ORIENTATION_TAG,
                time, interval);

        Log.d(Definitions.ORIENTATION_TAG,
                "******************************************");
        Log.d(Definitions.ORIENTATION_TAG, "Endereço: " + httpUrl);
        Log.d(Definitions.ORIENTATION_TAG, "Datastream: " + dataStream);
        Log.d(Definitions.ORIENTATION_TAG, "Intervalo: " + interval);
        Log.d(Definitions.ORIENTATION_TAG,
                "******************************************");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "Aferição da orientação inicializada.",
                Toast.LENGTH_SHORT).show();
        Log.d(Definitions.ORIENTATION_TAG, "onStart da Orientation[Service].");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isServiceRunning = true;

        return START_STICKY;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(Definitions.ORIENTATION_TAG, "Acurácia mudou.");

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        result1 = Double.valueOf(event.values[0]);
        result2 = Double.valueOf(event.values[1]);
        result3 = Double.valueOf(event.values[2]);

        Log.d(Definitions.ORIENTATION_TAG,
                "Tempo:" + (java.lang.System.currentTimeMillis() / 1000L));

        if (isStarted == false) {
            isStarted = true;
            sendX.setStarted(true);
            sendY.setStarted(true);
            sendZ.setStarted(true);
            sendX.setResult(result1);
            sendY.setResult(result2);
            sendZ.setResult(result3);
            sendX.start();
            sendY.start();
            sendZ.start();
        } else {
            sendX.setResult(result1);
            sendY.setResult(result2);
            sendZ.setResult(result3);
        }

        intent.putExtra("Orientation_result1", result1.doubleValue());
        intent.putExtra("Orientation_result2", result2.doubleValue());
        intent.putExtra("Orientation_result3", result3.doubleValue());
        sendBroadcast(intent);

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Aferição da orientação desativada.", Toast.LENGTH_SHORT)
                .show();

        if (sensor != null) {
            sensorManager.unregisterListener(this);

            Log.d(Definitions.ORIENTATION_TAG,
                    "onDestroy da Orientation[Service].");

            isServiceRunning = false;

            sendX.setStarted(false);
            sendY.setStarted(false);
            sendZ.setStarted(false);
            sendX.interrupt();
            sendY.interrupt();
            sendZ.interrupt();
        }
    }
}