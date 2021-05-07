package com.example.greenfarm.pojo;

public class FarmOrderBean {

    private User customer;

    private Farm farm;

    private FarmOrder order;

    private boolean selected;

    public FarmOrderBean() {
    }

    public FarmOrderBean(User customer, Farm farm, FarmOrder order, boolean selected) {
        this.customer = customer;
        this.farm = farm;
        this.order = order;
        this.selected = selected;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public FarmOrder getOrder() {
        return order;
    }

    public void setOrder(FarmOrder order) {
        this.order = order;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "FarmOrderBean{" +
                "customer=" + customer +
                ", farm=" + farm +
                ", order=" + order +
                ", selected=" + selected +
                '}';
    }
}
