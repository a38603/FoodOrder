package com.example.foodorder.Domain;

import java.util.List;

public class Cart {
    private List<Foods> initList;
    private double calculateCart;

    public Cart(List<Foods> initList, double calculateCart) {
        this.initList = initList;
        this.calculateCart = calculateCart;
    }

    public List<Foods> getInitList() {
        return initList;
    }

    public void setInitList(List<Foods> initList) {
        this.initList = initList;
    }

    public double getCalculateCart() {
        return calculateCart;
    }

    public void setCalculateCart(double calculateCart) {
        this.calculateCart = calculateCart;
    }


}