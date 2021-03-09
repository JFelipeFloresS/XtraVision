/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Date;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class Movie {
    
    private final int id;
    private final String title, category, description, restriction;
    private boolean isAvailable;
    private Date rentDate;

    public Movie(int id, String title, String category, String description, String restriction, boolean isAvailable, Date rentDate) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.restriction = restriction;
        this.isAvailable = isAvailable;
        this.rentDate = rentDate;
    }

    public Movie(int id, String title, String category, String restriction, boolean isAvailable, Date rentDate) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.restriction = restriction;
        this.isAvailable = isAvailable;
        this.rentDate = rentDate;
        this.description = "Description not currently available.";
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getRestriction() {
        return restriction;
    }

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public Date getRentDate() {
        return rentDate;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setRentDate(Date rentDate) {
        this.rentDate = rentDate;
    }
    
    
}
