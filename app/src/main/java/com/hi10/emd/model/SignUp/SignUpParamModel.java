package com.hi10.emd.model.SignUp;

public class SignUpParamModel {

    private String fullName;
    private String contactNumber;
    private String age;
    private String userName;
    private String weight;
    private String bloodGroup;
    private String allergy;
    private String allergyDetails;
    private String breathing;
    private String gender;
    private String type;
    private String r_mno;
    private String latlon;


    public SignUpParamModel(String fullName, String contactNumber, String age, String userName, String weight, String bloodGroup, String allergy, String allergyDetails, String breathing, String gender, String type, String r_mno, String latlon) {
        this.fullName = fullName;
        this.contactNumber = contactNumber;
        this.age = age;
        this.userName = userName;
        this.weight = weight;
        this.bloodGroup = bloodGroup;
        this.allergy = allergy;
        this.allergyDetails = allergyDetails;
        this.breathing = breathing;
        this.gender = gender;
        this.type = type;
        this.r_mno = r_mno;
        this.latlon = latlon;
    }

    public String getLatlon() {
        return latlon;
    }

    public void setLatlon(String latlon) {
        this.latlon = latlon;
    }

    public String getR_mno() {
        return r_mno;
    }

    public void setR_mno(String r_mno) {
        this.r_mno = r_mno;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
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


}
