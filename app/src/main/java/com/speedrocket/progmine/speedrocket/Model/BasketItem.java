package com.speedrocket.progmine.speedrocket.Model;



/**
 * Created by Ibrahim on 7/31/2018.
 */

public class BasketItem {
     int id , company_id,item_id,shopper_id,type , days_remain ,number , item_qty;
    String en_title,ar_title,company_name,company_name_ar,price,image ,description_ar ,description_en ;

    public BasketItem(){}

    public BasketItem(int id , int company_id , int item_id , int shopper_id , int type , String en_title , String ar_title
    ,String company_name ,String company_name_ar , String price,String image,String description_en , String description_ar ,
                      int days_remain  , int number , int item_qty
    )
    {
        this.item_qty = item_qty;
        this.ar_title = ar_title ;
        this.company_id = company_id ;
        this.id = id ;
        this .item_id = item_id ;
        this.shopper_id = shopper_id ;
        this.type = type ;
        this.en_title = en_title ;
        this.company_name = company_name ;
        this.company_name_ar = company_name_ar ;
        this.price = price ;
        this.image =image ;
        this.description_ar = description_ar;
        this.description_en = description_en;
        this.days_remain = days_remain ;
        this.number = number ;
    }

    public BasketItem(int id , int company_id , int item_id , int shopper_id , int type , String en_title , String ar_title
            ,String company_name ,String company_name_ar , String price,String image,String description_en , String description_ar ,
                      int days_remain
    )
    {
        this.ar_title = ar_title ;
        this.company_id = company_id ;
        this.id = id ;
        this .item_id = item_id ;
        this.shopper_id = shopper_id ;
        this.type = type ;
        this.en_title = en_title ;
        this.company_name = company_name ;
        this.company_name_ar = company_name_ar ;
        this.price = price ;
        this.image =image ;
        this.description_ar = description_ar;
        this.description_en = description_en;
        this.days_remain = days_remain ;

    }

    public int getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(int item_qty) {
        this.item_qty = item_qty;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDays_remain() {
        return days_remain;
    }

    public void setDays_remain(int days_remain) {
        this.days_remain = days_remain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getShopper_id() {
        return shopper_id;
    }

    public void setShopper_id(int shopper_id) {
        this.shopper_id = shopper_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEn_title() {
        return en_title;
    }

    public void setEn_title(String en_title) {
        this.en_title = en_title;
    }

    public String getAr_title() {
        return ar_title;
    }

    public void setAr_title(String ar_title) {
        this.ar_title = ar_title;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_name_ar() {
        return company_name_ar;
    }

    public void setCompany_name_ar(String company_name_ar) {
        this.company_name_ar = company_name_ar;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription_ar() {
        return description_ar;
    }

    public void setDescription_ar(String description_ar) {
        this.description_ar = description_ar;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }
}
