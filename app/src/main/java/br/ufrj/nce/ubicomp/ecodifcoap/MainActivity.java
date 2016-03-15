package br.ufrj.nce.ubicomp.ecodifcoap;


import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

    private String[] options = { "Lista de Sensores Disponíveis",
            "Notificações", "Luminosidade", "Localização", "Acelerômetro",
            "Gravidade", "Giroscópio", "Orientação", "Geomagnetismo",
            "Proximidade", "Pressão", "Temperatura Interna" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, layout,
                options);
        this.setListAdapter(adapter);
        // setContentView(R.layout.);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor;

        switch (position) {
		/* Lista de Sensores Disponíveis */
            case 0:
                startActivity(new Intent(MainActivity.this,
                        AvailableSensorsActivity.class));
                break;

		/* Notificações */
            case 1:
                //startActivity(new Intent(MainActivity.this, PushActivity.class));
                showAlertDialog("Atenção!", "Sensor indisponível no momento.", false);
                break;

		/* Luminosidade */
            case 2:
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                if (sensor != null)
                    startActivity(new Intent(MainActivity.this, LightActivity.class));
                else
                    showAlertDialog("Atenção!", "Sensor indisponível.", false);

                break;

		/* Localiza��o */
            case 3:
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
                break;

		/* Aceler�metro */
            case 4:
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                if (sensor != null)
                    startActivity(new Intent(MainActivity.this,
                            AccelerometerActivity.class));
                else
                    showAlertDialog("Atenção!", "Sensor indisponível.", false);
                break;

		/* Gravidade */
            case 5:
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
                if (sensor != null)
                    startActivity(new Intent(MainActivity.this,
                            GravityActivity.class));
                else
                    showAlertDialog("Atenção!", "Sensor indisponível.", false);
                break;

		/* Girosc�pio */
            case 6:
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                if (sensor != null)
                    startActivity(new Intent(MainActivity.this,
                            GyroscopeActivity.class));
                else
                    showAlertDialog("Atenção!", "Sensor indisponível.", false);
                break;

		/* Orienta��o */
            case 7:
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
                if (sensor != null)
                    startActivity(new Intent(MainActivity.this,
                            OrientationActivity.class));
                else
                    showAlertDialog("Atenção!", "Sensor indisponível.", false);
                break;

		/* Geomagnetismo */
            case 8:
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                if (sensor != null)
                    startActivity(new Intent(MainActivity.this,
                            MagneticFieldActivity.class));
                else
                    showAlertDialog("Atenção!", "Sensor indisponível.", false);
                break;

		/* Proximidade */
            case 9:
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                if (sensor != null)
                    startActivity(new Intent(MainActivity.this,
                            ProximityActivity.class));
                else
                    showAlertDialog("Atenção!", "Sensor indisponível.", false);
                break;

		/* Press�o */
            case 10:
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
                if (sensor != null)
                    startActivity(new Intent(MainActivity.this,
                            PressureActivity.class));
                else
                    showAlertDialog("Atenção!", "Sensor indisponível.", false);
                break;

		/* Temperatura Interna */
            case 11:
                startActivity(new Intent(MainActivity.this,
                        InternalTemperatureActivity.class));
                break;

            default:
                break;
        }

    }

    public void showAlertDialog(String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
