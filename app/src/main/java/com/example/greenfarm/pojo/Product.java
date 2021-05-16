package com.example.greenfarm.pojo;

public class Product {
    private int id;

    private int farmerId;

    private int category;

    private String name;

    private String description;

    private String pictureUrl;

    private double price;

    public Product() {
    }

    public Product(int id, int farmerId, int category, String name, String description, String pictureUrl, double price) {
        this.id = id;
        this.farmerId = farmerId;
        this.category = category;
        this.name = name;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", farmerId=" + farmerId +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", price=" + price +
                '}';
    }
}
