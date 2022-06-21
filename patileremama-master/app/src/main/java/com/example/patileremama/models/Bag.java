package com.example.patileremama.models;

public class Bag {
    public String markName;
    public int kilo;
    public int cost;
    public String image;
    public int count;

    public Bag(String markName,int kilo, int cost,String image,int count) {
        this.markName = markName;
        this.kilo = kilo;
        this.cost = cost;
        this.image = image;
        this.count = count;
    }
    public Bag() {}
}
