package cz.zdrubecky.zoopraha.model;

import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("_id")
    private String mId;
    @SerializedName("start")
    private String mStart;
    @SerializedName("end")
    private String mEnd;
    @SerializedName("duration")
    private int mDuration;
    @SerializedName("name")
    private String mName;
    @SerializedName("description")
    private String mDescription;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getStart() {
        return mStart;
    }

    public void setStart(String start) {
        mStart = start;
    }

    public String getEnd() {
        return mEnd;
    }

    public void setEnd(String end) {
        mEnd = end;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
