package com.ascf.jwt.appstore.dirparser;

import java.util.ArrayList;
import java.util.List;

public class Directory {

    private String mName = "";
    private int level = 0;
    private List<Item> itemList = new ArrayList<Item>();

    public Directory (){
        
    }

    public Directory(String name, int lev) {
        this.mName = name;
        this.level = lev;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setElement(int index, Item item) {
        itemList.set(index, item);
    }

    public void setElements(List<Item> list) {
        this.itemList = list;
    }
}
