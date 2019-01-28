package com.speedrocket.progmine.speedrocket.Model;



public class Image
{
    int offerId ;
    String image ;

    public Image(int offerId, String image) {
        this.offerId = offerId;
        this.image = image;
    }


    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
} // class of Image
