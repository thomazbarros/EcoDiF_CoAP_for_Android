package br.ufrj.nce.ubicomp.ecodifcoap;

/**
 * Created by thomaz on 06/03/16.
 */
public class PushActivity {
}
/*
import ufrj.ubicomp.sensors.Light;
import ufrj.ubicomp.utils.ConnectionDetector;
import ufrj.ubicomp.utils.Definitions;
import ufrj.ubicomp.utils.NewActivity;

import com.google.android.gcm.GCMRegistrar;

import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PushActivity extends NewActivity {

	private Button btnRegistrar;
	private EditText httpUrlET;

	private SharedPreferences app = null;

	private StrictMode.ThreadPolicy policy;

	// Receiver para receber a mensagem do Service por Intent
	private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String msg = intent.getExtras().getString("msg");
			exibirMensagem(msg);

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// super.onCreate(savedInstanceState);
		super.onNewCreate(savedInstanceState);

		policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		setContentView(R.layout.activity_push);

		// Bot�es da tela
		btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
		httpUrlET = (EditText) findViewById(R.id.httpUrlET);

		app = PreferenceManager.getDefaultSharedPreferences(this);

		httpUrlET.setText(app.getString("Push_httpUrlET", "Exemplo: http://www.ecodif.com.br/EcodifAPI/notification/FEEDID/"),
				TextView.BufferType.EDITABLE);

		// Verifica que o device pode receber pushs.
		GCMRegistrar.checkDevice(this);

		// Verifica se o Android Manifest foi configurado corretamente.
		GCMRegistrar.checkManifest(this);

		// Verifica se o dispositivo est� registrado no Google Cloud Messaging
		// for Android
		final String regId = GCMRegistrar.getRegistrationId(this);

		if (regId.equals("")) {
			// N�o est� registrado
			// btnRegistrar.setOnClickListener(onClickRegistrar());
			exibirMensagem("Dispositivo n�o registrado. Clique no bot�o Registrar.");

		} else {
			// btnRegistrar.setEnabled(false);
			exibirMensagem("Dispositivo j� registrado: " + regId);
		}

		registerReceiver(messageReceiver, new IntentFilter("RECEIVE_BCAST_GCM"));

		// Exibir o que for enviado pela notifica��o.
		String msg = getIntent().getStringExtra("msg");

		if (msg != null) {
			exibirMensagem(msg);
		}

	}

	public void onClickStartService(View v) {
		String temp = httpUrlET.getText().toString().trim();
		if (!temp.startsWith("http://")) {
			temp = "http://" + temp;
		}
		if (ishttpUrlValid(temp)&&(!temp.equals(""))) {
			SharedPreferences.Editor editor = app.edit();
			editor.putString("Push_httpUrlET", temp);
			editor.commit();
			ConnectionDetector connectionDetector = new ConnectionDetector(
					getApplicationContext());
			if (connectionDetector.haveConnection()) {
				GCMRegistrar.register(getContext(), Definitions.PROJECT_NUMBER);
				exibirMensagem("Solicita��o de registro disparada.");
			} else {
				showAlertDialog("Servi�o n�o inicializado",
						"N�o h� conectividade com a Internet.", false);
			}
		}else{
			showAlertDialog("Aten��o!", "Preencha o endere�o corretamente", false);
		}

	}

	private Context getContext() {
		return this;
	}

	private void exibirMensagem(String msg) {
		TextView text = (TextView) findViewById(R.id.tMsgRecebida);
		text.append(msg
				+ "\n------------------------------------------------------------\n");
	}

	@Override
	protected void onDestroy() {
		// Finalizando o Receiver ao servi�o do Google Cloud Messaging for
		// Android
		unregisterReceiver(messageReceiver);

		GCMRegistrar.unregister(this);
		super.onDestroy();
	}

	@Override
	public void onMessageReceived(Message msg) {
		// TODO Auto-generated method stub

	}
}

* */