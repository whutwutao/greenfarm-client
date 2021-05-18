package com.example.greenfarm.pojo;

/**
 * 购物车
 */
public class Cart {
    private int id;
    private int customerId;
    private int productId;
    private int amount;
    private double money;

    public Cart() {
    }

    public Cart(int id, int customerId, int productId, int amount, double money) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.amount = amount;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", productId=" + productId +
                ", amount=" + amount +
                ", money=" + money +
                '}';
    }
}

