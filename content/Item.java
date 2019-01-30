package com.example.unamed.mvc.content;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.unamed.mvc.error.NullChecker;

/**
 * This is a general item in the recyclerview representing a single tv serie.
 */

@SuppressWarnings("unused")
@Entity
public class Item {
    //It's required from RoomDB :(
    public Item(@NonNull String mId, String mNextDate, String mOriginalName, String
            mFirstAirDate, String mLanguage, String mOverview, String mPosterPath, String
            mBackdropPath, Integer mLike, Double mRating, boolean mIsTemp, int mItemPos) {
        this.mId = mId;
        this.mNextDate = mNextDate;
        this.mOriginalName = mOriginalName;
        this.mFirstAirDate = mFirstAirDate;
        this.mLanguage = mLanguage;
        this.mOverview = mOverview;
        this.mPosterPath = mPosterPath;
        this.mBackdropPath = mBackdropPath;
        this.mLike = mLike;
        this.mRating = mRating;
        this.mIsTemp = mIsTemp;
        this.mItemPos = mItemPos;
    }

    @PrimaryKey
    @NonNull
    private String mId = "";

    @ColumnInfo(name = "mNextDate")
    private String mNextDate;

    @ColumnInfo(name = "mOriginalName")
    private String mOriginalName;

    @ColumnInfo(name = "mFirstAirDate")
    private String mFirstAirDate;

    @ColumnInfo(name = "mLanguage")
    private String mLanguage;

    @ColumnInfo(name = "mOverview")
    private String mOverview;

    @ColumnInfo(name = "mPosterPath")
    private String mPosterPath;

    @ColumnInfo(name = "mBackdropPath")
    private String mBackdropPath;

    @ColumnInfo(name = "mLike")
    private Integer mLike;

    @ColumnInfo(name = "mRating")
    private Double mRating;

    @ColumnInfo(name = "mIsTemp")
    private boolean mIsTemp;

    @ColumnInfo(name = "mItemPos")
    private int mItemPos;

    private Item(ItemBuilder builder) {
        boolean notNull = NullChecker.isNotNullB(builder.mFirstAirDate, builder.mId, builder
                .mLanguage, builder.mLike, builder.mOriginalName, builder.mOverview, builder
                .mPosterPath, builder.mBackdropPath, builder.mRating);
        if (builder.mItemPos >= 0 && notNull) {
            mId = builder.mId;
            mOriginalName = builder.mOriginalName;
            mFirstAirDate = builder.mFirstAirDate;
            mLanguage = builder.mLanguage;
            mOverview = builder.mOverview;
            mPosterPath = builder.mPosterPath;
            mBackdropPath = builder.mBackdropPath;
            mItemPos = builder.mItemPos;
            mLike = builder.mLike;
            mRating = builder.mRating;
            mIsTemp = builder.mIsTemp;
        }
    }

    /**
     * Getters.
     */

    public String getMNextDate() {
        if (mNextDate == null || mNextDate.equals("null")) return "Unavailable";
        else return mNextDate;
    }

    public String getMId() {
        return mId;
    }

    public String getMOriginalName() {
        return mOriginalName;
    }

    public String getMOverview() {
        return mOverview;
    }

    public String getMPosterPath() {
        return mPosterPath;
    }

    public String getMBackdropPath() {
        return mBackdropPath;
    }

    public String getMFirstAirDate() {
        return mFirstAirDate;
    }

    public String getMLanguage() {
        return mLanguage;
    }

    public Integer getMLike() {
        return mLike;
    }

    public Double getMRating() {
        return mRating;
    }

    public int getMItemPos() {
        return mItemPos;
    }

    public boolean getMIsTemp() { return mIsTemp; }

    /**
     * Setters.
     */

    public void setMNextDate(String mNextDate) {
        if (mNextDate == null || mNextDate.equals("null"))
            this.mNextDate = "Unavailable";
        else
        this.mNextDate = mNextDate; }

    public void setmId(@NonNull String mId) { this.mId = mId; }

    public void setmOriginalName(String mOriginalName) { this.mOriginalName = mOriginalName; }

    public void setmFirstAirDate(String mFirstAirDate) { this.mFirstAirDate = mFirstAirDate; }

