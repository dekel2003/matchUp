package com.models;

/**
 * Created by User on 06/12/2015.
 */
public class NavIteam {
    private String title;
    private String subTitle;
    private int resIcon;

    public NavIteam(String title, String subTitle, int resIcon) {
        this.title = title;
        this.subTitle = subTitle;
        this.resIcon = resIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }
}
