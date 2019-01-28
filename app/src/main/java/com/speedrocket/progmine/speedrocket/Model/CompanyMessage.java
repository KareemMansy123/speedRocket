package com.speedrocket.progmine.speedrocket.Model;

/**
 * Created by Ibrahim on 9/10/2018.
 */

public class CompanyMessage {

    int seen  , id ;
    String title , message ,name ,since ;

    public CompanyMessage(){}

    public CompanyMessage(int seen ,int id , String title , String message , String name , String since){
        this.seen = seen ;
        this.title = title ;
        this.message = message ;
        this.name = name ;
        this.since = since ;
        this.id = id ;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }
}
