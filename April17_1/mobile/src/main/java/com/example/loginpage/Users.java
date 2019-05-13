package com.example.loginpage;

import java.util.ArrayList;
import java.util.List;

public class Users {

    private String email;
    private int highIntensity;
    private int lowIntensity;
    private int totalMovements;
    List<String> time = new ArrayList<>();

    private ArrayList<List<Object>> userData = new ArrayList<List<Object>>();

    public Users(){}

    public Users(String email_, ArrayList<List<Object>> userData_, int highIntensity_, int lowIntensity_, int totalMovements_, List<String> time_){
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

    public int getHighIntensity() {
        return highIntensity;
    }

    public void setHighIntensity(int highIntensity) {
        this.highIntensity = highIntensity;
    }

    public int getLowIntensity() {
        return lowIntensity;
    }

    public void setLowIntensity(int lowIntensity) {
        this.lowIntensity = lowIntensity;
    }

    public int getTotalMovements() {
        return totalMovements;
    }

    public void setTotalMovements(int totalMovements) {
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
