package cz.zdrubecky.zoopraha.model;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class JsonApiObject {
    @SerializedName("meta")
    @Expose
    private Meta mMeta;
    @SerializedName("data")
    @Expose
    private List<Resource> mData = null;

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
        private JsonElement mDocument;

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

        public JsonElement getDocument() {
            return mDocument;
        }

        public void setDocument(JsonElement document) {
            mDocument = document;
        }
    }
}