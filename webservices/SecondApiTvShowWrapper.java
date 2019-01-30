package com.example.unamed.mvc.webservices;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SecondApiTvShowWrapper {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("permalink")
    @Expose
    private String permalink;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("description_source")
    @Expose
    private String descriptionSource;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private Object endDate;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("runtime")
    @Expose
    private Integer runtime;
    @SerializedName("network")
    @Expose
    private String network;
    @SerializedName("youtube_link")
    @Expose
    private String youtubeLink;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("image_thumbnail_path")
    @Expose
    private String imageThumbnailPath;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("rating_count")
    @Expose
    private String ratingCount;
    @SerializedName("countdown")
    @Expose
    private SecondApiCountdownWrapper countdown;
    @SerializedName("genres")
    @Expose
    private List<String> genres = null;
    @SerializedName("pictures")
    @Expose
    private List<String> pictures = null;
    @SerializedName("episodes")
    @Expose
    private List<SecondApiEpisodeWrapper> episodes = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionSource() {
        return descriptionSource;
    }

    public void setDescriptionSource(String descriptionSource) {
        this.descriptionSource = descriptionSource;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Object getEndDate() {
        return endDate;
    }

    public void setEndDate(Object endDate) {
        this.endDate = endDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageThumbnailPath() {
        return imageThumbnailPath;
    }

    public void setImageThumbnailPath(String imageThumbnailPath) {
        this.imageThumbnailPath = imageThumbnailPath;
    }

    @Override
    public String toString() {
        return "TvShow{" + "id=" + id + ", name='" + name + '\'' + ", permalink='" + permalink +
                '\'' + ", url='" + url + '\'' + ", description='" + description + '\'' + ", " +
                "descriptionSource='" + descriptionSource + '\'' + ", startDate='" + startDate +
                '\'' + ", endDate=" + endDate + ", country='" + country + '\'' + ", status='" +
                status + '\'' + ", runtime=" + runtime + ", network='" + network + '\'' + ", " +
                "youtubeLink='" + youtubeLink + '\'' + ", imagePath='" + imagePath + '\'' + ", " +
                "imageThumbnailPath='" + imageThumbnailPath + '\'' + ", rating='" + rating + '\''
                + ", ratingCount='" + ratingCount + '\'' + ", countdown=" + countdown + ", " +
                "genres=" + genres + ", pictures=" + pictures + ", episodes=" + episodes + '}';
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }

    public SecondApiCountdownWrapper getCountdown() {
        return countdown;
    }

    public void setCountdown(SecondApiCountdownWrapper countdown) {
        this.countdown = countdown;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public List<SecondApiEpisodeWrapper> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<SecondApiEpisodeWrapper> episodes) {
        this.episodes = episodes;
    }

}