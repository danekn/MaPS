package com.example.android.handyshop;

public class User {
    private String userId;
    private String name;
    private String email;
    private double feedback;
    private int transanctions;


    public User(String userId, String name,String email, double feedback, int transanctions){
        this.userId=userId;
        this.name=name;
        this.email=email;
        this.feedback=feedback;
        this.transanctions=transanctions;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {}

    public double getFeedback() {
        return feedback;
    }

    public void setFeedback(double feedback) {
        this.feedback = feedback;
    }

    public int getTransanctions() {
        return transanctions;
    }

    public void setTransanctions(int transanctions) {
        this.transanctions = transanctions;
    }
}
