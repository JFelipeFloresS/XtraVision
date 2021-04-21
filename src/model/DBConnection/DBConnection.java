/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.DBConnection;

import controller.Controller;
import java.io.IOException;
import java.util.ArrayList;
import model.movie.Movie;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import model.customer.Customer;
import model.order.Order;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class DBConnection {
    
    private String dbServer, dbUser, dbPass;
    private Controller controller;
    private Connection conn;
    
    public DBConnection (Controller controller) {
        this.controller = controller;
        setUpCredentials();
    }

    public DBConnection() {
        setUpCredentials();
    }
    
    private void setUpCredentials() {
        this.dbServer = "jdbc:mysql://apontejaj.com:3306/Felipe_2019405?useSSL=false";
        this.dbUser = "Felipe_2019405";
        this.dbPass = "2019405";
    }
    
    private Connection establishConnection() throws SQLException {
        return  DriverManager.getConnection(this.dbServer, this.dbUser, this.dbPass);
    }
    /**
     * Gets all credit card numbers from a given customer
     * @param id customerID
     * @return list of card numbers
     */
    public ArrayList<String> getCustomerCreditCards(int id) {
        ArrayList<String> cards = new ArrayList<>();
        try {
            String query = "SELECT cardNumber FROM creditCards WHERE customerID=?";
            this.conn = establishConnection();
            try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while(rs.next()) {
                        cards.add(rs.getString("cardNumber"));
                    }
                }
            }
            this.conn.close();
        } catch (SQLException e) {
            System.out.println("Error getCustomerCreditCards(): \r\n" + e.getMessage());
        }
        return cards;
    }
    
    /**
     * Gets one of each available movie in the machine
     * @param id machineID
     * @return list of unique movies
     * @throws java.io.IOException 
     */
    public ArrayList<Movie> getMachineCurrentMovies(int id) throws IOException {
        ArrayList<Movie> movies = new ArrayList<>();
        ArrayList<String> unique = new ArrayList<>();
        
        try {
            String query = "SELECT * FROM discs LEFT JOIN movies ON discs.movieID = movies.movieID WHERE discs.machine=" + id +";";
            this.conn = establishConnection();
            try (Statement stmt = this.conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                
                while (rs.next()) {
                    if (!unique.contains(rs.getString("discs.movieID"))) {
                        unique.add(rs.getString("discs.movieID"));
                        movies.add(new Movie(rs.getString("discID"), rs.getString("title"), rs.getString("description"), rs.getString("ageRestriction"), true, rs.getString("thumbnail")));
                    }
                }
            }
            this.conn.close();
        } catch (SQLException e) {
            System.out.println("Error getMachineCurrentMovies(): \r\n" + e.getMessage());
        }
        
        return movies;
    }
    
    /**
     * Creates a new row in the table rent and updates the current machine of the movie to be rented.
     * @param discID 
     * @param machine machineID (currently 1 to 6)
     * @param customer customerID
     */
    public void rentMovie(String discID, int machine, Customer customer) {
        try {
            String query = "INSERT INTO rent(discID, customerID, machineID, status) VALUES(?, ?, ?, 'rented');";
            this.conn = establishConnection();
            try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
                stmt.setString(1, discID);
                stmt.setInt(2, customer.getId());
                stmt.setInt(3, machine);
                stmt.executeUpdate();
            }
            String query2 = "UPDATE discs SET machineID=-1 WHERE discID=?;";
            try (PreparedStatement stmt2 = this.conn.prepareStatement(query2)) {
                stmt2.setString(1, discID);
                stmt2.executeUpdate();
            }
            this.conn.close();
            JOptionPane.showConfirmDialog(this.controller.getFrame(), "You successfully rented your movie!");
        } catch (SQLException e) {
            System.out.println("Error rentMovie(): \r\n" + e.getMessage());
            JOptionPane.showConfirmDialog(this.controller.getFrame(), "Error rentMovie(): \r\n" + e.getMessage());
        }
    }
    
    /**
     * Gets current order of a disc
     * @param movieID
     * @return an order if rented or null if not currently rented
     */
    public Order getOrderFromMovieID(String movieID) {
        Order order = null;
        
        try {
            String query = "SELECT * FROM rent WHERE movieID=? AND status='rented';";
            this.conn = establishConnection();
            try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
                stmt.setString(1, movieID);
                try (ResultSet rs = stmt.executeQuery()) {
                    while(rs.next()) {
                        order = new Order(rs.getInt("rentID"),rs.getInt("customerID"),rs.getInt("machineID"),rs.getString("discID"),rs.getString("status"),rs.getTimestamp("rentDate"));
                    }
                }
            }
            this.conn.close();
            
        } catch (SQLException e) {
            System.out.println("Error getOrderFromMovieID(): \r\n" + e.getMessage());
        }
        
        return order;
    }
    
    /**
     * Updates the status of an order to returned and sets the current location of the disc to the current machine.
     * @param order 
     * @param machineID 
     */
    public void returnMovie(Order order, int machineID) {
        try {
            String query = "UPDATE rent SET status=returned WHERE rentID=" + order.getRentID() + "; UPDATE discs SET machine=" + machineID + " WHERE discID=" + order.getDiscID() + ";";
            this.conn = establishConnection();
            try (Statement stmt = this.conn.createStatement()) {
                stmt.executeUpdate(query);
            }
            this.conn.close();
        } catch (SQLException e) {
            System.out.println("Error returnMovie(): \r\n" + e.getMessage());
        }
    }
    
    /**
     * Updates the status of the rent to paid.
     * @param order 
     */
    public void payForMaxFee(Order order) {
        try {
            String query = "UPDATE rent SET status=paid WHERE rentID=" + order.getRentID() + ";";
            this.conn = establishConnection();
            try (Statement stmt = this.conn.createStatement()) {
                stmt.executeUpdate(query);
            }
            this.conn.close();
        } catch (SQLException e) {
            System.out.println("Error payForMaxFee(): \r\n" + e.getMessage());
        }
    }
    
    /**
     * Gets distinct entries for genres.
     * @return list of genres
     */
    public ArrayList<String> getUniqueGenres() {
        ArrayList<String> genres = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT genre FROM moviesGenre;";
            this.conn = establishConnection();
            try (Statement stmt = this.conn.createStatement()) {
                stmt.execute(query);
            }
            this.conn.close();
        } catch (SQLException e) {
            System.out.println("Error getUniqueGenres(): \r\n" + e.getMessage());
        }
        return genres;
    }
    
    
    
}
