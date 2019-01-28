package com.speedrocket.progmine.speedrocket.Model;

/**
 * Created by Ibrahim on 4/23/2018.
 */

public class ProductsWinner
{
    int userId , offerId , srCoin , days  , states , qty;
    double price ;
    String en_title;

    public String getAr_title() {
        return ar_title;
    }

    public void setAr_title(String ar_title) {
        this.ar_title = ar_title;
    }

    String ar_title;
    String postImage;
    String companyName;
    String image;
    String en_description;

    public String getAr_companyName() {
        return ar_companyName;
    }

    public void setAr_companyName(String ar_companyName) {
        this.ar_companyName = ar_companyName;
    }

    String ar_companyName ;

    public String getAr_description() {
        return ar_description;
    }

    public void setAr_description(String ar_description) {
        this.ar_description = ar_description;
    }

    String ar_description;

   // Timestamp created_at ;

    public ProductsWinner(int userId, int offerId, int srCoin, String en_title) {
        this.userId = userId;
        this.offerId = offerId;
        this.srCoin = srCoin;
        this.en_title = en_title;

    } // param constructor
    public ProductsWinner(int userId, int offerId, int srCoin, String en_title,int days,String companyName,String image,double price,
                          String en_description,int states) {
        this.userId = userId;
        this.offerId = offerId;
        this.srCoin = srCoin;
        this.en_title = en_title;
        this.days = days;
        this.companyName = companyName;
        this.image = image;
        this.price = price;
        this.en_description = en_description;
        this.states = states;

    } // param constructor

    public ProductsWinner(int userId, int offerId, int srCoin, String en_title,String ar_title ,int days,String companyName, String ar_companyName,String image,double price,
                          String en_description,String ar_description ,int states , int qty) {
        this.userId = userId;
        this.offerId = offerId;
        this.srCoin = srCoin;
        this.en_title = en_title;
        this.days = days;
        this.companyName = companyName;
        this.image = image;
        this.price = price;
        this.en_description = en_description;
        this.states = states;
        this.ar_title = ar_title ;
        this.ar_description=ar_description;
        this.ar_companyName=ar_companyName;
        this.qty = qty;

    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public int getSrCoin() {
        return srCoin;
    }

    public void setSrCoin(int srCoin) {
        this.srCoin = srCoin;
    }

    public String getTitle() {
        return en_title;
    }

    public void setTitle(String en_title) {
        this.en_title = en_title;
    }

 /*   public Timestamp getCreated_at() {
        return created_at;
    }*/

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double  getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getEn_description() {
        return en_description;
    }

    public void setEn_description(String en_description) {
        this.en_description = en_description;
    }

    public int getStates() {
        return states;
    }

    public void setStates(int states) {
        this.states = states;
    }

    /* public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }*/
} // class of ProductsWinner
