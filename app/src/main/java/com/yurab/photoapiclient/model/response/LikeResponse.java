package com.yurab.photoapiclient.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikeResponse {
    @SerializedName("photo")
    @Expose
    private Photo photo;
    @SerializedName("user")
    @Expose
    private User user;

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LikeResponse{" +
                "photo=" + photo.toString() +
                ", user=" + user.toString() +
                '}';
    }
}
