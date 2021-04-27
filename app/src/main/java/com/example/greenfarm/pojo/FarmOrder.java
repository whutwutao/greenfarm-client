package com.example.greenfarm.pojo;

import java.util.Date;

public class FarmOrder {
    private int id;

    private int farmId;

    private int customerId;

    private int status;

    private Date updateTime;//订单最近一次更新的时间

    public FarmOrder() {
    }

    public FarmOrder(int id, int farmId, int customerId, int status, Date updateTime) {
        this.id = id;
        this.farmId = farmId;
        this.customerId = customerId;
        this.status = status;
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "FarmOrder{" +
                "id=" + id +
                ", farmId=" + farmId +
                ", customerId=" + customerId +
                ", status=" + status +
                ", updateTime=" + updateTime +
                '}';
    }
}
