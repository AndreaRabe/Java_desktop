package com.example.employeemanagement;

public class employeeData {

    public String numEmployee;
    public String name;
    public String address;

    public employeeData(String numEmployee, String name, String address){
        this.numEmployee = numEmployee;
        this.name = name;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getNumEmployee() {
        return numEmployee;
    }
}
