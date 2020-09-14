package com.errorror.clone.instagram.Model;

public class Story {

    private String imageurl;
    private long timestart;
    private long timeend;
    private String storyid;
    private String userid;

    public Story() {
    }

    public Story(String imageurl, long timestart, long timeend, String storyid, String userid) {
        this.imageurl = imageurl;
        this.timestart = timestart;
        this.timeend = timeend;
        this.storyid = storyid;
        this.userid = userid;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public void setTimestart(long timestart) {
        this.timestart = timestart;
    }

    public void setTimeend(long timeend) {
        this.timeend = timeend;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setStoryid(String storyid) {
        this.storyid = storyid;
    }

    public String getImageurl() {
        return imageurl;
    }

    public long getTimestart() {
        return timestart;
    }

    public long getTimeend() {
        return timeend;
    }

    public String getUserid() {
        return userid;
    }

    public String getStoryid() {
        return storyid;
    }
}
