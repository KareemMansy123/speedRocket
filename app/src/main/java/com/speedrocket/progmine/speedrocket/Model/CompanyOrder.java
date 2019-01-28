package com.speedrocket.progmine.speedrocket.Model;

/**
 * Created by Ibrahim on 8/28/2018.
 */

public class CompanyOrder {
  int id ,companyId ,detecate,productId  , price , count_items;
  String companyNameEn,companyNameAn,ProdTitleAr,ProdTitleEn,ar_description,en_description,image,created_at;

  public CompanyOrder(int id , int companyId , int detecate , int productId ,
                      String companyNameAn , String companyNameEn , String ProdTitleAr , String ProdTitleEn , String image ,String ar_description
                      ,String en_description,String created_at , int price , int count_items
                      ){
      this.id = id ;
      this.companyId =companyId;
      this.detecate = detecate ;
      this.productId = productId ;
      this.price = price ;
      this.count_items = count_items ;
      this.companyNameAn = companyNameAn ;
      this.companyNameEn = companyNameEn ;
      this.ProdTitleAr = ProdTitleAr ;
      this.ProdTitleEn = ProdTitleEn ;
      this.en_description =en_description ;
      this.ar_description =ar_description ;
      this.image = image ;
      this.created_at = created_at ;
  }

    public int getCount_items() {
        return count_items;
    }

    public void setCount_items(int count_items) {
        this.count_items = count_items;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getDetecate() {
        return detecate;
    }

    public void setDetecate(int detecate) {
        this.detecate = detecate;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCompanyNameEn() {
        return companyNameEn;
    }

    public void setCompanyNameEn(String companyNameEn) {
        this.companyNameEn = companyNameEn;
    }

    public String getCompanyNameAn() {
        return companyNameAn;
    }

    public void setCompanyNameAn(String companyNameAn) {
        this.companyNameAn = companyNameAn;
    }

    public String getProdTitleAr() {
        return ProdTitleAr;
    }

    public void setProdTitleAr(String prodTitleAr) {
        ProdTitleAr = prodTitleAr;
    }

    public String getProdTitleEn() {
        return ProdTitleEn;
    }

    public void setProdTitleEn(String prodTitleEn) {
        ProdTitleEn = prodTitleEn;
    }

    public String getAr_description() {
        return ar_description;
    }

    public void setAr_description(String ar_description) {
        this.ar_description = ar_description;
    }

    public String getEn_description() {
        return en_description;
    }

    public void setEn_description(String en_description) {
        this.en_description = en_description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}


