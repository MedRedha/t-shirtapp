package com.tshirtapp.api.dto;

import com.tshirtapp.domain.ImageOrder;
import com.tshirtapp.domain.Product;
import com.tshirtapp.domain.TextOrder;
import com.tshirtapp.domain.User;
import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by aminedev on 1/20/16.
 */
public class OrderDTO {


    private String id;
    private Product product;
    private String uuid;
    private int quantity;
    private List<ImageOrder> imagerOrderList;
    private List<TextOrder> textOrders;
    private String status;
    private User client;
    private ZonedDateTime creationDate;
    private ZonedDateTime validationDate;
    private ZonedDateTime cancelDate;
    private ZonedDateTime deliverDate;
    private User validatedBy;
    private User canceledBy;
    private User realizedBy;
    private String createdFrom;
    private Long ref;
    private double total;
    private double advance;

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

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(ZonedDateTime validationDate) {
        this.validationDate = validationDate;
    }

    public ZonedDateTime getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(ZonedDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }

    public ZonedDateTime getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(ZonedDateTime deliverDate) {
        this.deliverDate = deliverDate;
    }

    public User getValidatedBy() {
        return validatedBy;
    }

    public void setValidatedBy(User validatedBy) {
        this.validatedBy = validatedBy;
    }

    public User getCanceledBy() {
        return canceledBy;
    }

    public void setCanceledBy(User canceledBy) {
        this.canceledBy = canceledBy;
    }

    public User getRealizedBy() {
        return realizedBy;
    }

    public void setRealizedBy(User realizedBy) {
        this.realizedBy = realizedBy;
    }

    public String getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<ImageOrder> getImagerOrderList() {
        return imagerOrderList;
    }

    public void setImagerOrderList(List<ImageOrder> imagerOrderList) {
        this.imagerOrderList = imagerOrderList;
    }

    public List<TextOrder> getTextOrders() {
        return textOrders;
    }

    public void setTextOrders(List<TextOrder> textOrders) {
        this.textOrders = textOrders;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
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


    public double getAdvance() {
        return advance;
    }

    public void setAdvance(double advance) {
        this.advance = advance;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
