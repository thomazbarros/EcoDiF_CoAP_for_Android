package br.ufrj.nce.ubicomp.ecodifcoap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import br.ufrj.nce.ubicomp.sensors.Location;
import br.ufrj.nce.ubicomp.utils.ConnectionDetector;
import br.ufrj.nce.ubicomp.utils.Definitions;
import br.ufrj.nce.ubicomp.utils.NewActivity;

public class LocationActivity extends NewActivity {

    private StrictMode.ThreadPolicy policy;

    private SharedPreferences app = null;

    private EditText httpUrlLatitudeET, httpUrlLongitudeET,
            datastreamLatitudeET, datastreamLongitudeET, intervalET;
    private TextView latitudeResultTV, longitudeResultTV;

    private MyReceiver myReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onNewCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        app = PreferenceManager.getDefaultSharedPreferences(this);

        httpUrlLatitudeET = (EditText) findViewById(R.id.httpUrlLatitudeET);
        httpUrlLongitudeET = (EditText) findViewById(R.id.httpUrlLongitudeET);
        intervalET = (EditText) findViewById(R.id.intervalTimeET);
        datastreamLatitudeET = (EditText) findViewById(R.id.datastreamLatitudeET);
        datastreamLongitudeET = (EditText) findViewById(R.id.datastreamLongitudeET);
        latitudeResultTV = (TextView) findViewById(R.id.latitudeResultTV);
        longitudeResultTV = (TextView) findViewById(R.id.longitudeResultTV);

        httpUrlLatitudeET.setText(app.getString("Location_httpUrlLatitudeET",
                ""));
        httpUrlLongitudeET.setText(app.getString("Location_httpUrlLongitudeET",
                ""));
        datastreamLatitudeET.setText(app.getString(
                "Location_datastreamLatitudeET", ""));
        datastreamLongitudeET.setText(app.getString(
                "Location_datastreamLongitudeET", ""));
        intervalET.setText(app.getString("Location_intervalET", ""));

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Definitions.LOCATION_TAG);
        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onStop(){
        unregisterReceiver(myReceiver);
        super.onStop();
    }

    @Override
    public void onMessageReceived(Message msg) {
        // TODO Auto-generated method stub

    }

    public void onClickStartService(View v) {

        String temp1 = httpUrlLatitudeET.getText().toString().trim();
        if (!temp1.startsWith("http://")) {
            temp1 = "http://" + temp1;
        }
        String temp2 = httpUrlLongitudeET.getText().toString().trim();
        if (!temp2.startsWith("http://")) {
            temp2 = "http://" + temp2;
        }

        if ((ishttpUrlValid(temp1, "latitude"))
                && (ishttpUrlValid(temp2, "longitude"))
                && (isDataStreamValid(datastreamLatitudeET.getText().toString()
                .trim(), "latitude"))
                && (isDataStreamValid(datastreamLongitudeET.getText()
                .toString().trim(), "longitude"))
                && (isIntervalValid(intervalET.getText().toString().trim()))) {
            SharedPreferences.Editor editor = app.edit();
            editor.putString("Location_httpUrlLatitudeET", temp1);
            editor.putString("Location_httpUrlLongitudeET", temp2);
            editor.putString("Location_datastreamLatitudeET",
                    datastreamLatitudeET.getText().toString().trim());
            editor.putString("Location_datastreamLongitudeET",
                    datastreamLongitudeET.getText().toString().trim());
            editor.putString("Location_intervalET", intervalET.getText()
                    .toString().trim());
            editor.commit();
            ConnectionDetector connectionDetector = new ConnectionDetector(
                    getApplicationContext());
            if (connectionDetector.haveConnection()) {
                startService(new Intent(this, Location.class));
            } else {
                showAlertDialog("Servi�o n�o inicializado",
                        "N�o h� conectividade com a Internet.", false);
            }
        }

    }

    public void onClickStopService(View V) {
        stopService(new Intent(this, Location.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            double datapassed1 = intent.getDoubleExtra("Location_result1", 0.0);
            double datapassed2 = intent.getDoubleExtra("Location_result2", 0.0);
            latitudeResultTV.setText(String.valueOf(datapassed1));
            longitudeResultTV.setText(String.valueOf(datapassed2));

        }

    }

}
