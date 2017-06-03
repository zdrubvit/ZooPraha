package cz.zdrubecky.zoopraha.model;

import com.google.gson.annotations.SerializedName;

public class Adoption {
    @SerializedName("_id")
    private String mId;
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