    public void setmLanguage(String mLanguage) { this.mLanguage = mLanguage; }

    public void setmOverview(String mOverview) { this.mOverview = mOverview; }

    public void setmPosterPath(String mPosterPath) { this.mPosterPath = mPosterPath; }

    public void setmBackdropPath(String mBackdropPath) { this.mBackdropPath = mBackdropPath; }

    public void setmLike(Integer mLike) { this.mLike = mLike; }

    public void setmRating(Double mRating) { this.mRating = mRating; }

    public void setmIsTemp(boolean mIsTemp) { this.mIsTemp = mIsTemp; }

    public void setmItemPos(int mItemPos) { this.mItemPos = mItemPos; }

    @Override
    public String toString() {
        return "Item{" + "mId='" + mId + '\'' + ", mOriginalName='" + mOriginalName + '\'' + ", "
                + "mFirstAirDate='" + mFirstAirDate + '\'' + ", mLanguage='" + mLanguage + '\'' +
                ", mPosterPath='" + mPosterPath + '\'' + ", " + "mBackdropPath='" + mBackdropPath
                + '\'' + ", mLike=" + mLike + ", mRating=" + mRating + ", mItemPos=" + mItemPos +
                '}';
    }

    //Singleton inner class to build items.
    public static class ItemBuilder {

        //Eager initialization of singleton sBuilder.
        private static volatile ItemBuilder sBuilder = new ItemBuilder();

        //Base url for download images.
        private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";
        private static final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w780";
        private static final String UNAVAILABLE = "Unavailable";

        private String mOriginalName = UNAVAILABLE;
        private String mFirstAirDate;
        private String mLanguage;
        private Integer mLike;
        private Double mRating;
        private String mId;
        private String mOverview = UNAVAILABLE;
        private String mPosterPath;
        private String mBackdropPath;
        private boolean mIsTemp;
        private int mItemPos;


        /**
         * Setters for builder.
         */
        public ItemBuilder setOriginalName(String originalName) {
            if (originalName != null) {
                mOriginalName = originalName;
            } else {
                mOriginalName = UNAVAILABLE;
            }
            return this;
        }


        public ItemBuilder setFirstAirDate(String firstAirDate) {
            if (firstAirDate != null) {
                mFirstAirDate = firstAirDate;
            } else {
                mFirstAirDate = UNAVAILABLE;
            }
            return this;
        }

        public ItemBuilder setLanguage(String language) {
            if (language != null) {
                mLanguage = language;
            } else {
                mLanguage = UNAVAILABLE;
            }
            return this;
        }

        public ItemBuilder setLike(Integer like) {
            if (like != null) {
                mLike = like;
            } else {
                mLike = 0;
            }
            return this;
        }


        public ItemBuilder setRating(Double rating) {
            if (rating != null) {
                mRating = rating / 2;
            } else {
                mRating = 0.0;
            }
            return this;
        }

        public ItemBuilder setId(String id) {
            if (id != null) {
                mId = id;
            } else {
                mId = UNAVAILABLE;
            }
            return this;
        }

        public ItemBuilder setOverview(String overview) {
            if (overview != null) {
                mOverview = overview;
            } else {
                mOverview = UNAVAILABLE;
            }
            return this;
        }

        public ItemBuilder setPosterPath(String posterPath, boolean flag) {
            if (flag) {
                mPosterPath = POSTER_BASE_URL + posterPath;
            }
            else {
                mPosterPath = posterPath;
            }
            return this;
        }

        public ItemBuilder setTemp(boolean temp) {
            mIsTemp = temp;
            return this;
        }

        public ItemBuilder setBackdropPath(String backdropPath, boolean flag) {
            if (flag) {
                mBackdropPath = BACKDROP_BASE_URL + backdropPath;
            } else {
                mBackdropPath = backdropPath;
            }
            return this;
        }

        public ItemBuilder setItemPos(int itemPos) {
            if (itemPos >= 0) {
                mItemPos = itemPos;
                return this;
            }
            //Abnormal situation.
            throw new IllegalStateException("Negative item position.");
        }

        //Return the singleton instance, shared only between UI threads.
        public static ItemBuilder getInstance() {
            return sBuilder;
        }

        public Item build() {
            return new Item(this);
        }
    }
}

