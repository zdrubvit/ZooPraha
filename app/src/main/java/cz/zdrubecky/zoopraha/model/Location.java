package cz.zdrubecky.zoopraha.model;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("_id")
    private String mId;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("ordering")
    private int mOrdering;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("gps_x")
    private String mGpsX;
    @SerializedName("gps_y")
    private String mGpsY;
    @SerializedName("name")
    private String mName;
    @SerializedName("slug")
    private String mSlug;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getOrdering() {
        return mOrdering;
    }

    public void setOrdering(int ordering) {
        mOrdering = ordering;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getGpsX() {
        return mGpsX;
    }

    public void setGpsX(String gpsX) {
        mGpsX = gpsX;
    }

    public String getGpsY() {
        return mGpsY;
    }

    public void setGpsY(String gpsY) {
        mGpsY = gpsY;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSlug() {
        return mSlug;
    }

    public void setSlug(String slug) {
        mSlug = slug;
    }
}
