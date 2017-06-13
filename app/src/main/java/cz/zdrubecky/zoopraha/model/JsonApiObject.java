package cz.zdrubecky.zoopraha.model;

import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonApiObject {
    // Artificially added HTTP status code, not present in the original response body
    private int mStatus;
    @SerializedName("meta")
    @Expose
    private Meta mMeta;
    @SerializedName("data")
    @Expose
    private List<Resource> mData = null;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public Meta getMeta() {
        return mMeta;
    }

    public void setMeta(Meta meta) {
        mMeta = meta;
    }

    public List<Resource> getData() {
        return mData;
    }

    public void setData(List<Resource> data) {
        mData = data;
    }

    // Check if the response was processed some time in the past
    public boolean wasProcessed() {
        return mStatus == 304;
    }

    public class Meta {
        @SerializedName("count")
        @Expose
        private int mCount;

        public int getCount() {
            return mCount;
        }

        public void setCount(int count) {
            mCount = count;
        }
    }

    public class Resource {
        @SerializedName("type")
        @Expose
        private String mType;
        @SerializedName("id")
        @Expose
        private String mId;
        @SerializedName("attributes")
        @Expose
        private JsonObject mDocument;

        public String getType() {
            return mType;
        }

        public void setType(String type) {
            mType = type;
        }

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }

        public JsonObject getDocument() {
            return mDocument;
        }

        public void setDocument(JsonObject document) {
            mDocument = document;
        }
    }
}