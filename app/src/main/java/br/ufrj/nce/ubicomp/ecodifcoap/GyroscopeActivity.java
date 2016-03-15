package br.ufrj.nce.ubicomp.ecodifcoap;

import android.annotation.SuppressLint;
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

import br.ufrj.nce.ubicomp.sensors.Gyroscope;
import br.ufrj.nce.ubicomp.utils.ConnectionDetector;
import br.ufrj.nce.ubicomp.utils.Definitions;
import br.ufrj.nce.ubicomp.utils.NewActivity;

public class GyroscopeActivity extends NewActivity {

    private TextView xResultTV, yResultTV, zResultTV;
    private EditText xhttpUrlET, xdataStreamET, yhttpUrlET, ydataStreamET,
            zhttpUrlET, zdataStreamET, intervalET;

    private SharedPreferences app = null;

    private StrictMode.ThreadPolicy policy;

    protected MyReceiver myReceiver;

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onNewCreate(savedInstanceState);

        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_gyroscope);

        xhttpUrlET = (EditText) findViewById(R.id.httpUrl_x_ET);
        xdataStreamET = (EditText) findViewById(R.id.datastream_x_ET);
        yhttpUrlET = (EditText) findViewById(R.id.httpUrl_y_ET);
        ydataStreamET = (EditText) findViewById(R.id.datastream_y_ET);
        zhttpUrlET = (EditText) findViewById(R.id.httpUrl_z_ET);
        zdataStreamET = (EditText) findViewById(R.id.datastream_z_ET);
        intervalET = (EditText) findViewById(R.id.intervalTimeET);
        xResultTV = (TextView) findViewById(R.id.xResultTV);
        yResultTV = (TextView) findViewById(R.id.yResultTV);
        zResultTV = (TextView) findViewById(R.id.zResultTV);

        app = PreferenceManager.getDefaultSharedPreferences(this);

        xhttpUrlET.setText(app.getString("Gyroscope_xhttpUrlET", ""),
                TextView.BufferType.EDITABLE);
        xdataStreamET.setText(app.getString("Gyroscope_xdataStreamET", ""),
                TextView.BufferType.EDITABLE);
        yhttpUrlET.setText(app.getString("Gyroscope_yhttpUrlET", ""),
                TextView.BufferType.EDITABLE);
        ydataStreamET.setText(app.getString("Gyroscope_ydataStreamET", ""),
                TextView.BufferType.EDITABLE);
        zhttpUrlET.setText(app.getString("Gyroscope_zhttpUrlET", ""),
                TextView.BufferType.EDITABLE);
        zdataStreamET.setText(app.getString("Gyroscope_zdataStreamET", ""),
                TextView.BufferType.EDITABLE);
        intervalET.setText(app.getString("Gyroscope_intervalET", ""),
                TextView.BufferType.EDITABLE);

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Definitions.GYROSCOPE_TAG);
        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onStop()
    {
        unregisterReceiver(myReceiver);
        super.onStop();
    }

    @Override
    public void onMessageReceived(Message msg) {
        // TODO Auto-generated method stub

    }

    public void onClickStartService(View v) {
        String temp1 = xhttpUrlET.getText().toString().trim();
        if (!temp1.startsWith("http://")) {
            temp1 = "http://" + temp1;
        }
        String temp2 = yhttpUrlET.getText().toString().trim();
        if (!temp2.startsWith("http://")) {
            temp2 = "http://" + temp2;
        }
        String temp3 = zhttpUrlET.getText().toString().trim();
        if (!temp3.startsWith("http://")) {
            temp3 = "http://" + temp3;
        }

        if ((ishttpUrlValid(temp1, "X"))
                && (ishttpUrlValid(temp2, "Y"))
                && (ishttpUrlValid(temp3, "Z"))
                && (isDataStreamValid(
                xdataStreamET.getText().toString().trim(), "X"))
                && (isDataStreamValid(
                ydataStreamET.getText().toString().trim(), "Y"))
                && (isDataStreamValid(
                zdataStreamET.getText().toString().trim(), "Z"))
                && (isIntervalValid(intervalET.getText().toString().trim()))) {

            SharedPreferences.Editor editor = app.edit();
            editor.putString("Gyroscope_xhttpUrlET", temp1);
            editor.putString("Gyroscope_yhttpUrlET", temp2);
            editor.putString("Gyroscope_zhttpUrlET", temp3);
            editor.putString("Gyroscope_xdataStreamET", xdataStreamET.getText()
                    .toString().trim());
            editor.putString("Gyroscope_ydataStreamET", ydataStreamET.getText()
                    .toString().trim());
            editor.putString("Gyroscope_zdataStreamET", zdataStreamET.getText()
                    .toString().trim());
            editor.putString("Gyroscope_intervalET", intervalET.getText()
                    .toString().trim());
            editor.commit();
            ConnectionDetector connectionDetector = new ConnectionDetector(
                    getApplicationContext());
            if (connectionDetector.haveConnection()) {
                startService(new Intent(this, Gyroscope.class));
            } else {
                showAlertDialog("Serviço não inicializado",
                        "Não há conectividade com a Internet.", false);
            }
        }
    }

    public void onClickStopService(View v) {
        stopService(new Intent(this, Gyroscope.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context content, Intent intent) {

            float datapassed1 = (float) intent.getDoubleExtra(
                    "Gyroscope_result1", 0.0);
            float datapassed2 = (float) intent.getDoubleExtra(
                    "Gyroscope_result2", 0.0);
            float datapassed3 = (float) intent.getDoubleExtra(
                    "Gyroscope_result3", 0.0);
            xResultTV.setText(String.valueOf(datapassed1) + " rad/s");
            yResultTV.setText(String.valueOf(datapassed2) + " rad/s");
            zResultTV.setText(String.valueOf(datapassed3) + " rad/s");

        }
    }

}
