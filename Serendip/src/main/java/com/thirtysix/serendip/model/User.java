package com.thirtysix.serendip.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public String email;
    public String handle;
    public String userId;
    public Integer uid;

    public User(String email, String handle, String userId, Integer uid) {
        this.email = email;
        this.handle = handle;
        this.userId = userId;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(handle);
        parcel.writeInt(uid);
        parcel.writeString(userId);
    }

    public static final Creator<User> CREATOR
            = new Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        email = in.readString();
        handle = in.readString();
        uid = in.readInt();
        userId = in.readString();
    }

}
