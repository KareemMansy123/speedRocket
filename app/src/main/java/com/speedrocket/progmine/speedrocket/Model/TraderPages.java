package com.speedrocket.progmine.speedrocket.Model;

public class TraderPages {

    TraderPages(){}

    int id ;
    String title_en , title_ar ,content_en,content_ar , image;

    TraderPages(int id , String title_ar , String title_en , String content_ar , String content_en , String image ){
        this. image = image ;
        this. id = id ;
        this. title_ar = title_ar ;
        this.title_en = title_en ;
        this. content_ar = content_ar ;
        this.content_en =content_en ;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public void setTitle_ar(String title_ar) {
        this.title_ar = title_ar;
    }

    public String getContent_en() {
        return content_en;
    }

    public void setContent_en(String content_en) {
        this.content_en = content_en;
    }

    public String getContent_ar() {
        return content_ar;
    }

    public void setContent_ar(String content_ar) {
        this.content_ar = content_ar;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
