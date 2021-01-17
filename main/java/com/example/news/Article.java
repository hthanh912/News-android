package com.example.news;

public class Article {
    String link;
    String title;
    String thumb;
    String desc;
    String time;
    String cate;

    public Article(String link, String title, String thumb, String desc, String time, String cate) {
        this.link = link;
        this.title = title;
        this.thumb = thumb;
        this.desc = desc;
        this.time = time;
        this.cate = cate;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getThumb() {
        return thumb;
    }

    public String getTime() {
        return time;
    }

    public String getCate() {
        return cate;
    }

}
