
package com.android.chalkboard.dashboard.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardItem {

    @SerializedName("role")
    @Expose
    public String role;
    @SerializedName("tile1")
    @Expose
    public String tile1;
    @SerializedName("title2")
    @Expose
    public String title2;
    @SerializedName("button_text")
    @Expose
    public String buttonText;
    @SerializedName("items")
    @Expose
    public List<Item> items = null;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTile1() {
        return tile1;
    }

    public void setTile1(String tile1) {
        this.tile1 = tile1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
