package com.example.greenfarm.pojo;

public class Plant {
    private int id;

    private String name;

    private String description;

    private String amount;

    private int farmId;

    private int customerId;

    private int status;//0：未种植；1：种植中；2：已种好

    public Plant() {
    }

    public Plant(int id, String name, String description, String amount, int farmId, int customerId, int status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.farmId = farmId;
        this.customerId = customerId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getFarmId() {
        return farmId;
    }

    public void setFarmId(int farmId) {
        this.farmId = farmId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amount='" + amount + '\'' +
                ", farmId=" + farmId +
                ", customerId=" + customerId +
                ", status=" + status +
                '}';
    }
}
