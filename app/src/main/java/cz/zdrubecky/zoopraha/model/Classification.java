package cz.zdrubecky.zoopraha.model;

import com.google.gson.annotations.SerializedName;

public class Classification {
    @SerializedName("id")
    private String mId;
    @SerializedName("type")
    private String mType;
    @SerializedName("title.name")
    private String mName;
    @SerializedName("title.latin_name")
    private String mLatinName;
    @SerializedName("slug")
    private String mSlug;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLatinName() {
        return mLatinName;
    }

    public void setLatinName(String latinName) {
        mLatinName = latinName;
    }

    public String getSlug() {
        return mSlug;
    }

    public void setSlug(String slug) {
        mSlug = slug;
    }
}
