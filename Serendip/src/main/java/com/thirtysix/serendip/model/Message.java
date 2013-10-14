package com.thirtysix.serendip.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Message implements Parcelable {

    public String id;
    public String message;
    public String userId;
    public Date createdOn;

    public Message(String id, String msg, String userId, Date createdOn){
        this.id = id;
        this.message = msg;
        this.userId = userId;
        this.createdOn = createdOn;
    }

    public Message(){}

    public String getId() {
        return id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeString(userId);
        parcel.writeString(id);
        parcel.writeString(createdOn.toString());
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    private Message(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {

        // The rest is the same as in ObjectA
        id = parcel.readString();
        message = parcel.readString();
        userId = parcel.readString();
    }
}
