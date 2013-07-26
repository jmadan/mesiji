package com.thirtysix.serendip.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationDetails implements Parcelable{

    String address;
    Double lat;
    Double lng;
    String postalCode;
    String city;
    String state;
    String country;
    String cc;

    private int mData;

    public LocationDetails(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
    }

    public static final Creator<LocationDetails> CREATOR
            = new Creator<LocationDetails>() {
        public LocationDetails createFromParcel(Parcel in) {
            return new LocationDetails(in);
        }

        public LocationDetails[] newArray(int size) {
            return new LocationDetails[size];
        }
    };

    private LocationDetails(Parcel in) {
//        mData = in.readInt();
        lat = in.readDouble();
        lng = in.readDouble();
    }
}
