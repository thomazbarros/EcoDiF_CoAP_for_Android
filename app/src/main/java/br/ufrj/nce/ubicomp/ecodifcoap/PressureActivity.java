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

import br.ufrj.nce.ubicomp.sensors.Pressure;
import br.ufrj.nce.ubicomp.utils.ConnectionDetector;
import br.ufrj.nce.ubicomp.utils.Definitions;
import br.ufrj.nce.ubicomp.utils.NewActivity;

public class PressureActivity extends NewActivity {

    private TextView pressureResultTV;
    private EditText httpUrlET, dataStreamET, intervalET;

    private SharedPreferences app = null;

    private StrictMode.ThreadPolicy policy;

    private MyReceiver myReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onNewCreate(savedInstanceState);

        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_pressure);

        httpUrlET = (EditText) findViewById(R.id.httpUrlET);
        dataStreamET = (EditText) findViewById(R.id.datastreamET);
        intervalET = (EditText) findViewById(R.id.intervalTimeET);
        pressureResultTV = (TextView) findViewById(R.id.pressureResultTV);
        app = PreferenceManager.getDefaultSharedPreferences(this);

        httpUrlET.setText(app.getString("Pressure_httpUrlET", ""),
                TextView.BufferType.EDITABLE);
        dataStreamET.setText(app.getString("Pressure_dataStreamET", ""),
                TextView.BufferType.EDITABLE);
        intervalET.setText(app.getString("Pressure_intervalET", ""),
                TextView.BufferType.EDITABLE);

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Definitions.PRESSURE_TAG);
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
        String temp = httpUrlET.getText().toString().trim();
        if (!temp.startsWith("http://")) {
            temp = "http://" + temp;
        }
        if ((ishttpUrlValid(temp))
                && (isDataStreamValid(dataStreamET.getText().toString().trim()))
                && (isIntervalValid(intervalET.getText().toString().trim()))) {
            SharedPreferences.Editor editor = app.edit();
            editor.putString("Pressure_httpUrlET", temp);
            editor.putString("Pressure_dataStreamET", dataStreamET.getText()
                    .toString().trim());
            editor.putString("Pressure_intervalET", intervalET.getText()
                    .toString().trim());
            editor.commit();
            ConnectionDetector connectionDetector = new ConnectionDetector(
                    getApplicationContext());
            if (connectionDetector.haveConnection()) {
                startService(new Intent(this, Pressure.class));
            } else {
                showAlertDialog("Serviço não inicializado",
                        "Não há conectividade com a Internet.", false);
            }
        }
    }

    public void onClickStopService(View V) {
        stopService(new Intent(this, Pressure.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context content, Intent intent) {

            float datapassed1 = (float) intent.getDoubleExtra(
                    "Pressure_result1", 0.0);
            pressureResultTV.setText(String.valueOf(datapassed1) + " hPa");

        }

    }
}
