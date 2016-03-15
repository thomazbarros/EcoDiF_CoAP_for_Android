package br.ufrj.nce.ubicomp.ecodifcoap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import br.ufrj.nce.ubicomp.sensors.AmbientTemperature;
import br.ufrj.nce.ubicomp.utils.ConnectionDetector;
import br.ufrj.nce.ubicomp.utils.NewActivity;

public class AmbientTemperatureActivity extends NewActivity {

    private TextView temperatureResultTV;
    private EditText httpUrlET, dataStreamET, intervalET;

    private SharedPreferences app = null;

    private StrictMode.ThreadPolicy policy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onNewCreate(savedInstanceState);

        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_temperature);

        httpUrlET = (EditText) findViewById(R.id.httpUrlET);
        dataStreamET = (EditText) findViewById(R.id.datastreamET);
        intervalET = (EditText) findViewById(R.id.intervalTimeET);
        temperatureResultTV = (TextView) findViewById(R.id.temperatureResultTV);

        app = PreferenceManager.getDefaultSharedPreferences(this);

        httpUrlET.setText(app.getString("AmbientTemperature_httpUrlET", ""),
                TextView.BufferType.EDITABLE);
        dataStreamET.setText(
                app.getString("AmbientTemperature_dataStreamET", ""),
                TextView.BufferType.EDITABLE);
        intervalET.setText(app.getString("AmbientTemperature_intervalET", ""),
                TextView.BufferType.EDITABLE);

    }

    @Override
    public void onMessageReceived(Message msg) {
        // TODO Auto-generated method stub

    }

    public void onClickStartService(View v) {
        String temp = httpUrlET.getText().toString().trim();
        if (!temp.startsWith("http://")) {
            temp = "http://" + temp;
        }
        if ((ishttpUrlValid(temp))
                && (isDataStreamValid(dataStreamET.getText().toString().trim()))
                && (isIntervalValid(intervalET.getText().toString().trim()))) {
            SharedPreferences.Editor editor = app.edit();
            editor.putString("AmbientTemperature_httpUrlET", temp);
            editor.putString("AmbientTemperature_dataStreamET", dataStreamET
                    .getText().toString().trim());
            editor.putString("AmbientTemperature_intervalET", intervalET
                    .getText().toString().trim());
            editor.commit();
            ConnectionDetector connectionDetector = new ConnectionDetector(
                    getApplicationContext());
            if (connectionDetector.haveConnection()) {
                startService(new Intent(this, AmbientTemperature.class));
            } else {
                showAlertDialog("Serviço não inicializado",
                        "Não há conectividade com a Internet.", false);
            }
        }
    }

    public void onClickStopService(View V) {
        stopService(new Intent(this, AmbientTemperature.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            int datapassed = arg1.getIntExtra("DATAPASSED", 0);

        }

    }

}
