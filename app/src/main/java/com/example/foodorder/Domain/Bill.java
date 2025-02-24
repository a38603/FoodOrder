package com.example.foodorder.Domain;

import java.util.ArrayList;

public class Bill {
    private ArrayList<Foods> productList;
    private double totalFee;
    private double tax;
    private double delivery;
    private double total;

    public Bill(double totalFee, double tax, double delivery, double total) {
        this.totalFee = totalFee;
        this.tax = tax;
        this.delivery = delivery;
        this.total = total;
        this.productList = productList;
    }

    public ArrayList<Foods> getProductList() {
        return productList;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public double getTax() {
        return tax;
    }

    public double getDelivery() {
        return delivery;
    }

    public double getTotal() {
        return total;
    }
    public double getnumberinCart(){
        return getnumberinCart();
    }
}
