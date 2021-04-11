package com.example.greenfarm.pojo;

import java.util.Date;

public class Farm {
    private int id;

    private int ownerId;

    private String address;

    private String description;

    private String picturePath;

    private int serviceLife;

    private double area;

    private double price;

    private Date updateTime;

    public Farm() {
    }

    public Farm(int id, String telephone, int ownerId, String address, String description, String picturePath, int serviceLife, double area, double price, Date updateTime) {
        this.id = id;
        this.ownerId = ownerId;
        this.address = address;
        this.description = description;
        this.picturePath = picturePath;
        this.serviceLife = serviceLife;
        this.area = area;
        this.price = price;
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public int getServiceLife() {
        return serviceLife;
    }

    public void setServiceLife(int serviceLife) {
        this.serviceLife = serviceLife;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Farm{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", serviceLife=" + serviceLife +
                ", area=" + area +
                ", price=" + price +
                ", updateTime=" + updateTime +
                '}';
    }
}
