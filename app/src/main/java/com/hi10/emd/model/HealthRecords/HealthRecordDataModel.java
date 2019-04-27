package com.hi10.emd.model.HealthRecords;

import com.google.gson.annotations.SerializedName;

public class HealthRecordDataModel {

    @SerializedName("d_name")
    private String d_name;

    @SerializedName("symtoms")
    private String symtoms;

    @SerializedName("doc_name")
    private String doc_name;

    @SerializedName("r_date")
    private String r_date;

    public String getD_name() {
        return d_name;
    }

    public void setD_name(String d_name) {
        this.d_name = d_name;
    }

    public String getSymtoms() {
        return symtoms;
    }

    public void setSymtoms(String symtoms) {
        this.symtoms = symtoms;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getR_date() {
        return r_date;
    }

    public void setR_date(String r_date) {
        this.r_date = r_date;
    }
}
