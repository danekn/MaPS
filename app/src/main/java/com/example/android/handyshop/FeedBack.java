package com.example.android.handyshop;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class FeedBack {
    private String yourId;
    private String userId;
    private long timeCreation;
    private String titleRequest;
    private String category;
    private String subCategory;
    private String description;

    public String getTitleRequest() {
        return titleRequest;
    }

    public void setTitleRequest(String titleRequest) {
        this.titleRequest = titleRequest;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getYourId() {
        return yourId;
    }

    public void setYourId(String yourId) {
        this.yourId = yourId;
    }

    public long getTimeCreation() {
        return timeCreation;
    }

    public void setTimeCreation(long timeCreation) {
        this.timeCreation = timeCreation;
    }

    public FeedBack(String yourId, String userId, long timeCreation,
                    String titleRequest, String category, String subCategory, String description) {
        this.yourId = yourId;
        this.userId = userId;
        this.timeCreation = timeCreation;
        this.titleRequest = titleRequest;
        this.category = category;
        this.subCategory = subCategory;
        this.description = description;
    }

    public FeedBack(){}
}

