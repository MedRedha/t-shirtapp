package com.tshirtapp.util;

/**
 * Created by aminedev on 3/26/16.
 */
public class LeftReport {
    String takenBy;
    Double total;
    Double advance;
    Double rest;

    public LeftReport(String takenBy, Double total, Double advance, Double rest) {
        this.takenBy = takenBy;
        this.total = total;
        this.advance = advance;
        this.rest = rest;
    }

    public String getTakenBy() {
        return takenBy;
    }

    public void setTakenBy(String takenBy) {
        this.takenBy = takenBy;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getAdvance() {
        return advance;
    }

    public void setAdvance(Double advance) {
        this.advance = advance;
    }

    public Double getRest() {
        return rest;
    }

    public void setRest(Double rest) {
        this.rest = rest;
    }
}
