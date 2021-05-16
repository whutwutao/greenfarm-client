package com.example.greenfarm.pojo;

public class Category {
    private int id;
    private String cname;
    private int parentId;
    private boolean selected;

    public Category() {
    }

    public Category(int id, String cname, int parentId, boolean selected) {
        this.id = id;
        this.cname = cname;
        this.parentId = parentId;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", cname='" + cname + '\'' +
                ", parentId=" + parentId +
                ", selected=" + selected +
                '}';
    }
}
