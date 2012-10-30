package com.ascf.jwt.appstore.dirparser;

public class Item {

    private int mId = 0;
    private String mDisplayname = "";
    private String mHttpUrl = "";
    private boolean mIsEnable = false;

    public Item(int id, String dsplay, String url, boolean enable) {
        this.mId = id;
        this.mDisplayname = dsplay;
        this.mHttpUrl = url;
        this.mIsEnable = enable;
    }

    public Item() {

    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmDisplayname() {
        return mDisplayname;
    }

    public void setmDisplayname(String mDisplayname) {
        this.mDisplayname = mDisplayname;
    }

    public String getmHttpUrl() {
        return mHttpUrl;
    }

    public void setmHttpUrl(String mHttpUrl) {
        this.mHttpUrl = mHttpUrl;
    }

    public boolean ismIsEnable() {
        return mIsEnable;
    }

    public void setmIsEnable(boolean mIsEnable) {
        this.mIsEnable = mIsEnable;
    }

}
