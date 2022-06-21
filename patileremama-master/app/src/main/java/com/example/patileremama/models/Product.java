package com.example.patileremama.models;

public class Product {
    public String markName;
    public int kilo;
    public int cost;
    public String image;

    public Product(String markName,int kilo, int cost,String image) {
        this.markName = markName;
        this.kilo = kilo;
        this.cost = cost;
        this.image = image;
    }
    public Product() {}
}
