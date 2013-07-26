package com.thirtysix.serendip.model;

public class Contact {

    String phone;
    String formattedPhone;

    public Contact(String phone, String formattedPhone){
        this.phone = phone;
        this.formattedPhone = formattedPhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFormattedPhone() {
        return formattedPhone;
    }

    public void setFormattedPhone(String formattedPhone) {
        this.formattedPhone = formattedPhone;
    }
}
