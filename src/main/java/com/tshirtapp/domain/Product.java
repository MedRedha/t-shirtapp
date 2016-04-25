package com.tshirtapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.gridfs.GridFsResource;

/**
 * Created by Amine on 12/26/15.
 */
@Document
public class Product {


    @Id
    private String id ;
    private String ref;
    @DBRef
    private ProductType productType;
    private String logoPath;
    private String status;
    private String color;
    private String size;
    private Integer quantity;
    private String logoPathBack;
    private String logoPathLeft;
    private String logoPathRight;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getLogoPathBack() {
        return logoPathBack;
    }

    public void setLogoPathBack(String logoPathBack) {
        this.logoPathBack = logoPathBack;
    }

    public String getLogoPathLeft() {
        return logoPathLeft;
    }

    public void setLogoPathLeft(String logoPathLeft) {
        this.logoPathLeft = logoPathLeft;
    }

    public String getLogoPathRight() {
        return logoPathRight;
    }

    public void setLogoPathRight(String logoPathRight) {
        this.logoPathRight = logoPathRight;
    }
}
