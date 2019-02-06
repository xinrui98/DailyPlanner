package com.example.xinruigao.dailyplanner;

public class Upload {
    private String mTitle;
    private String mDescription;
    private String mImageUrl;

    public Upload() {
        //empty constructor
    }

    public Upload(String title, String description, String imageUrl) {
        if (title.trim().equals("")) {
            title = "No Title";
        }if (description.trim().equals("")) {
            description = "No Title";
        }
        mTitle = title;
        mDescription = description;
        mImageUrl = imageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}
