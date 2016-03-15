package br.ufrj.nce.ubicomp.ecodifcoap;

import android.app.ListActivity;
//import android.content.Context;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;

import br.ufrj.nce.ubicomp.sensors.SensorsList;

public class AvailableSensorsActivity extends ListActivity {

    private SensorsList sensorsList;
    private List<Sensor> list;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        sensorsList = new SensorsList(getApplicationContext());
        list = sensorsList.getSensorsList();
        String array[] = new String[list.size()];

        for(int i=0;i<array.length;i++){
            Sensor s = list.get(i);
            array[i] = s.getName() + " - " + s.getVendor() + " - " + s.getType();

        }
        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, layout, array);
        this.setListAdapter(adaptador);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // Recupera o sensor selecionado
        Sensor s = list.get(position);
        String msg = s.getName() + " - " + s.getVendor() + " - " + s.getType();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
