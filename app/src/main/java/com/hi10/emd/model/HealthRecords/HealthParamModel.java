package com.hi10.emd.model.HealthRecords;

public class HealthParamModel {
    private String user_id;
    private String d_name;
    private String symtoms;
    private String doc_name;

    public HealthParamModel(String user_id, String d_name, String symtoms, String doc_name) {
        this.user_id = user_id;
        this.d_name = d_name;
        this.symtoms = symtoms;
        this.doc_name = doc_name;
    }
}
