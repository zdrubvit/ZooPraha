package cz.zdrubecky.zoopraha.model;

import com.google.gson.annotations.SerializedName;

public class Animal {
    @SerializedName("_id")
    private String mId;
    @SerializedName("opendata_id")
    private int mOpendataId;
    @SerializedName("name")
    private String mName;
    @SerializedName("latin_name")
    private String mLatinName;
    @SerializedName("class.name")
    private String mClassName;
    @SerializedName("class.latin_name")
    private String mClassLatinName;
    @SerializedName("order.name")
    private String mOrderName;
    @SerializedName("order.latin_name")
    private String mOrderLatinName;
    @SerializedName("desription")
    private String mDescription;
    @SerializedName("image")
    private String mImage;
    @SerializedName("continents")
    private String mContinents;
    @SerializedName("distribution")
    private String mDistribution;
    @SerializedName("biotope")
    private String mBiotope;
    @SerializedName("biotopes_detail")
    private String mBiotopesDetail;
    @SerializedName("food")
    private String mFood;
    @SerializedName("food_detail")
    private String mFoodDetail;
    @SerializedName("proportions")
    private String mProportions;
    @SerializedName("reproduction")
    private String mReproduction;
    @SerializedName("attractions")
    private String mAttractions;
    @SerializedName("projects")
    private String mProjects;
    @SerializedName("breeding")
    private String mBreeding;
    @SerializedName("location")
    private String mLocation;
    @SerializedName("location_url")
    private String mLocationUrl;

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

    public String getClassName() {
        return mClassName;
    }

    public void setClassName(String aClassName) {
        mClassName = aClassName;
    }

    public String getClassLatinName() {
        return mClassLatinName;
    }

    public void setClassLatinName(String classLatinName) {
        mClassLatinName = classLatinName;
    }

    public String getOrderName() {
        return mOrderName;
    }

    public void setOrderName(String orderName) {
        mOrderName = orderName;
    }

    public String getOrderLatinName() {
        return mOrderLatinName;
    }

    public void setOrderLatinName(String orderLatinName) {
        mOrderLatinName = orderLatinName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getContinents() {
        return mContinents;
    }

    public void setContinents(String continents) {
        mContinents = continents;
    }

    public String getDistribution() {
        return mDistribution;
    }

    public void setDistribution(String distribution) {
        mDistribution = distribution;
    }

    public String getBiotope() {
        return mBiotope;
    }

    public void setBiotope(String biotope) {
        mBiotope = biotope;
    }

    public String getBiotopesDetail() {
        return mBiotopesDetail;
    }

    public void setBiotopesDetail(String biotopesDetail) {
        mBiotopesDetail = biotopesDetail;
    }

    public String getFood() {
        return mFood;
    }

    public void setFood(String food) {
        mFood = food;
    }

    public String getFoodDetail() {
        return mFoodDetail;
    }

    public void setFoodDetail(String foodDetail) {
        mFoodDetail = foodDetail;
    }

    public String getProportions() {
        return mProportions;
    }

    public void setProportions(String proportions) {
        mProportions = proportions;
    }

    public String getReproduction() {
        return mReproduction;
    }

    public void setReproduction(String reproduction) {
        mReproduction = reproduction;
    }

    public String getAttractions() {
        return mAttractions;
    }

    public void setAttractions(String attractions) {
        mAttractions = attractions;
    }

    public String getProjects() {
        return mProjects;
    }

    public void setProjects(String projects) {
        mProjects = projects;
    }

    public String getBreeding() {
        return mBreeding;
    }

    public void setBreeding(String breeding) {
        mBreeding = breeding;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getLocationUrl() {
        return mLocationUrl;
    }

    public void setLocationUrl(String locationUrl) {
        mLocationUrl = locationUrl;
    }
}
