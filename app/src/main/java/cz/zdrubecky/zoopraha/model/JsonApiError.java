package cz.zdrubecky.zoopraha.model;

import com.google.gson.annotations.SerializedName;

public class JsonApiError {
    @SerializedName("status")
    private int mStatus;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("detail")
    private String mDetail;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }

    public String toString() {
        return "status " + mStatus + ", \"" + mTitle + "\", detail = " + mDetail;
    }
}
