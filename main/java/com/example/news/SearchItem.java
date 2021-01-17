package com.example.news;

public class SearchItem {
    String url;
    String title;
    String desc;

    public SearchItem(String url, String title, String desc) {
        this.url = url;
        this.title = title;
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }
}
