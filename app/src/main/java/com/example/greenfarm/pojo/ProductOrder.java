package com.example.greenfarm.pojo;

import java.util.Date;

public class ProductOrder {
    private int id;
    private int productId;
    private int customerId;
    private int farmerId;
    private int amount;
    private double money;//订单金额
    private Date updateTime;
    private int status;

    public ProductOrder() {
    }

    public ProductOrder(int id, int productId, int customerId, int farmerId, int amount, double money, Date updateTime, int status) {
        this.id = id;
        this.productId = productId;
        this.customerId = customerId;
        this.farmerId = farmerId;
        this.amount = amount;
        this.money = money;
        this.updateTime = updateTime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProductOrder{" +
                "id=" + id +
                ", productId=" + productId +
                ", customerId=" + customerId +
                ", farmerId=" + farmerId +
                ", amount=" + amount +
                ", money=" + money +
                ", updateTime=" + updateTime +
                ", status=" + status +
                '}';
    }
}
