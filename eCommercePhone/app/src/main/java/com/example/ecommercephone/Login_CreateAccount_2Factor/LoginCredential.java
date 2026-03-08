package com.example.ecommercephone.Login_CreateAccount_2Factor;

public class LoginCredential {
    //Fields
    private String firstName;
    private String lastName;
    private String subscription;
    private String username;
    private String password;
    private String deviceManufacturer;
    private String deviceModel;

    // Constructors
    public LoginCredential(String firstName, String lastName, String username, String password, String manufacturer, String model) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.deviceManufacturer = deviceManufacturer;
        this.deviceModel = deviceModel;

    }

    //Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return password;
    }

    public String getDeviceManufacturer() {
        return deviceManufacturer;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    // Setters in the event you only want to set a value for one or two fields
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDeviceManufacturer(String deviceManufacturer) {
        this.deviceManufacturer = deviceManufacturer;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    //Method to call everytime data is added to the database in the SetLoginCredential class
    public String getInputData() {
        return ("User *" + firstName + "* successfully added");
    }
}
