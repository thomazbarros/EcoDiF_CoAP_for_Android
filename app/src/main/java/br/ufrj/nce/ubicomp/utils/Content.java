package br.ufrj.nce.ubicomp.utils;

/**
 * Created by thomaz on 29/02/16.
 */

public class Content {

    private int origin;
    private String datastream;
    private String feedId;
    private Double result1=null, result2=null, result3=null;

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public String getDatastream() {
        return datastream;
    }

    public void setDatastream(String datastream) {
        this.datastream = datastream;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
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



}
