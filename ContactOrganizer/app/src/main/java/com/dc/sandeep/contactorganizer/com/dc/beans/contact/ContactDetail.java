package com.dc.sandeep.contactorganizer.com.dc.beans.contact;

/**
 * Created by Gs on 24-09-2015.
 */
public class ContactDetail {


    private String name;
    private String phoneNo;
    private String email;
    private String address;

    public ContactDetail(String name, String phone, String email, String address){
        this.name = name;
        this.phoneNo = phone;
        this.email = email;
        this.address = address;
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
}
