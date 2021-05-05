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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class Movie {

    private final String id, title, description, restriction, duration, director;
    private final ArrayList<String> category;
    private boolean isAvailable;
    private Date rentDate, returnDate;
    private ImageIcon thumbnail;
    private int machine;

    public Movie(String id, String title, String description, String restriction, boolean isAvailable, Date rentDate, String thumbnail, String duration, String director)  {
        this.id = id;
        this.title = title;
        this.description = description;
        this.restriction = restriction;
        this.isAvailable = isAvailable;
        this.rentDate = rentDate;
        this.returnDate = null;
        try {
            this.thumbnail = new ImageIcon(ImageIO.read(new URL(thumbnail)));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.duration = duration;
        this.director = director;
        this.category = new ArrayList<>();
    }

    public Movie(String id, String title, String description, String restriction, String thumbnail, String duration, String director) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.restriction = restriction;
        try {
            this.thumbnail = new ImageIcon(ImageIO.read(new URL(thumbnail)));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.duration = duration;
        this.director = director;
        this.category = new ArrayList<>();
    }

    public Movie(String id, String title, String restriction, boolean isAvailable, Date rentDate, String thumbnail, String duration, String director) {
        this.id = id;
        this.title = title;
        this.restriction = restriction;
        this.isAvailable = isAvailable;
        this.description = "Description not currently available.";
        this.rentDate = rentDate;
        this.returnDate = null;
        try {
            this.thumbnail = new ImageIcon(ImageIO.read(new URL(thumbnail)));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.duration = duration;
        this.director = director;
        this.category = new ArrayList<>();
    }

    public Movie(String id, String title, String description, String restriction, boolean isAvailable, String thumbnail, String duration, String director) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.restriction = restriction;
        this.isAvailable = isAvailable;
        this.rentDate = null;
        this.returnDate = null;
        try {
            this.thumbnail = new ImageIcon(ImageIO.read(new URL(thumbnail)));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.duration = duration;
        this.director = director;
        this.category = new ArrayList<>();
    }

    public Movie(String id, String title, String restriction, boolean isAvailable, String thumbnail, String duration, String director) {
        this.id = id;
        this.title = title;
        this.description = "Description not currently available.";
        this.restriction = restriction;
        this.isAvailable = isAvailable;
        this.rentDate = null;
        this.returnDate = null;
        try {
            this.thumbnail = new ImageIcon(ImageIO.read(new URL(thumbnail)));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.duration = duration;
        this.director = director;
        this.category = new ArrayList<>();
    }

    public Movie(String id, String title, String description, String restriction, String thumbnail, int machine, String duration, String director){
        this.id = id;
        this.title = title;
        this.description = description;
        this.restriction = restriction;
        try {
            this.thumbnail = new ImageIcon(ImageIO.read(new URL(thumbnail)));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.machine = machine;
        this.duration = duration;
        this.director = director;
        this.category = new ArrayList<>();
    }

    public String getDuration() {
        return duration;
    }

    public String getDirector() {
        return director;
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

    public ArrayList<String> getCategory() {
        return category;
    }
    
    public void addCategory(String c) {
        this.category.add(c);
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

    /**
     * Gets price to be paid if it's late return
     * @param movie object movie to be returned
     * @return extra to be paid
     */
    public static double getLateReturnPrice(Movie movie) {
        double price = 1.50;
        
        Date date = new Date(System.currentTimeMillis());
        long diffInDays = ( (date.getTime() - movie.getRentDate().getTime()) / (1000 * 60 * 60 * 24) ) % 365;
        
        return (price * diffInDays) - price;
    }
    
    public static Comparator<Movie> MovieTitleComparator = (Movie m1, Movie m2) -> m1.getTitle().compareToIgnoreCase(m2.getTitle());

}
