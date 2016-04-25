package com.tshirtapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;

/**
 * Created by aminedev on 1/19/16.
 */
@Document
public class Order {

    @Id
    private String id ;
    @NotNull
    private String productID;
    @NotNull
    private String uuid;
    private int quantity;
    private List<ImageOrder> imagerOrderList;
    private List<TextOrder> textOrders;
    private String status;
    @NotNull
    private String fbID;
    private ZonedDateTime creationDate;
    private ZonedDateTime validationDate;
    private ZonedDateTime cancelDate;
    private ZonedDateTime deliverDate;
    private User validatedBy;
    private User canceledBy;
    private User realizedBy;
    private String createdFrom;
    private Double total;
    private Long ref;
    private Double advance;

    private String userID;
    private String description;
    private String canceledFor;
    private String payment;


    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public String getFbID() {
        return fbID;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Long getRef() {
        return ref;
    }

    public void setRef(Long ref) {
        this.ref = ref;
    }

    public Double getAdvance() {
        return advance;
    }

    public void setAdvance(Double advance) {
        this.advance = advance;
    }

    public String  getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

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
}
