package com.tshirtapp.util;

/**
 * Created by aminedev on 3/26/16.
 */
public class RightReport {

    private String product;
    private String size;
    private String color;
    private String client;
    private String codeLogo;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCodeLogo() {
        return codeLogo;
    }

    public void setCodeLogo(String codeLogo) {
        this.codeLogo = codeLogo;
    }

    public RightReport(String product, String size, String color, String client, String codeLogo) {

        this.product = product;
        this.size = size;
        this.color = color;
        this.client = client;
        this.codeLogo = codeLogo;
    }
}
