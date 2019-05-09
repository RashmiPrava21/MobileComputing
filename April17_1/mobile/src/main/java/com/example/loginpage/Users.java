package com.example.loginpage;

import java.util.ArrayList;
import java.util.List;

public class Users {

    private String email;
    private ArrayList<List<Object>> userData = new ArrayList<List<Object>>();

    public Users(){}

    public Users(String email_, ArrayList<List<Object>> userData_){
        this.email = email_;
        this.userData = userData_;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<List<Object>> getUserData() {
        return userData;
    }

    public void setUserData(ArrayList<List<Object>> userData) {
        this.userData = userData;
    }
}
