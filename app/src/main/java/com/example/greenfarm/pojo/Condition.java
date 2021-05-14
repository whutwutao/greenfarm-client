package com.example.greenfarm.pojo;
//查询条件
public class Condition {
    private String address;
    private String description;
    private double areaMin;
    private double areaMax;
    private double priceMin;
    private double priceMax;
    private int serviceLifeMin;
    private int serviceLifeMax;

    public Condition() {
    }

    public Condition(String address, String description, double areaMin, double areaMax, double priceMin, double priceMax, int serviceLifeMin, int serviceLifeMax) {
        this.address = address;
        this.description = description;
        this.areaMin = areaMin;
        this.areaMax = areaMax;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.serviceLifeMin = serviceLifeMin;
        this.serviceLifeMax = serviceLifeMax;
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

    public double getAreaMin() {
        return areaMin;
    }

    public void setAreaMin(double areaMin) {
        this.areaMin = areaMin;
    }

    public double getAreaMax() {
        return areaMax;
    }

    public void setAreaMax(double areaMax) {
        this.areaMax = areaMax;
    }

    public double getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(double priceMin) {
        this.priceMin = priceMin;
    }

    public double getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(double priceMax) {
        this.priceMax = priceMax;
    }

    public int getServiceLifeMin() {
        return serviceLifeMin;
    }

    public void setServiceLifeMin(int serviceLifeMin) {
        this.serviceLifeMin = serviceLifeMin;
    }

    public int getServiceLifeMax() {
        return serviceLifeMax;
    }

    public void setServiceLifeMax(int serviceLifeMax) {
        this.serviceLifeMax = serviceLifeMax;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", areaMin=" + areaMin +
                ", areaMax=" + areaMax +
                ", priceMin=" + priceMin +
                ", priceMax=" + priceMax +
                ", serviceLifeMin=" + serviceLifeMin +
                ", serviceLifeMax=" + serviceLifeMax +
                '}';
    }
}

