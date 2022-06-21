package com.example.patileremama.models;

public class CurrentUser {
    public String id;
    public String email;
    public String username;
    public String address;


    public CurrentUser(String id, String email,String username, String address) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.address = address;
    }
    public CurrentUser() {}
}
