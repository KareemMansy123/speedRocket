package com.speedrocket.progmine.speedrocket.Model;

public class profileModel {

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    private String name;
    private String age;
    private String UserId;

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getUserId() {
        return UserId;
    }


    public profileModel() {
    }



    public profileModel(String name, String age, String userId) {
        this.name = name;
        this.age = age;
        UserId = userId;
    }
}
