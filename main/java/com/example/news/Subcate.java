package com.example.news;

public class Subcate {
    String url;
    String name;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    boolean isSelected;

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public Subcate(String url, String name, Boolean isSelected) {
        this.url = url;
        this.name = name;
        this.isSelected = isSelected;
    }
}
