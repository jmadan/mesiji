package com.thirtysix.serendip.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thirtysix.serendip.Constants;
import com.thirtysix.serendip.MesijiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

public class User implements Serializable {

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

    public User(){};

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
