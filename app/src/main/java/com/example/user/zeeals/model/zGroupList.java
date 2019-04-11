package com.example.user.zeeals.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class zGroupList {

    @SerializedName("group_list")
    private ArrayList<zGroup> groupList;

    public ArrayList<zGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<zGroup> groupList) {
        this.groupList = groupList;
    }
}
