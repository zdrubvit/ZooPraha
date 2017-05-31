package cz.zdrubecky.zoopraha.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Classification {
    @SerializedName("id")
    private String mId;
    @SerializedName("opendata_id")
    private int mOpendataId;
    @SerializedName("type")
    private String mType;
    @SerializedName("parent_id")
    private int mParentId;
    @SerializedName("title.name")
    private String mName;
    @SerializedName("title.latin_name")
    private String mLatinName;
    @SerializedName("slug")
    private String mSlug;
    @SerializedName("orders")
    private List<Classification> mOrders;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getOpendataId() {
        return mOpendataId;
    }

    public void setOpendataId(int opendataId) {
        mOpendataId = opendataId;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public int getParentId() {
        return mParentId;
    }

    public void setParentId(int parentId) {
        mParentId = parentId;
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

    public List<Classification> getOrders() {
        return mOrders;
    }

    public void setOrders(List<Classification> orders) {
        mOrders = orders;
    }
}
