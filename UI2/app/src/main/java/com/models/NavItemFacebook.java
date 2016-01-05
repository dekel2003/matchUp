package com.models;

/**
 * Created by User on 06/12/2015.
 */
public class NavItemFacebook {
    private String title;
    private String id;
    private String metcher;//only for gridvuew
    private String metcherid;//only for gridvuew
    private String rec;//only for gridvuew
    private String matchID;
    private String side;


    public NavItemFacebook(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public NavItemFacebook(String title, String id,String metcher, String metcherid,String rec,String matchID,String side) {
        this.title = title;
        this.id = id;
        this.metcher=metcher;
        this.metcherid=metcherid;
        this.rec=rec;
        this.matchID=matchID;
        this.side=side;
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

    public String getMetcher() {
        return metcher;
    }

    public void setMetcher(String metcher) {
        this.metcher = metcher;
    }

    public String getMetcherid() {
        return metcherid;
    }

    public void setMetcherid(String metcherid) {
        this.metcherid = metcherid;
    }


    public String getRec() {
        return rec;
    }

    public void setRec(String rec) {
        this.rec = rec;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }
}