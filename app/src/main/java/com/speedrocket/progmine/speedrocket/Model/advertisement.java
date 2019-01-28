package com.speedrocket.progmine.speedrocket.Model;

/**
 * Created by Ibrahim on 8/7/2018.
 */

public class advertisement {
    int id ,action ;

    String image ;
    String code ;
    public advertisement(){}

    public advertisement(int id , int action , String image , String code){
        this.id =id ;
        this.action =action ;
        this.image =image;
        this.code = code ;

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
