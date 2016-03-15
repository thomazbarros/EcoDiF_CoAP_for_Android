package br.ufrj.nce.ubicomp.sensors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import br.ufrj.nce.ubicomp.connection.SendDataCoAP;
import br.ufrj.nce.ubicomp.utils.Definitions;
import br.ufrj.nce.ubicomp.utils.NewService;

public class InternalTemperature extends NewService {

    private SharedPreferences app = null;

    public SendDataCoAP sendData;

    protected Intent intent;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.setupThreadPolicy();

        Toast.makeText(this, "Iniciando aferição da temperatura interna.",
                Toast.LENGTH_LONG).show();
        Log.d(Definitions.INTERNALTEMPERATURE_TAG,
                "onCreate da InternalTemperature[Service].");

        time = java.lang.System.currentTimeMillis() / 1000L;

        app = PreferenceManager.getDefaultSharedPreferences(this);

        intent = new Intent();
        intent.setAction(Definitions.INTERNALTEMPERATURE_TAG);

        httpUrl = app.getString("InternalTemperature_httpUrlET", "");
        dataStream = app.getString("InternalTemperature_dataStreamET", "");
        interval = Integer.valueOf(app.getString(
                "InternalTemperature_intervalET", "5"));

        Log.d(Definitions.INTERNALTEMPERATURE_TAG,
                "******************************************");
        Log.d(Definitions.INTERNALTEMPERATURE_TAG, "Endereço: " + httpUrl);
        Log.d(Definitions.INTERNALTEMPERATURE_TAG, "Datastream: " + dataStream);
        Log.d(Definitions.INTERNALTEMPERATURE_TAG, "Intervalo: " + interval);
        Log.d(Definitions.INTERNALTEMPERATURE_TAG,
                "******************************************");

        sendData = new SendDataCoAP(httpUrl, dataStream,
                Definitions.INTERNALTEMPERATURE_TAG, time, interval);

        this.registerReceiver(this.myBatteryReceiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "Aferição da temperatura interna inicializada.",
                Toast.LENGTH_SHORT).show();
        Log.d(Definitions.INTERNALTEMPERATURE_TAG,
                "onStart da InternalTemperature[Service].");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isServiceRunning = true;

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Aferição da temperatura interna desativada.",
                Toast.LENGTH_SHORT).show();

        this.unregisterReceiver(this.myBatteryReceiver);

        Log.d(Definitions.LIGHT_TAG,
                "onDestroy da InternalTemperature[Service].");
        isServiceRunning = false;

    }

    protected BroadcastReceiver myBatteryReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent2) {
            if (intent2.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                // Voltagem
                result1 = intent2.getDoubleExtra("voltage", 0) / 1000;
                // Temperatura
                double temp = (double) (intent2.getIntExtra(
                        BatteryManager.EXTRA_TEMPERATURE, 0) / 10);

                // N�vel
                result2 = Double.valueOf(temp);
                result3 = intent2.getDoubleExtra("level", 0);
                Log.d(Definitions.INTERNALTEMPERATURE_TAG, "--->Leitura: "
                        + result2.doubleValue() + "   <--");
                Log.d(Definitions.INTERNALTEMPERATURE_TAG,
                        "Temperature: "
                                + String.valueOf((float) intent2.getIntExtra(
                                "temperature", 0) / 10) + "c");
                Log.d(Definitions.INTERNALTEMPERATURE_TAG, "Tempo: "
                        + (java.lang.System.currentTimeMillis() / 1000L));

                if (isStarted == false) {
                    isStarted = true;

                    sendData.setStarted(true);
                    sendData.setResult(result2);
                    sendData.start();

                } else {
                    sendData.setResult(result2);
                }

                intent.putExtra("InternalTemperature_result2",
                        result2.doubleValue());
                sendBroadcast(intent);
            }
        }

    };

}
