package com.speedrocket.progmine.speedrocket.Model;



/**
 * Created by Ibrahim on 5/14/2018.
 */

public class Product
{
    int id;
    double price;
    int companyId;
    int productQty ;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    int categoryId;
    String en_title;
    String ar_title;
    String en_description;
    String ar_description;
    String image;
    String companyName;
    int sub_categoryId ;

    public String getAr_companyName() {
        return Ar_companyName;
    }

    public void setAr_companyName(String ar_companyName) {
        Ar_companyName = ar_companyName;
    }

    String Ar_companyName;

    public  Product () {}

    public Product(int id, String en_title, String en_description, String image, String companyName,int price ) {
        this.id = id;
        this.en_title = en_title;
        this.en_description = en_description;
        this.image = image;
        this.companyName = companyName;
        this.price = price;
    }
    public Product(int id, String en_title, String en_description, String image, String companyName,
                   double price ,String ar_title , String ar_discription ,int productQty) {
        this.id = id;
        this.en_title = en_title;
        this.en_description = en_description;
        this.image = image;
        this.companyName = companyName;
        this.price = price;
        this.ar_description = ar_discription ;
        this.ar_title = ar_title ;
        this.productQty = productQty ;
    }
    public Product(int id, String en_title, String en_description, String image, String companyName,int price ,String ar_title , String ar_discription , String ar_companyName) {
        this.id = id;
        this.en_title = en_title;
        this.en_description = en_description;
        this.image = image;
        this.companyName = companyName;
        this.price = price;
        this.ar_description = ar_discription ;
        this.ar_title = ar_title ;
        this.Ar_companyName =ar_companyName ;
    }

    public  Product (int id, String en_title)
    {
        this.id = id;
        this.en_title = en_title;
    }

    public  Product (int id, String en_title , String ar_title)
    {
        this.id = id;
        this.en_title = en_title;
        this.ar_title = ar_title ;
    }

    public Product(int id, int price, int companyId, String en_title, String ar_title, String en_description, String ar_description, String image)
    {
        this.id = id;
        this.price = price;
        this.companyId = companyId;
        this.en_title = en_title;
        this.ar_title = ar_title;
        this.en_description = en_description;
        this.ar_description = ar_description;
        this.image = image;
    } // param Constructor



    public Product(int id, int price, int companyId, String en_title, String ar_title, String en_description, String ar_description, String image,String companyName)
    {
        this.id = id;
        this.price = price;
        this.companyId = companyId;
        this.en_title = en_title;
        this.ar_title = ar_title;
        this.en_description = en_description;
        this.ar_description = ar_description;
        this.image = image;
        this.companyName = companyName;
    } // param Constructor



    public  Product(String en_title,String en_description,String image,int id)
    {
        this.en_title = en_title;
        this.en_description = en_description;
        this.image = image;
        this.id = id;
    } // constructor

    public  Product(String en_title,String arTilee ,String en_description, String ar_description,String image,int id ,double price)
    {
        this.en_title = en_title;
        this.en_description = en_description;
        this.image = image;
        this.id = id;
        this.ar_title = arTilee ;
        this.ar_description = ar_description;
        this.price = price;
    } // constructor

    public  Product(int CategoryId, String en_title,String arTilee ,String en_description, String ar_description,String image,int id ,int productQty )
    {
        this.en_title = en_title;
        this.en_description = en_description;
        this.image = image;
        this.id = id;
        this.ar_title = arTilee ;
        this.ar_description = ar_description;
        this.categoryId = CategoryId ;
        this.productQty = productQty ;

    } // constructor


    public  Product(String en_title,String en_description,String image,int id,double price , int productQty)
    {
        this.en_title = en_title;
        this.en_description = en_description;
        this.image = image;
        this.id = id;
        this.price = price;
        this.productQty = productQty ;
    } // constructor

    public int getProductQty() {
        return productQty;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public int getSub_categoryId() {
        return sub_categoryId;
    }

    public void setSub_categoryId(int sub_categoryId) {
        this.sub_categoryId = sub_categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
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

    public String getEn_discription() {
        return en_description;
    }

    public void setEn_discription(String en_discription) {
        this.en_description = en_discription;
    }

    public String getAr_discription() {
        return ar_description;
    }

    public void setAr_discription(String ar_discription) {
        this.ar_description = ar_discription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}// class Product
