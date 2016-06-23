package com.example.android.handyshop;

public class Offer {
    private String userId;
    private String title;
    private String category;
    private String subCategory;
    private String description;
    private double latitude;
    private double longitude;

    public Offer(String userId, String title, String category, String subCategory, String description, double latitude, double longitude){
        this.userId=userId;
        this.title=title;
        this.category=category;
        this.subCategory=subCategory;
        this.description=description;
        this.latitude=latitude;
        this.longitude=longitude;

}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription () {
        return description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLongitude(){
        return longitude;
    }

    public double getLatitude(){
        return latitude;
    }


    public Offer() {}
}

