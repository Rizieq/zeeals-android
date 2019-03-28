package com.example.user.zeeals;

import android.os.Parcel;
import android.os.Parcelable;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.io.Serializable;
import java.util.List;

public class Group extends ExpandableGroup<Source> {

    String nama;
    public Group(String title, List<Source> items) {
        super(title, items);
        nama=title;
    }

    public String getName(){
        return nama;
    }

    public void setName(String nama){
        this.nama=nama;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

    }
}
