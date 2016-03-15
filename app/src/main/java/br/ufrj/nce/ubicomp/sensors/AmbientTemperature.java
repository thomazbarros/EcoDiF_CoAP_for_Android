package br.ufrj.nce.ubicomp.sensors;

//import android.app.AlertDialog;
import android.content.Context;
//import android.content.DialogInterface;
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

public class AmbientTemperature extends NewService implements
        SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;

    private SendDataCoAP sendData;

    private SharedPreferences app = null;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void onCreate() {

        super.setupThreadPolicy();

        Toast.makeText(this, "Sensor de temperatura  inicializando.",
                Toast.LENGTH_LONG).show();
        Log.d(Definitions.AMBIENTTEMPERATURE_TAG,
                "onCreate da AmbientTemperature[Service].");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if (sensor == null) {
            // showAlertDialog("Alerta!", "Sensor indisponível.", false);
            Log.d(Definitions.AMBIENTTEMPERATURE_TAG, "Sensor nulo!!!");
            return;
        }

        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        time = java.lang.System.currentTimeMillis() / 1000L;
        app = PreferenceManager.getDefaultSharedPreferences(this);

        httpUrl = app.getString("AmbientTemperature_httpUrlET", "");
        dataStream = app.getString("AmbientTemperature_dataStreamET", "");
        Integer temp = app.getInt("AmbientTemperature_intervalET", 5);
        interval = temp.intValue();

        Log.d(Definitions.AMBIENTTEMPERATURE_TAG, "*********************************");
        Log.d(Definitions.AMBIENTTEMPERATURE_TAG, "Endereço: " + httpUrl);
        Log.d(Definitions.AMBIENTTEMPERATURE_TAG, "Datastream: " + dataStream);
        Log.d(Definitions.AMBIENTTEMPERATURE_TAG, "Intervalo: " + interval);
        Log.d(Definitions.AMBIENTTEMPERATURE_TAG,
                "**********************************");

        sendData = new SendDataCoAP(httpUrl, dataStream,
                Definitions.AMBIENTTEMPERATURE_TAG, time, interval);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "Ambient Temperature[Serviço] inicializado.",
                Toast.LENGTH_SHORT).show();
        Log.d(Definitions.AMBIENTTEMPERATURE_TAG,
                "onStart da AmbientTemperature[Service].");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isServiceRunning = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "AmbientService[Serviço] finalizado.",
                Toast.LENGTH_SHORT).show();
        if (sensor != null) {
            sensorManager.unregisterListener(this);

            Log.d(Definitions.AMBIENTTEMPERATURE_TAG,
                    "onDestroy da Light[Service].");
            isServiceRunning = false;

            sendData.setStarted(false);
            sendData.interrupt();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(Definitions.AMBIENTTEMPERATURE_TAG, "Acurácia mudou.");

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        result1 = Double.valueOf(event.values[0]);

        Log.d(Definitions.AMBIENTTEMPERATURE_TAG,
                "--->Leitura: " + result1.floatValue() + "   <--");
        Log.d(Definitions.AMBIENTTEMPERATURE_TAG, "Tempo: "
                + (java.lang.System.currentTimeMillis() / 1000L));

        if (isStarted == false) {
            isStarted = true;

            sendData.setStarted(true);
            sendData.setResult(result1.doubleValue());
            sendData.start();

        } else {
            sendData.setResult(result1.doubleValue());
        }

    }

    public void onPause() {
        sensorManager.unregisterListener(this);
    }

    public void onRestart() {
        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

}
