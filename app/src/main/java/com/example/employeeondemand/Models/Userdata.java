package com.example.employeeondemand.Models;

public class Userdata {
    String uId, cnicNo, username, lName, eMail, password, gender,dateOfBirthString, phoneNo,province, city, address, profession, skillDetails, profilePic, CnicUri, rating, ratePerDay, earned, token;
    int badReview;
    public Userdata() {

    }

    public Userdata(String uId, String cnicNo, String username, String lName, String eMail, String password, String gender, String dateOfBirthString, String phoneNo, String province, String city, String address, String profession, String skillDetails, String profilePic, String cnicUri, String rating, String ratePerDay, String earned, int badReview, String token) {
        this.uId = uId;
        this.cnicNo = cnicNo;
        this.username = username;
        this.lName = lName;
        this.eMail = eMail;
        this.password = password;
        this.gender = gender;
        this.dateOfBirthString = dateOfBirthString;
        this.phoneNo = phoneNo;
        this.province = province;
        this.city = city;
        this.address = address;
        this.profession = profession;
        this.skillDetails = skillDetails;
        this.profilePic = profilePic;
        CnicUri = cnicUri;
        this.rating = rating;
        this.ratePerDay = ratePerDay;
        this.earned = earned;
        this.badReview = badReview;
        this.token = token;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getCnicNo() {
        return cnicNo;
    }

    public void setCnicNo(String cnicNo) {
        this.cnicNo = cnicNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirthString() {
        return dateOfBirthString;
    }

    public void setDateOfBirthString(String dateOfBirthString) {
        this.dateOfBirthString = dateOfBirthString;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getSkillDetails() {
        return skillDetails;
    }

    public void setSkillDetails(String skillDetails) {
        this.skillDetails = skillDetails;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getCnicUri() {
        return CnicUri;
    }

    public void setCnicUri(String cnicUri) {
        CnicUri = cnicUri;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(String ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public String getEarned() {
        return earned;
    }

    public void setEarned(String earned) {
        this.earned = earned;
    }

    public int getBadReview() {
        return badReview;
    }

    public void setBadReview(int badReview) {
        this.badReview = badReview;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
