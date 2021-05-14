
package model.movie;

import java.awt.Image;
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
 * @author Thyago De Oliveira Alves
 * @author José Felipe Flores da Silva
 */
public class Movie {

    private String id, title, description, restriction, duration, director;
    private  ArrayList<String> category;
    private boolean isAvailable;
    private Date rentDate;
    private ImageIcon thumbnail;
    private int machine;

    /**
     * Constructor with no machineID
     * @param id discID
     * @param title
     * @param description
     * @param restriction
     * @param isAvailable is it currently available
     * @param rentDate when it was rented
     * @param thumbnail link to image
     * @param duration
     * @param director 
     */
    public Movie(String id, String title, String description, String restriction, boolean isAvailable, Date rentDate, String thumbnail, String duration, String director)  {
        this.id = id;
        this.title = title;
        this.description = description;
        this.restriction = restriction;
        this.isAvailable = isAvailable;
        this.rentDate = rentDate;
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

    /**
     * Constructor with no rentDate, no machine and no availability
     * @param id discID
     * @param title
     * @param description
     * @param restriction
     * @param thumbnail link to image
     * @param duration
     * @param director 
     */
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

    /**
     * Constructor with no description and no machine
     * @param id
     * @param title
     * @param restriction
     * @param isAvailable
     * @param rentDate
     * @param thumbnail
     * @param duration
     * @param director 
     */
    public Movie(String id, String title, String restriction, boolean isAvailable, Date rentDate, String thumbnail, String duration, String director) {
        this.id = id;
        this.title = title;
        this.restriction = restriction;
        this.isAvailable = isAvailable;
        this.description = "Description not currently available.";
        this.rentDate = rentDate;
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

    /**
     * Constructor with no rentDate and no machine
     * @param id
     * @param title
     * @param description
     * @param restriction
     * @param isAvailable
     * @param thumbnail
     * @param duration
     * @param director 
     */
    public Movie(String id, String title, String description, String restriction, boolean isAvailable, String thumbnail, String duration, String director) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.restriction = restriction;
        this.isAvailable = isAvailable;
        this.rentDate = null;
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

    /**
     * Constructor with no description, no rentDate and no machine
     * @param id
     * @param title
     * @param restriction
     * @param isAvailable
     * @param thumbnail
     * @param duration
     * @param director 
     */
    public Movie(String id, String title, String restriction, boolean isAvailable, String thumbnail, String duration, String director) {
        this.id = id;
        this.title = title;
        this.description = "Description not currently available.";
        this.restriction = restriction;
        this.isAvailable = isAvailable;
        this.rentDate = null;
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

    /**
     * Constructor with no machine and no availability
     * @param id
     * @param title
     * @param description
     * @param restriction
     * @param thumbnail
     * @param machine
     * @param duration
     * @param director 
     */
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

    /**
     * Constructor with ID only
     * @param discID 
     */
    public Movie(String discID) {
        this.id = discID;
    }

    /**
     * Constructor with only discID and rentDate
     * @param discID
     * @param rentDate 
     */
    public Movie(String discID, Date rentDate) {
        this.id = discID;
        this.rentDate = rentDate;
    }

    /**
     * Constructor with only discID, title and rentDate
     * @param discID
     * @param title
     * @param rentDate 
     */
    public Movie(String discID, String title, Date rentDate) {
        this.id = discID;
        this.title = title;
        this.rentDate = rentDate;
    }

    /**
     * 
     * @return movie title
     */
    @Override
    public String toString() {
        return this.title;
    }
    
    public String getDuration() {
        return duration;
    }

    public String getDirector() {
        return director;
    }

    /**
     * Get an ImageIcon
     * @return 
     */
    public ImageIcon getThumbnail() {
        return thumbnail;
    }
    
    /**
     * 
     * @param width
     * @param height
     * @return resized ImageIcon
     */
    public ImageIcon getResizedThumbnail(int width, int height) {
        Image img = this.thumbnail.getImage();
        img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        
        return new ImageIcon(img);
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
    
    /**
     * Adds a genre to a movie
     * @param c genre
     */
    public void addCategory(String c) {
        this.category.add(c);
    }

    public String getDescription() {
        return description;
    }

    public String getRestriction() {
        return restriction;
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
