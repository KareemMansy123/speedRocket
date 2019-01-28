package com.speedrocket.progmine.speedrocket.Control;

public class Messages {
    private String message,type;
    private long time;
    private boolean seen;
    private int from;

    public Messages(){

    }

    public Messages(String message, String type, long time, boolean seen, int from) {
        this.message = message;
        this.type = type;
        this.time = time;
        this.seen = seen;
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public  int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

}
