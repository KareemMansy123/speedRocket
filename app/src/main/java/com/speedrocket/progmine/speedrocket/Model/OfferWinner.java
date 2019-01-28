package com.speedrocket.progmine.speedrocket.Model;

/**
 * Created by Ibrahim on 7/16/2018.
 */

public class OfferWinner {
  private  int userid ,
          coins ,offer , offer_id ;

  private String CompanyArTitle ,
          companyEnTitle,
          companyLogo,
          firstName,
          lastName,
          offerArTitle,
          offerEnTitle ,
          offerImage ;


    public int getOffer() {
        return offer;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public OfferWinner(){}

public OfferWinner(int userid , int coins , String CompanuArTitle ,
                   String companyEnTitle , String companyLogo , String firstName ,
                   String lastName , String offerArTitle , String offerEnTitle , String offerImage ,
                   int offer , int offer_id){
    this .coins = coins ;
    this .userid = userid ;
    this .CompanyArTitle= CompanuArTitle ;
    this .companyEnTitle = companyEnTitle ;
    this .companyLogo = companyLogo ;
    this .firstName = firstName ;
    this .lastName = lastName ;
    this .offerArTitle = offerArTitle ;
    this .offerEnTitle = offerEnTitle ;
    this .offerImage = offerImage ;
    this .offer = offer ;
    this.offer_id = offer_id ;
}

    public int getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(int offer_id) {
        this.offer_id = offer_id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getCompanyArTitle() {
        return CompanyArTitle;
    }

    public void setCompanyArTitle(String companyArTitle) {
        CompanyArTitle = companyArTitle;
    }

    public String getCompanyEnTitle() {
        return companyEnTitle;
    }

    public void setCompanyEnTitle(String companyEnTitle) {
        this.companyEnTitle = companyEnTitle;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOfferArTitle() {
        return offerArTitle;
    }

    public void setOfferArTitle(String offerArTitle) {
        this.offerArTitle = offerArTitle;
    }

    public String getOfferEnTitle() {
        return offerEnTitle;
    }

    public void setOfferEnTitle(String offerEnTitle) {
        this.offerEnTitle = offerEnTitle;
    }

    public String getOfferImage() {
        return offerImage;
    }

    public void setOfferImage(String offerImage) {
        this.offerImage = offerImage;
    }
}
