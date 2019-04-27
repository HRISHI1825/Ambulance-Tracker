package com.hi10.emd.model.Hospitals;

import com.google.gson.annotations.SerializedName;

public class HospitalDataModel {

    @SerializedName("hospital_name")
    private String hospital_name;

    @SerializedName("contact")
    private String contact;

    @SerializedName("bed_count")
    private String bed_count;

    @SerializedName("doctor_available")
    private String doctor_available;

    @SerializedName("speciality")
    private String speciality;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getBed_count() {
        return bed_count;
    }

    public void setBed_count(String bed_count) {
        this.bed_count = bed_count;
    }

    public String getDoctor_available() {
        return doctor_available;
    }

    public void setDoctor_available(String doctor_available) {
        this.doctor_available = doctor_available;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public HospitalDataModel(String hospital_name, String contact, String bed_count, String doctor_available, String speciality, String latitude, String longitude) {
        this.hospital_name = hospital_name;
        this.contact = contact;
        this.bed_count = bed_count;
        this.doctor_available = doctor_available;
        this.speciality = speciality;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
