package com.speedrocket.progmine.speedrocket.Model;

public class Testact {

    private int id;
    private String firstName , image;

    public Testact(int id , String firstName , String image){
        this.firstName =firstName ;
        this.id =id ;
        this.image = image ;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
