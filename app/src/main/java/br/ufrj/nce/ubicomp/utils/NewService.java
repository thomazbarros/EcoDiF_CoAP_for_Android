package br.ufrj.nce.ubicomp.utils;


import android.app.Service;


import android.os.StrictMode;

public abstract class NewService extends Service {

    protected static Boolean isServiceRunning = false;

    protected boolean isStarted = false;

    protected int interval;

    protected long time;

    protected String httpUrl, dataStream;

    protected Double result1 = null, result2 = null, result3 = null;

    protected StrictMode.ThreadPolicy threadPolicy;

    public void setupThreadPolicy() {
        threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll()
                .build();
        StrictMode.setThreadPolicy(threadPolicy);
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

    public Double getResult1() {
        return result1;
    }

    public void setResult1(Double result1) {
        this.result1 = result1;
    }

    public Double getResult2() {
        return result2;
    }

    public void setResult2(Double result2) {
        this.result2 = result2;
    }

    public Double getResult3() {
        return result3;
    }

    public void setResult3(Double result3) {
        this.result3 = result3;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getDataStream() {
        return dataStream;
    }

    public void setDataStream(String dataStream) {
        this.dataStream = dataStream;
    }



}
