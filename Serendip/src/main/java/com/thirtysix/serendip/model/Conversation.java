package com.thirtysix.serendip.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Conversation implements Parcelable{

    public String id;
    public String title;
    public String creator;
    public boolean isApproved;
    public Date createdOn;
    public String[] circles;
    public User user;
    public List<Message> messages;

    public Conversation(String id, String title, Boolean isApproved, User user, String[] circles, List<Message> messages){
        super();
        this.id = id;
        this.title = title;
        this.isApproved = isApproved;
        this.circles = circles;
        this.user = user;
        this.messages = messages;
    }

    public Conversation(String id, String title, boolean isApproved, User user){
        super();
        this.id = id;
        this.title = title;
        this.isApproved = isApproved;
        this.user = user;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
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
        parcel.writeTypedList(messages);
    }

    public static final Parcelable.Creator<Conversation> CREATOR = new Parcelable.Creator<Conversation>() {
        public Conversation createFromParcel(Parcel parcel) {
            return new Conversation(parcel);
        }

        public Conversation[] newArray(int size) {
            return new Conversation[size];
        }
    };

    private Conversation(Parcel parcel) {
        id = parcel.readString();
        title = parcel.readString();
        creator = parcel.readString();
        messages = new ArrayList<Message>();
        parcel.readTypedList(messages, Message.CREATOR);
        user = parcel.readParcelable(User.class.getClassLoader());
    }

}
