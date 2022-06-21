package com.example.patileremama.models;

public class Bucket {
    public String id;
    public String boxId;
    public String boxMarkName;
    public String boxName;
    public String address;
    public String image;
    public int kilo;
    public int cost;
    public int count;

    public Bucket(String id,String boxId,String boxName,String boxMarkName,String address,String image,int kilo, int cost,int count) {
        this.id = id;
        this.boxId = boxId;
        this.boxName = boxName;
        this.boxMarkName = boxMarkName;
        this.address = address;
        this.image = image;
        this.kilo = kilo;
        this.cost = cost;
        this.count = count;
    }
    public Bucket() {}
}
