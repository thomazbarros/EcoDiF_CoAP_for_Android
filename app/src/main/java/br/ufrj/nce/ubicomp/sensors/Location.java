package br.ufrj.nce.ubicomp.sensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import br.ufrj.nce.ubicomp.connection.SendDataCoAP;
import br.ufrj.nce.ubicomp.utils.Definitions;
import br.ufrj.nce.ubicomp.utils.MyXML;
import br.ufrj.nce.ubicomp.utils.NewService;


public class Location extends NewService implements LocationListener {

    public static Handler locationServiceHandler = null;

    public static Boolean isServiceRunning = false;

    private LocationManager locationManager;

    private String provider;

    private SharedPreferences app = null;

    private SendDataCoAP sendLatitude, sendLongitude;

    private Intent intent;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.setupThreadPolicy();

        app = PreferenceManager.getDefaultSharedPreferences(this);
        Log.d(Definitions.LOCATION_TAG, "onCreate da Localização[Service].");
        Toast.makeText(this, "Geolocalizaçãoo inicializando.", Toast.LENGTH_LONG)
                .show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        locationManager.requestLocationUpdates(provider, 1, 1, this);

        time = java.lang.System.currentTimeMillis() / 1000L;
        app = PreferenceManager.getDefaultSharedPreferences(this);

        intent = new Intent();
        intent.setAction(Definitions.LOCATION_TAG);

        httpUrl = app.getString("Location_httpUrlLatitudeET", "");
        dataStream = app.getString("Location_datastreamLatitudeET", "");
        interval = Integer.valueOf(app.getString("Location_intervalET", ""));
        sendLatitude = new SendDataCoAP(httpUrl, dataStream,
                Definitions.LOCATION_TAG, time, interval, false);

        Log.d(Definitions.LOCATION_TAG,
                "******************************************");
        Log.d(Definitions.LOCATION_TAG, "Endere�o: " + httpUrl);
        Log.d(Definitions.LOCATION_TAG, "Datastream: " + dataStream);
        Log.d(Definitions.LOCATION_TAG, "Intervalo: " + interval);
        Log.d(Definitions.LOCATION_TAG,
                "******************************************");

        httpUrl = app.getString("Location_httpUrlLongitudeET", "");
        dataStream = app.getString("Location_datastreamLongitudeET", "");
        sendLongitude = new SendDataCoAP(httpUrl, dataStream,
                Definitions.LOCATION_TAG, time, interval, false);

        Log.d(Definitions.LOCATION_TAG,
                "******************************************");
        Log.d(Definitions.LOCATION_TAG, "Endereço: " + httpUrl);
        Log.d(Definitions.LOCATION_TAG, "Datastream: " + dataStream);
        Log.d(Definitions.LOCATION_TAG, "Intervalo: " + interval);
        Log.d(Definitions.LOCATION_TAG,
                "******************************************");

    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "Geolocalizaçãoo inicializada.", Toast.LENGTH_SHORT)
                .show();
        Log.d(Definitions.LOCATION_TAG, "onStart da Localização[Service]");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isServiceRunning = true;
        return START_STICKY;
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        result1 = Double.valueOf(location.getLatitude());
        result2 = Double.valueOf(location.getLongitude());
        Log.d(Definitions.LOCATION_TAG,
                "### Mediçãoo --> Latitude: " + result1.floatValue()
                        + " Longitude: " + result2.floatValue() + " ###");
        Log.d(Definitions.LOCATION_TAG,
                MyXML.myToXML("Teste LAT", result1.doubleValue()));
        Log.d(Definitions.LOCATION_TAG,
                MyXML.myToXML("Teste LONG", result2.doubleValue()));

        if (isStarted == false) {

            isStarted = true;
            sendLatitude.setStarted(true);
            sendLongitude.setStarted(true);
            sendLatitude.setResult(result1.doubleValue());
            sendLongitude.setResult(result2.doubleValue());
            sendLatitude.start();
            sendLongitude.start();

        } else {

            sendLatitude.setResult(result1.doubleValue());
            sendLongitude.setResult(result2.doubleValue());

        }

        intent.putExtra("Location_result1", result1.doubleValue());
        intent.putExtra("Location_result2", result2.doubleValue());
        sendBroadcast(intent);

    }

    @Override
    public void onDestroy() {

        Log.d(Definitions.LOCATION_TAG, "onDestroy da Light[Service].");
        Toast.makeText(this, "Aferição da geolocalização desativada.",
                Toast.LENGTH_LONG).show();

        isServiceRunning = false;

        sendLatitude.setStarted(false);
        sendLongitude.setStarted(false);
        sendLatitude.interrupt();
        sendLongitude.interrupt();

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}
