package com.example.employeemanagement;

import java.util.Date;

public class workData {

    public String numEmployee;
    public String name;
    public String address;
    public double salary;
    public Date date;
    public String numEnterprise;
    public String designation;
    
    public workData(String numEmployee, String name, String address, double salary, Date date, String numEnterprise, String designation){
        this.numEmployee = numEmployee;
        this.name = name;
        this.address = address;
        this.salary = salary;
        this.date = date;
        this.numEnterprise = numEnterprise;
        this.designation = designation;
    }

    public String getNumEnterprise() {
        return numEnterprise;
    }

    public String getDesignation() {
        return designation;
    }

    public String getName() {
        return name;
    }

    public String getNumEmployee() {
        return numEmployee;
    }

    public String getAddress() {
        return address;
    }

    public Date getDate() {
        return date;
    }

    public double getSalary() {
        return salary;
    }
}
