package com.example.employeemanagement;

public class enterpriseData {

    public String numEnterprise;
    public String designation;

    public enterpriseData(String numEnterprise, String designation){
        this.numEnterprise = numEnterprise;
        this.designation = designation;
    }

    public String getDesignation() {
        return designation;
    }

    public String getNumEnterprise() {
        return numEnterprise;
    }
}
