package com.example.loginpage;

import java.util.ArrayList;
import java.util.List;

public class Users {

    private String email;
    private String highIntensity;
    private String lowIntensity;
    private String totalMovements;
    List<String> time = new ArrayList<>();

    private ArrayList<List<Object>> userData = new ArrayList<List<Object>>();

    public Users(){}

    public Users(String email_, ArrayList<List<Object>> userData_, String highIntensity_, String lowIntensity_, String totalMovements_, List<String> time_){
        this.email = email_;
        this.userData = userData_;
        this.highIntensity = highIntensity_;
        this.lowIntensity = lowIntensity_;
        this.totalMovements = totalMovements_;
        this.time = time_;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHighIntensity() {
        return highIntensity;
    }

    public void setHighIntensity(String highIntensity) {
        this.highIntensity = highIntensity;
    }

    public String getLowIntensity() {
        return lowIntensity;
    }

    public void setLowIntensity(String lowIntensity) {
        this.lowIntensity = lowIntensity;
    }

    public String getTotalMovements() {
        return totalMovements;
    }

    public void setTotalMovements(String totalMovements) {
        this.totalMovements = totalMovements;
    }

    public ArrayList<List<Object>> getUserData() {
        return userData;
    }

    public void setUserData(ArrayList<List<Object>> userData) {
        this.userData = userData;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }
}
