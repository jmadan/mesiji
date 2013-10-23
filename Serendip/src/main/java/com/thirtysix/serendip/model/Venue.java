package com.thirtysix.serendip.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Venue implements Serializable{
    public String id; //this is foursquare ID for locationDetails
    public String name;
    public String streetName;
    public Contact contact;
    public LocationDetails locationDetails;

//    private int mData;

    public LocationDetails getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(LocationDetails locationDetails) {
        this.locationDetails = locationDetails;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Venue(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Venue(String id, String name, String streetName, LocationDetails locationDetails) {
        this.id = id;
        this.name = name;
        this.streetName = streetName;
//        this.contact = contact;
        this.locationDetails = locationDetails;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(id);
//        parcel.writeString(name);
//        parcel.writeString(streetName);
//        parcel.writeParcelable(locationDetails, i);
//    }
//
//    public static final Creator<Venue> CREATOR
//            = new Creator<Venue>() {
//        public Venue createFromParcel(Parcel in) {
//            return new Venue(in);
//        }
//
//        public Venue[] newArray(int size) {
//            return new Venue[size];
//        }
//    };
//
//    private Venue(Parcel in) {
//        readFromParcel(in);
//    }
//
//    private void readFromParcel(Parcel in) {
//
//        // The rest is the same as in ObjectA
//        id = in.readString();
//        name = in.readString();
//        streetName = in.readString();
//
//        // readParcelable needs the ClassLoader
//        // but that can be picked up from the class
//        // This will solve the BadParcelableException
//        // because of ClassNotFoundException
//        locationDetails = in.readParcelable(LocationDetails.class.getClassLoader());

//    }
}
