package com.tshirtapp.api.dto;

import com.tshirtapp.domain.ImageOrder;
import com.tshirtapp.domain.Product;
import com.tshirtapp.domain.TextOrder;
import com.tshirtapp.domain.User;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by aminedev on 1/20/16.
 */
public class OrderClientDTO {


    private String id;
    private Product product;

    private int quantity;
    private String status;
    private ZonedDateTime deliverDate;
    private Long ref;
    private double total;
    private ZonedDateTime creationDate;
    private String description;
    private String canceledFor;
    private String payment;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCanceledFor() {
        return canceledFor;
    }

    public void setCanceledFor(String canceledFor) {
        this.canceledFor = canceledFor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(ZonedDateTime deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Long getRef() {
        return ref;
    }

    public void setRef(Long ref) {
        this.ref = ref;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
