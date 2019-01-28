package com.speedrocket.progmine.speedrocket.Model;

import com.google.gson.annotations.SerializedName;

//K.M

public class UsersSearchContent {
    @SerializedName("id")
    private Integer id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @SerializedName("firstName")
    private String title;

    @SerializedName("image")
    private String imageUrl;

    public UsersSearchContent(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
