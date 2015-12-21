package com.models;

/**
 * Created by User on 06/12/2015.
 */
public class NavItemFacebook {
    private String title;
    private String id;

    public NavItemFacebook(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }
}
