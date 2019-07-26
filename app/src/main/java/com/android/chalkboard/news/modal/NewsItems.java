package com.android.chalkboard.news.modal;

public class NewsItems {
    private String itemName;
    private int image;

    public NewsItems(String itemName, int image) {
        this.itemName = itemName;
        this.image = image;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
