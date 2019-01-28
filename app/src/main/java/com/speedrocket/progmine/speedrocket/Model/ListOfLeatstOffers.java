package com.speedrocket.progmine.speedrocket.Model;



public class ListOfLeatstOffers {
    private int position ;
    private String arOfferTitle , enOfferTitle;


    public ListOfLeatstOffers(){};
    public ListOfLeatstOffers(int position , String arOfferTitle , String enOffeTitle ){
        this .position = position ;
        this .arOfferTitle = arOfferTitle ;
        this .enOfferTitle = enOffeTitle;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getArOfferTitle() {
        return arOfferTitle;
    }

    public void setArOfferTitle(String arOfferTitle) {
        this.arOfferTitle = arOfferTitle;
    }

    public String getEnOfferTitle() {
        return enOfferTitle;
    }

    public void setEnOfferTitle(String enOffeTitle) {
        this.enOfferTitle = enOffeTitle;
    }
}
