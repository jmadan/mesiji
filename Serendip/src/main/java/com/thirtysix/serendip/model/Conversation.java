package com.thirtysix.serendip.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Conversation implements Parcelable{

    public String id;
    public String title;
    public String creator;
    public Boolean isApproved;
    public Date createdOn;
    public String[] circles;
    public User user;

    public Conversation(String Id, String Title, Boolean Isapproved, String[] circles, User user){
        this.id = Id;
        this.title = Title;
        this.isApproved = Isapproved;
        this.circles = circles;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String[] getCircles() {
        return circles;
    }

    public void setCircles(String[] circles) {
        this.circles = circles;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(creator);
        parcel.writeParcelable(user, i);
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
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {

        // The rest is the same as in ObjectA
        id = in.readString();
        title = in.readString();
        creator = in.readString();

        // readParcelable needs the ClassLoader
        // but that can be picked up from the class
        // This will solve the BadParcelableException
        // because of ClassNotFoundException
        user = in.readParcelable(User.class.getClassLoader());

    }

}
