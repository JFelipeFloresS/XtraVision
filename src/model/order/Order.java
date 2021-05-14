
package model.order;

import java.sql.Date;
import model.movie.Movie;

/**
 * @author Thyago De Oliveira Alves
 * @author José Felipe Flores da Silva
 */
public class Order {
    private int rentID, customerID;
    private final int machineID;
    private Movie movie;
    private String status;
    private Date rentDate;
    private double paidFor;

    /**
     * Constructor full
     * @param rentID
     * @param customerID
     * @param machineID
     * @param status
     * @param rentDate
     * @param paidFor
     * @param discID 
     */
    public Order(int rentID, int customerID, int machineID, String status, Date rentDate, double paidFor, String discID) {
        this.rentID = rentID;
        this.customerID = customerID;
        this.machineID = machineID;
        this.movie = new Movie(discID, rentDate);
        this.status = status;
        this.rentDate = rentDate;
        this.paidFor = paidFor;
    }
    
       public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setDiscID(String discID) {
        this.movie = new Movie(discID);
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

    public Movie getMovie() {
        return this.movie;
    }

    public String getStatus() {
        return this.status;
    }

    public Date getRentDate() {
        return this.rentDate;
    }

    public void setPaidFor(double paidFor) {
        this.paidFor = paidFor;
    }
    
    
}