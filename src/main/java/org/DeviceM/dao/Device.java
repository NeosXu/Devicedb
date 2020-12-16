package org.DeviceM.dao;

import java.io.Serializable;
import java.sql.Timestamp;

public class Device implements Serializable {
    public Integer id;
    public String name;
    public String type;
    public Timestamp purchaseTime;
    public Double price;
    public String producer;
    public Timestamp warrantyUntil;
    public Integer transactorId;
    public String deviceStatus;
    public Integer adminId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Timestamp purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Timestamp getWarrantyUntil() {
        return warrantyUntil;
    }

    public void setWarrantyUntil(Timestamp warrantyUntil) {
        this.warrantyUntil = warrantyUntil;
    }

    public Integer getTransactorId() {
        return transactorId;
    }

    public void setTransactorId(Integer transactorId) {
        this.transactorId = transactorId;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", purchaseTime=" + purchaseTime +
                ", price=" + price +
                ", producer='" + producer + '\'' +
                ", warrantyUntil=" + warrantyUntil +
                ", transactorId=" + transactorId +
                ", deviceStatus='" + deviceStatus + '\'' +
                ", adminId=" + adminId +
                '}';
    }
}
