package com.Guruprasad.innovativesolutions.Model;

public class RegisterModel {
    private String UserId,FullName , PhoneNo,Email,Password,Address , Profile_pic;

    public RegisterModel(String userId, String fullName, String phoneNo, String email, String password, String address, String profile_pic) {
        UserId = userId;
        FullName = fullName;
        PhoneNo = phoneNo;
        Email = email;
        Password = password;
        Address = address;
        Profile_pic = profile_pic;
    }

    public RegisterModel(String userId, String fullName, String phoneNo, String email, String password, String address) {
        UserId = userId;
        FullName = fullName;
        PhoneNo = phoneNo;
        Email = email;
        Password = password;
        Address = address;
    }

    public RegisterModel() {
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getProfile_pic() {
        return Profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        Profile_pic = profile_pic;
    }
}
