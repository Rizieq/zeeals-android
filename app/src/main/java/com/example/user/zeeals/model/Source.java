package com.example.user.zeeals.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Source implements Parcelable {
    private String sourceName;
    private String sourceLink;
    private String groupName;
    private int groupPosition;

    public Source(String sourceName, String sourceLink,String groupName,int groupPosition) {
        this.sourceName = sourceName;
        this.sourceLink = sourceLink;
        this.groupName = groupName;
        this.groupPosition = groupPosition;
    }

    public Source(String sourceName, String sourceLink) {
        this.sourceName = sourceName;
        this.sourceLink = sourceLink;
    }

    public Source(String sourceName, String sourceLink, String groupName) {
        this.sourceName = sourceName;
        this.sourceLink = sourceLink;
        this.groupName = groupName;
    }

    protected Source(Parcel in) {
        sourceName=in.readString();
        sourceLink=in.readString();
        groupName=in.readString();
    }

    public String getGroupName() {
        return groupName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public int getGroupPosition() {
        return groupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public static final Creator<Source> CREATOR = new Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel in) {
            return new Source(in);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sourceName);
        dest.writeString(sourceLink);
        dest.writeString(groupName);
    }
}
