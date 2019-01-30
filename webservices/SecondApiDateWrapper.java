package com.example.unamed.mvc.webservices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SecondApiDateWrapper {
    @Override
    public String toString() {
        return "FirstDateWrapper{" + "tvShow=" + tvShow + '}';
    }

    @SerializedName("tvShow")
    @Expose
    private SecondApiTvShowWrapper tvShow;

    public SecondApiTvShowWrapper getTvShow() {
        return tvShow;
    }

    public void setTvShow(SecondApiTvShowWrapper tvShow) {
        this.tvShow = tvShow;
    }


}
