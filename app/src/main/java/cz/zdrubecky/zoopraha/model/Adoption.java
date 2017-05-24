package cz.zdrubecky.zoopraha.model;

import com.google.gson.annotations.SerializedName;

public class Adoption {
    @SerializedName("id")
    private String mId;
    @SerializedName("opendata_id")
    private int mOpendataId;
    @SerializedName("lexicon_id")
    private String mLexiconId;
    @SerializedName("name")
    private String mName;
    @SerializedName("price")
    private int mPrice;
    @SerializedName("visit")
    private boolean mVisit;

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

    public String getLexiconId() {
        return mLexiconId;
    }

    public void setLexiconId(String lexiconId) {
        mLexiconId = lexiconId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public boolean isVisit() {
        return mVisit;
    }

    public void setVisit(boolean visit) {
        mVisit = visit;
    }
}
