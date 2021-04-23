/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.order;

import java.sql.Timestamp;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class Order {
    private final int rentID, customerID, machineID;
    private final String discID, status;
    private final Timestamp rentDate;
    private final double paidFor;

    public Order(int rentID, int customerID, int machineID, String discID, String status, Timestamp rentDate, double paidFor) {
        this.rentID = rentID;
        this.customerID = customerID;
        this.machineID = machineID;
        this.discID = discID;
        this.status = status;
        this.rentDate = rentDate;
        this.paidFor = paidFor;
    }

    public double getPaidFor() {
        return paidFor;
    }

    public int getRentID() {
        return rentID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getMachineID() {
        return machineID;
    }

    public String getDiscID() {
        return discID;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getRentDate() {
        return rentDate;
    }
    
    
    
    
}
