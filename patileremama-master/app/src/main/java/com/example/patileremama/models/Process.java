package com.example.patileremama.models;

public class Process {
    public String boxOwnerAddress;
    public String boxOwnerName;
    public int cost;
    public String markName;
    public String name;

    public Process(String boxOwnerAddress,String boxOwnerName, int cost,String markName,String name) {
        this.boxOwnerAddress = boxOwnerAddress;
        this.boxOwnerName = boxOwnerName;
        this.cost = cost;
        this.markName = markName;
        this.name = name;
    }
    public Process() {}
}
