package com.example.giphy_app;

public class Gif {

    private String gifURL;
    private String title;

    public Gif(String gifURL, String title) {
        this.gifURL = gifURL;
        this.title = title;
    }

    public String getGifURL() {
        return gifURL;
    }

    public void setGifURL(String gifURL) {
        this.gifURL = gifURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
