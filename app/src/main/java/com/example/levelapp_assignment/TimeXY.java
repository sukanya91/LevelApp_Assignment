package com.example.levelapp_assignment;

public class TimeXY {

    private String xvalue;
    private String yvalue;
    private String timestamp;

    public TimeXY(){

    }

    public TimeXY(String xvalue, String yvalue, String timestamp) {
        this.xvalue = xvalue;
        this.yvalue = yvalue;
        this.timestamp = timestamp;
    }

    public String getXvalue() {
        return xvalue;
    }

    public String getYvalue() {
        return yvalue;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

