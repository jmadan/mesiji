package com.thirtysix.serendip.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Conversation implements Parcelable{

    public String Id;
    public String Title;
    public String Creator;
    public Boolean isApproved;
    public Date CreatedOn;
    public String[] Circles;

    private int mData;

    public Conversation(String Id, String Title){
        this.Id = Id;
        this.Title = Title;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public Date getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(Date createdOn) {
        CreatedOn = createdOn;
    }

    public String[] getCircles() {
        return Circles;
    }

    public void setCircles(String[] circles) {
        Circles = circles;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(i);
    }

    public static final Parcelable.Creator<Conversation> CREATOR
            = new Parcelable.Creator<Conversation>() {
        public Conversation createFromParcel(Parcel in) {
            return new Conversation(in);
        }

        public Conversation[] newArray(int size) {
            return new Conversation[size];
        }
    };

    private Conversation(Parcel in) {
        mData = in.readInt();
    }

}
