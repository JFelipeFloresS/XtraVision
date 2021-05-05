/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.order;

import java.sql.Date;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class Order {
    private int rentID, customerID;
    private final int machineID;
    private String discID, status;
    private Date rentDate;
    private double paidFor;

    public Order(int rentID, int customerID, int machineID, String discID, String status, Date rentDate, double paidFor) {
        this.rentID = rentID;
        this.customerID = customerID;
        this.machineID = machineID;
        this.discID = discID;
        this.status = status;
        this.rentDate = rentDate;
        this.paidFor = paidFor;
    }
    
    public Order(int machineID) {
        this.machineID = machineID;
        this.status = "not ready";
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setDiscID(String discID) {
        this.discID = discID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPaidFor() {
        return this.paidFor;
    }

    public int getRentID() {
        return this.rentID;
    }

    public int getCustomerID() {
        return this.customerID;
    }

    public int getMachineID() {
        return this.machineID;
    }

    public String getDiscID() {
        return this.discID;
    }

    public String getStatus() {
        return this.status;
    }

    public Date getRentDate() {
        return this.rentDate;
    }
    
    
}
