package br.ufrj.nce.ubicomp.utils;


//import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.ufrj.nce.ubicomp.ecodifcoap.R;


public class Setup extends NewActivity {

    private EditText etfeed1, etdatastream1, etfeed2, etdatastream2;
    private EditText etServer;
    private Button bBegin;
    SharedPreferences app = null;
    private ConnectionDetector connectionDetector;
    StrictMode.ThreadPolicy policy;
    private String feed1, feed2, datastream1, datastream2, server;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onNewCreate(savedInstanceState);
        setTitle("EcoDiF Geolocalização - Configuração");
        setContentView(R.layout.activity_setup);
        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        app = PreferenceManager.getDefaultSharedPreferences(this);
        connectionDetector = new ConnectionDetector(getApplicationContext());
        //startService(new Intent(this, Light.class));

        if(!(app.getString("server", "VAZIO").equals("VAZIO"))){

            if(connectionDetector.haveConnection()){
                //showAlertDialog("Sucesso!", "Cadastro de feeds realizado anteriormente.", false);
                //intent = new Intent(Setup.this, LocationActivity2.class);
                //startActivity(intent);
                //finish();
            }else{
                showAlertDialog("Sem Conectividade", "Não há conexão com a Internet.", false);
            }
        }else{
            if(!connectionDetector.haveConnection()){
                showAlertDialog("Sem Conectividade", "Não há conexão com a Internet.", false);
            }
        }

        etfeed1 = (EditText) findViewById(R.id.etFeed1);
        etfeed2 = (EditText) findViewById(R.id.etFeed2);
        etdatastream1 = (EditText) findViewById(R.id.etDataStream1);
        etdatastream2 = (EditText) findViewById(R.id.etDataStream2);
        etServer = (EditText) findViewById(R.id.etServer);



    }

    public void onClickEnter(View v){
        feed1 = etfeed1.getText().toString();
        feed2 = etfeed2.getText().toString();
        datastream1 = etdatastream1.getText().toString();
        datastream2 = etdatastream2.getText().toString();
        server = etServer.getText().toString();


        if(connectionDetector.haveConnection()){
            if((isStringValid(feed1, 1))&&(isStringValid(feed2, 2))&&(isStringValid(datastream1, 3))&&(isStringValid(datastream2, 4))&&isStringValid(server, 5)){
                SharedPreferences.Editor editor = app.edit();
                editor.putString("feed1", feed1);
                editor.putString("feed2", feed2);
                editor.putString("datastream1", datastream1);
                editor.putString("datastream2", datastream2);
                editor.putString("server", server);
                editor.commit();

                //intent = new Intent(Setup.this, LocationActivity2.class);
                //startActivity(intent);
                finish();
            }
        }else{
            showAlertDialog("Sem Conectividade", "Não há conexão com a Internet.", false);
        }
    }

    @Override
    public void onMessageReceived(Message msg) {
        // TODO Auto-generated method stub

    }

    public boolean isStringValid(String in, int i){
        //System.out.println("Lido: "+in);
        if((in==null)||(in.length()==0)){
            if(i==1){
                showAlertDialog("Cadastro não realizado", "Campo feed1 vazio.", false);
            }
            else if(i==2){
                showAlertDialog("Cadastro não realizado", "Campo feed2 vazio.", false);
            }
            else if (i==3){
                showAlertDialog("Cadastro não realizado", "Campo datastream1 vazio.", false);
            }
            else if (i==4){
                showAlertDialog("Cadastro não realizado", "Campo datastream2 vazio.", false);
            }
            else if (i==5){
                showAlertDialog("Cadastro não realizado", "Campo servidor vazio.", false);
            }
            return false;
        }
        for (int j=0;j<in.length();j++){
            if((!Character.isLetterOrDigit(in.charAt(j)))&&(i!=5)){
                if(i==1){
                    showAlertDialog("Cadastro não realizado", "Utilize apenas letras ou números em feed1.", false);

                }
                else if(i==2){
                    showAlertDialog("Cadastro não realizado", "Utilize apenas letras ou números em feed2.", false);

                }
                else if(i==3){
                    showAlertDialog("Cadastro não realizado", "Utilize apenas letras ou números em datastream1.", false);
                }
                else if(i==4){
                    showAlertDialog("Cadastro não realizado", "Utilize apenas letras ou números em datastream2.", false);

                }

                return false;
            }
        }
        return true;

    }


    public boolean isStringValid(String in){
        //System.out.println("Lido: "+in);
        if((in==null)||(in.length()==0)){
            return false;
        }
        for (int j=0;j<in.length();j++){
            if(!Character.isLetterOrDigit(in.charAt(j))){
                return false;
            }
        }
        return true;

    }

}
