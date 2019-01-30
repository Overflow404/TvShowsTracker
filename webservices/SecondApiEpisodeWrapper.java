package com.example.unamed.mvc.webservices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SecondApiEpisodeWrapper {
    @Override
    public String toString() {
        return "Episode{" + "season=" + season + ", episode=" + episode + ", name='" + name +
                '\'' + ", airDate='" + airDate + '\'' + '}';
    }

    @SerializedName("season")
    @Expose
    private Integer season;
    @SerializedName("episode")
    @Expose
    private Integer episode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("air_date")
    @Expose
    private String airDate;

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getEpisode() {
        return episode;
    }

    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

}

