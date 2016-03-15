package br.ufrj.nce.ubicomp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.URLUtil;

public abstract class NewActivity extends Activity {

    public static Handler mUiHandler = null;
    protected Bundle savedInstanceState;


    public void onNewCreate(Bundle savedInstanceState){
        this.savedInstanceState=savedInstanceState;
        super.onCreate(savedInstanceState);
        mUiHandler = new Handler(){
            public void handleMessage(Message msg){
                onMessageReceived(msg);
            }
        };

    }


    public void showAlertDialog(String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        // alertDialog.setIcon((status) ? R.drawable.s : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


    public boolean ishttpUrlValid (String httpUrl){
/*		if(httpUrl.isEmpty()){
			showAlertDialog("Atenção!", "Endereço em branco.", false);
			return false;
		}
		if(URLUtil.isHttpUrl(httpUrl)){
			return true;
		}else{
			showAlertDialog("Atenção!", "Endereço inv�lido. Utilize apenas letras e n�meros.", false);
			return false;
		}*/
        return true;
    }

    public boolean ishttpUrlValid (String httpUrl, String httpUrlId){
/*		if(httpUrl.isEmpty()){
			showAlertDialog("Atenção!", "Endereço em branco.", false);
			return false;
		}
		if(URLUtil.isHttpUrl(httpUrl)){
			return true;
		}else{
			showAlertDialog("Atenção!", "Endereço inv�lido. Utilize apenas letras e n�meros.", false);
			return false;
		}*/
        return true;
    }


    public boolean isDataStreamValid(String dataStream){
        if(dataStream.isEmpty()){
            showAlertDialog("Atenção!", "DataStream não preenchido.", false);
            return false;
        }
        for(int i =0;i<dataStream.length();i++){
            if(!Character.isLetterOrDigit(dataStream.charAt(i))){
                showAlertDialog("Atenção!", "Datastream incorreto. Use apenas e números.", false);
                return false;
            }
        }
        return true;
    }

    public boolean isDataStreamValid(String dataStream, String datastreamId){
        if(dataStream.isEmpty()){
            showAlertDialog("Atenção!", "DataStream " + datastreamId + "não preenchido.", false);
            return false;
        }
        for(int i =0;i<dataStream.length();i++){
            if(!Character.isLetterOrDigit(dataStream.charAt(i))){
                showAlertDialog("Atenção!", "Datastream " + datastreamId + " incorreto. Use apenas e números.", false);
                return false;
            }
        }
        return true;
    }

    public boolean isIntervalValid(String interval){
        String regex = "[0-9]+";
		/*showAlertDialog("DEBUG!", "Intervalo: "+interval, false);*/
        if(interval.isEmpty()){
            showAlertDialog("Atenção!", "Intervalo de transmissão não preenchido.", false);
            return false;
        }
        else if(!interval.matches(regex)){
            showAlertDialog("Atenção!", "Intervalo incorreto. Apenas números inteiros maiores que zero.", false);
            return false;
        }else if(Integer.parseInt(interval)>86400){
            showAlertDialog("Atenção!", "Utilize apenas intervalos menores que 86400.", false);
            return false;
        }else if(Integer.parseInt(interval)<1){
            showAlertDialog("Atenção!", "Utilize apenas intervalos maiores que zero.", false);
            return false;
        }else{
            return true;
        }

    }

    public abstract void onMessageReceived(Message msg);


}
