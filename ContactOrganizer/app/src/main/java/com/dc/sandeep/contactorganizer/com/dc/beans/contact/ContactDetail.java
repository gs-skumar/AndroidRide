package com.dc.sandeep.contactorganizer.com.dc.beans.contact;

import android.net.Uri;

/**
 * Created by SandeepK on 24-09-2015.
 */
public class ContactDetail {


    private String name;
    private String phoneNo;
    private String email;
    private String address;
    private Uri imageuri;
    private int id;

    public ContactDetail(){}
    public ContactDetail(String name, String phone, String email, String address,Uri imageuri){
        this.name = name;
        this.phoneNo = phone;
        this.email = email;
        this.address = address;
        this.imageuri = imageuri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Uri getImageuri() {
        return imageuri;
    }

    public void setImageuri(Uri imageuri) {
        this.imageuri = imageuri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
