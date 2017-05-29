package com.yurab.photoapiclient.model.response.random_photo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yurab.photoapiclient.model.response.Links;

public class Category {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("photo_count")
    @Expose
    private Integer photoCount;
    @SerializedName("links")
    @Expose
    private Links links;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(Integer photoCount) {
        this.photoCount = photoCount;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }
}
