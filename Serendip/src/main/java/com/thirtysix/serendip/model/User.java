package com.thirtysix.serendip.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {

    public String _id;
    public Integer userId;
    public String name;
    public String email;
    public String handle;


    public User(String id, Integer userId, String name, String email, String handle) {
        this._id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.handle = handle;
    }

    public String get_id(){
        return _id;
    }

    public String getName(){
        return name;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeInt(userId);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(handle);
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
        _id = in.readString();
        userId = in.readInt();
        name = in.readString();
        email = in.readString();
        handle = in.readString();

    }


    public static User getUserFromJson(JSONObject userJson) {
        User user = null;
        try {
            user = new User(userJson.getString("_id"), userJson.getInt("userid"), userJson.getString("name"), userJson.getString("email"),userJson.getString("handle"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}
