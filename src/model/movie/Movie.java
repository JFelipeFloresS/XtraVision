/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.movie;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class Movie {

    private final String id, title, description, restriction;
    private String[] category;
    private boolean isAvailable;
    private Date rentDate, returnDate;
    private final ImageIcon thumbnail;
    private int machine;

    public Movie(String id, String title, String description, String restriction, boolean isAvailable, Date rentDate, String thumbnail) throws MalformedURLException, IOException {
        this.id = id;
        this.title = title;
        this.description = description;
        this.restriction = restriction;
        this.isAvailable = isAvailable;
        this.rentDate = rentDate;
        this.returnDate = null;
        this.thumbnail = new ImageIcon(ImageIO.read(new URL(thumbnail)));
    }

    public Movie(String id, String title, String description, String restriction, String thumbnail) throws MalformedURLException, IOException {
        this.id = id;
        this.title = title;
        this.description = description;
        this.restriction = restriction;
        this.thumbnail = new ImageIcon(ImageIO.read(new URL(thumbnail)));
    }

    public Movie(String id, String title, String restriction, boolean isAvailable, Date rentDate, String thumbnail) throws MalformedURLException, IOException {
        this.id = id;
        this.title = title;
        this.restriction = restriction;
        this.isAvailable = isAvailable;
        this.description = "Description not currently available.";
        this.rentDate = rentDate;
        this.returnDate = null;
        this.thumbnail = new ImageIcon(ImageIO.read(new URL(thumbnail)));
    }

    public Movie(String id, String title, String description, String restriction, boolean isAvailable, String thumbnail) throws MalformedURLException, IOException {
        this.id = id;
        this.title = title;
        this.description = description;
        this.restriction = restriction;
        this.isAvailable = isAvailable;
        this.rentDate = null;
        this.returnDate = null;
        this.thumbnail = new ImageIcon(ImageIO.read(new URL(thumbnail)));
    }

    public Movie(String id, String title, String restriction, boolean isAvailable, String thumbnail) throws MalformedURLException, IOException {
        this.id = id;
        this.title = title;
        this.description = "Description not currently available.";
        this.restriction = restriction;
        this.isAvailable = isAvailable;
        this.rentDate = null;
        this.returnDate = null;
        this.thumbnail = new ImageIcon(ImageIO.read(new URL(thumbnail)));
    }

    public Movie(String id, String title, String description, String restriction, String thumbnail, int machine) throws MalformedURLException, IOException {
        this.id = id;
        this.title = title;
        this.description = description;
        this.restriction = restriction;
        this.thumbnail = new ImageIcon(ImageIO.read(new URL(thumbnail)));
        this.machine = machine;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public ImageIcon getThumbnail() {
        return thumbnail;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] c) {
        this.category = c;
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

    public int getMachine() {
        return machine;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Date getRentDate() {
        return rentDate;
    }

    public void setRentDate(Date rentDate) {
        this.rentDate = rentDate;
    }

    public void rentMovie(int customerID, int machineID) {
        Calendar calendar = Calendar.getInstance(Locale.UK);
        Date currentDate = new Date(calendar.getTimeInMillis());
        this.rentDate = currentDate;

        this.isAvailable = false;
    }

    /**
     * Returns a movie using the current date, updates the database with where
     * the movie is currently located, when the order was finished and the
     * customer's loyalty points
     *
     * @param machineID the ID of the machine where the movie is being returned
     * to
     */
    public void returnMovie(int machineID) {
        LocalDateTime local = LocalDateTime.now(ZoneId.of("Ireland")); // gets current date and time

        Date currentDate = null;

        if (local.getHour() >= 20) { // if it's past 20pm on the day, the next day will be considered when calculating the difference
            currentDate = new Date(local.getYear(), local.getMonthValue(), local.getDayOfMonth());
        }

        this.returnDate = currentDate;

        long differenceInDays = TimeUnit.DAYS.convert((currentDate.getTime() - this.rentDate.getTime()), TimeUnit.MILLISECONDS); // difference in days between when the movie was rented and when it was returned

        this.isAvailable = true;
    }

}
