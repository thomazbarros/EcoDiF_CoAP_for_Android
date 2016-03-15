package br.ufrj.nce.ubicomp.connection;

/**
 * Created by thomaz on 28/02/16.
 */
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import br.ufrj.nce.ubicomp.utils.Definitions;
import br.ufrj.nce.ubicomp.utils.MyXML;

import android.util.Log;

public class SendDataCoAP extends Thread {

    private boolean isStarted = false;
    private int interval;
    private long time;
    private Double result = null;
    private String tag, coapUri, datastream;
    private String registrationId = null;
    //private CoapClient client;

    public SendDataCoAP(String coapUri, String datastream, String tag, long time,
                        int interval, boolean isStarted) {
        this.coapUri = coapUri;
        this.datastream = datastream;
        this.tag = tag;
        this.time = time;
        this.interval = interval;
        this.isStarted = isStarted;
        this.registrationId = null;
    }

    public SendDataCoAP(String coapUri, String datastream, String tag, long time,
                        int interval) {
        this.coapUri = coapUri;
        this.datastream = datastream;
        this.tag = tag;
        this.time = time;
        this.interval = interval;
        this.isStarted = false;
        this.registrationId = null;
    }

    public SendDataCoAP(String coapUri, String registrationId) {
        this.coapUri = coapUri;
        this.registrationId = registrationId;
        this.isStarted = false;
    }

    public void run() {
        isStarted=true;
        while (isStarted) {

            long currentTime;

            currentTime = (java.lang.System.currentTimeMillis() / 1000L);
            if ((currentTime - time) > (long) interval) {
                time = currentTime;
            }

            if (result != null) {
                Log.d(tag, "###Enviando Result: " + result.doubleValue());
                Log.d(tag, "###Time: " + java.lang.System.currentTimeMillis()
                        / 1000L);
                sendData(result.doubleValue());
            }

            try {
                Thread.sleep(interval * 1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if ((!isStarted) && (registrationId != null)) {
            //isStarted=true;
        }
        // return null;

    }

    public void sendData(double result) {
        String data = MyXML.myToXML(datastream, result);

        Log.d(Definitions.NOTIFICATION_TAG, "URL FORMADA: "+coapUri);

        CoapClient client = new CoapClient(coapUri);

        CoapResponse response = client.put(data, MediaTypeRegistry.APPLICATION_XML);

        Log.d(Definitions.NOTIFICATION_TAG, "Saída: "+ response.getResponseText());

    }

    public void sendData() {

    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getHttpUrl() {
        return coapUri;
    }

    public void setHttpUrl(String httpUrl) {
        this.coapUri = httpUrl;
    }

    public String getDatastream() {
        return datastream;
    }

    public void setDatastream(String datastream) {
        this.datastream = datastream;
    }

}
