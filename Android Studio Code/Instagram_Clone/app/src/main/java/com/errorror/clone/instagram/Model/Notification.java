package com.errorror.clone.instagram.Model;

public class Notification {

    private String userid;
    private String text;
    private String postid;
    private boolean ispost;

    public Notification() {
    }

    public Notification(String userid, String text, String postid, boolean ispost) {
        this.userid = userid;
        this.text = text;
        this.postid = postid;
        this.ispost = ispost;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public void setIspost(boolean ispost) {
        this.ispost = ispost;
    }

    public String getUserid() {
        return userid;
    }

    public String getText() {
        return text;
    }

    public String getPostid() {
        return postid;
    }

    public boolean isIspost() {
        return ispost;
    }
}
