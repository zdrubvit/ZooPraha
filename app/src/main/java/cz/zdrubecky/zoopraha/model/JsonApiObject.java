package cz.zdrubecky.zoopraha.model;

import android.content.Context;

import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cz.zdrubecky.zoopraha.api.InternalStorageDriver;

public class JsonApiObject {
    // Artificially added HTTP status code, not present in the original response body
    @Expose(serialize = false, deserialize = false)
    private int mStatus;
    // The response's eTag header value to denote the resource's modified state
    @Expose(serialize = false, deserialize = false)
    private String mEtag;
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

    public String getEtag() {
        return mEtag;
    }

    public void setEtag(String etag) {
        mEtag = etag;
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

    // Check if the response was processed at some point in the past
    public boolean wasProcessed(Context context) {
        return InternalStorageDriver.wasResourceProcessed(context, mEtag);
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