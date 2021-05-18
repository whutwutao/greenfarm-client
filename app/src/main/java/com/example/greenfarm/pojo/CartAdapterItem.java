package com.example.greenfarm.pojo;

public class CartAdapterItem {
    private Cart cart;
    private Product product;
    private boolean selected = false;

    public CartAdapterItem() {
    }

    public CartAdapterItem(Cart cart, Product product) {
        this.cart = cart;
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "CartAdapterItem{" +
                "cart=" + cart +
                ", product=" + product +
                ", selected=" + selected +
                '}';
    }
}
