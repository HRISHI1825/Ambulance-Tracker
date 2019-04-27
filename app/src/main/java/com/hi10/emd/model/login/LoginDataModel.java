package com.hi10.emd.model.login;

import com.google.gson.annotations.SerializedName;

public class LoginDataModel {


    @SerializedName("id")
    private String id;

    @SerializedName("fullname")
    private String fullName;

    @SerializedName("mno")
    private String contactNumber;

    @SerializedName("age")
    private String age;

    @SerializedName("username")
    private String userName;

    @SerializedName("u_weight")
    private String weight;

    @SerializedName("allergy")
    private String allergy;

    @SerializedName("bgroup")
    private String bloodGroup;

    @SerializedName("a_details")
    private String allergyDetails;

    @SerializedName("b_prob")
    private String breathing;

    @SerializedName("gender")
    private String gender;

    @SerializedName("u_type")
    private String type;

    @SerializedName("r_mno")
    private String r_mno;

    @SerializedName("latlon")
    private String latlon;


    public String getLatlon() {
        return latlon;
    }

    public void setLatlon(String latlon) {
        this.latlon = latlon;
    }



    public LoginDataModel(String fullName, String contactNumber, String age, String userName, String weight, String allergy, String bloodGroup, String allergyDetails, String breathing, String gender, String type, String r_mno) {
        this.fullName = fullName;
        this.contactNumber = contactNumber;
        this.age = age;
        this.userName = userName;
        this.weight = weight;
        this.allergy = allergy;
        this.bloodGroup = bloodGroup;
        this.allergyDetails = allergyDetails;
        this.breathing = breathing;
        this.gender = gender;
        this.type = type;
        this.r_mno = r_mno;
    }

    public LoginDataModel(String id, String fullName, String contactNumber, String age, String userName, String weight, String allergy, String bloodGroup, String allergyDetails, String breathing, String gender, String type, String r_mno) {
        this.id = id;
        this.fullName = fullName;
        this.contactNumber = contactNumber;
        this.age = age;
        this.userName = userName;
        this.weight = weight;
        this.allergy = allergy;
        this.bloodGroup = bloodGroup;
        this.allergyDetails = allergyDetails;
        this.breathing = breathing;
        this.gender = gender;
        this.type = type;
        this.r_mno = r_mno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAllergyDetails() {
        return allergyDetails;
    }

    public void setAllergyDetails(String allergyDetails) {
        this.allergyDetails = allergyDetails;
    }

    public String getBreathing() {
        return breathing;
    }

    public void setBreathing(String breathing) {
        this.breathing = breathing;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getR_mno() {
        return r_mno;
    }

    public void setR_mno(String r_mno) {
        this.r_mno = r_mno;
    }
}
