package com.speedrocket.progmine.speedrocket.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ibrahim on 5/2/2018.
 */

public class ServerResponse
{
    // variable name should be same as in the json response from php


    @SerializedName("id")
    private Integer id;

    @SerializedName("success")
    boolean success;

    @SerializedName("message")
    String message;

    @SerializedName("message1")
    String message1;

    @SerializedName("product_id")
    int product_id;

    @SerializedName("code")
    int code ;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage1() {
        return message1;
    }

    public void setMessage1(String message1) {
        this.message1 = message1;
    }

    public boolean getSuccess() {
        return success;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }




} // class of ServerResponse
