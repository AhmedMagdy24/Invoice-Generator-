package com.mycompany.controller;

public class component {
    private String itemName;
    private int itemPrice;
    private int count;
    public component() {}

    public String getItemName() {
        return itemName;
    }
    public int getItemTotalValue() {
        return itemPrice * count;
    }
    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }
    public int getCountValue() {
        return count;
    }
    public int getItemPriceValue() {
        return itemPrice;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public void setCountValue(int count) {
        this.count = count;
    }
}
