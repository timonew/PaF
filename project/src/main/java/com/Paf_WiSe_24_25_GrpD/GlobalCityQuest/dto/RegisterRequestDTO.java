package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;

public class RegisterRequestDTO {

    private String userName; // Benutzername
    private String password; // Passwort


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
