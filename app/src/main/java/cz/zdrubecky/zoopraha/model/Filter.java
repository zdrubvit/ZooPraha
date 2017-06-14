package cz.zdrubecky.zoopraha.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Filter {
    @Expose(serialize = false, deserialize = false)
    private String mName;
    @SerializedName("name")
    private String mValue;
    @SerializedName("count")
    private int mCount;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }
}
