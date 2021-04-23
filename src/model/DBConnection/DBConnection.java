/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.DBConnection;

import controller.Controller;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import model.movie.Movie;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.customer.Customer;
import model.customer.Loyalty;
import model.order.Order;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class DBConnection {

    private String dbServer, dbUser, dbPass;
    private Controller controller;
    private Connection conn;

    public DBConnection(Controller controller) {
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

    /**
     *
     * @return connection to DB
     * @throws SQLException
     */
    private Connection establishConnection() throws SQLException {
        return DriverManager.getConnection(this.dbServer, this.dbUser, this.dbPass);
    }

    /**
     * Gets all credit card numbers from a given customer
     *
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
                    while (rs.next()) {
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
     *
     * @param card credit card number
     * @return customer with the given card
     */
    public Customer getCustomerFromCreditCard(String card) {
        Customer customer = null;

        try {
            String query = "SELECT * FROM creditCards INNER JOIN customers ON creditCards.customerID=customers.customerID WHERE cardNumber=?";
            this.conn = establishConnection();
            try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
                stmt.setString(1, card);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        customer = new Customer(rs.getInt("customers.customerID"), rs.getString("email"), rs.getInt("currentMovies"), rs.getString("loyalty"));
                    }
                }
            }
            this.conn.close();

        } catch (SQLException e) {
            System.out.println("Error getCustomerFromCreditCard(): \r\n" + e.getMessage());
        }

        return customer;
    }

    /**
     * Creates a customer
     *
     * @param card credit card number (not null)
     * @param email email address (if not input, set to null)
     * @param passInput password (if not input, set to null)
     * @param numberOfMovies number of movies currently rented
     * @return success of creation
     */
    public boolean createCustomer(String card, String email, String passInput, int numberOfMovies) {
        String salt = null;
        String password = null;

        if (passInput != null) {
            salt = createSalt();
            password = createHash(passInput, salt);

        }
        try {
            String query = "INSERT INTO customers(email, currentMovies, loyalty, password, salt) VALUES(?, ?, ?, ?, ?);";
            this.conn = establishConnection();
            try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
                stmt.setString(1, email);
                stmt.setInt(2, numberOfMovies);
                stmt.setString(3, Loyalty.STANDARD.getName());
                stmt.setString(4, password);
                stmt.setString(5, salt);
                stmt.executeQuery();
            }
            this.conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error createCustomerFromCreditCard(): \r\n" + e.getMessage());
            return false;
        }
    }

    /**
     * Generates a secure random salt.
     *
     * @return salt
     */
    private String createSalt() {
        byte[] bytes = new byte[32];

        SecureRandom rd = new SecureRandom();
        rd.nextBytes(bytes);

        BigInteger num = new BigInteger(1, bytes);

        String salt = num.toString();

        return salt;
    }

    /**
     * Creates a hash with MD5
     *
     * @param pass password
     * @param salt pre-generated salt
     * @return hashed password
     */
    public String createHash(String pass, String salt) {

        String hashText = "";

        try {

            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            byte[] hash = digest.digest((pass + salt).getBytes());

            BigInteger num = new BigInteger(1, hash);

            hashText = num.toString();

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error createHash(): \r\n" + e.getMessage());
        }

        return hashText;
    }

    /**
     * Updates password
     * @param customerID
     * @param passInput password
     * @return success of update
     */
    public boolean changePassword(int customerID, String passInput) {
        String salt = createSalt();
        String password = createHash(passInput, salt);
        
        try {
            String query = "UPDATE customers SET password=?, salt=? WHERE customerID=?";
            this.conn = establishConnection();
            try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
                stmt.setString(1, password);
                stmt.setString(2, salt);
                stmt.setInt(3, customerID);
                stmt.executeUpdate();
            }
            this.conn.close();
            
            return true;
        } catch (SQLException e) {
            System.out.println("Error changePassword(): \r\n" + e.getMessage());
            return false;
        }
    }
    
    /**
     * Checks whether password is correct.
     * @param email account email
     * @param passInput password input by user
     * @return 0: connection error \r\n 1: match \r\n 2: not a match
     */
    public int checkPassword(String email, String passInput) {
        try {
            String pass = "";
            String salt = "";
            String query = "SELECT password, salt FROM customers WHERE email=?";
            this.conn = establishConnection();
            try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    while(rs.next()) {
                        pass = rs.getString("password");
                        salt = rs.getString("salt");
                    }
                }
            }
            
            String hashFromInput = createHash(passInput, salt);
            if (hashFromInput.equals(pass)) {
                return 1;
            } else {
                return 2;
            }
            
        } catch (SQLException e) {
            System.out.println("Error checkPassword(): \r\n" + e.getMessage());
            return 0;
        }
    }

    /**
     * Gets one of each available movie in the machine
     *
     * @param id machineID
     * @return list of unique movies
     * @throws java.io.IOException
     */
    public ArrayList<Movie> getMachineCurrentMovies(int id) throws IOException {
        ArrayList<Movie> movies = new ArrayList<>();
        ArrayList<String> unique = new ArrayList<>();

        try {
            String query = "SELECT * FROM discs LEFT JOIN movies ON discs.movieID = movies.movieID WHERE discs.machine=" + id + ";";
            this.conn = establishConnection();
            try (Statement stmt = this.conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    if (!unique.contains(rs.getString("discs.movieID"))) {
                        unique.add(rs.getString("discs.movieID"));
                        movies.add(new Movie(rs.getString("discID"), rs.getString("title"), rs.getString("description"), rs.getString("ageRestriction"), true, rs.getString("thumbnail"), rs.getString("duration"), rs.getString("director")));
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
     * Creates a new row in the table rent and updates the current machine of
     * the movie to be rented - already sets 2.99 as paid for.
     *
     * @param discID
     * @param machine machineID (currently 1 to 6)
     * @param customer customerID
     * @return success of rent
     */
    public boolean rentMovie(String discID, int machine, Customer customer) {
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
            return true;
        } catch (SQLException e) {
            System.out.println("Error rentMovie(): \r\n" + e.getMessage());
            return false;
        }
    }

    /**
     * Gets current order of a disc
     *
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
                    while (rs.next()) {
                        order = new Order(rs.getInt("rentID"), rs.getInt("customerID"), rs.getInt("machineID"), rs.getString("discID"), rs.getString("status"), rs.getTimestamp("rentDate"), rs.getDouble("paidFor"));
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
     * Updates the status of an order to returned and sets the current location
     * of the disc to the current machine.
     *
     * @param order
     * @param machineID
     * @param furtherPayment how much more to be paid
     * @return success of return
     */
    public boolean returnMovie(Order order, int machineID, double furtherPayment) {
        furtherPayment += 2.99;
        
        try {
            String query = "UPDATE rent SET status=available, paidFor=" + furtherPayment + " WHERE rentID=" + order.getRentID() + "; UPDATE discs SET machine=" + machineID + " WHERE discID=" + order.getDiscID() + ";";
            this.conn = establishConnection();
            try (Statement stmt = this.conn.createStatement()) {
                stmt.executeUpdate(query);
            }
            this.conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error returnMovie(): \r\n" + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the status of the rent to paid.
     *
     * @param order
     * @return success of payment
     */
    public boolean payForMaxFee(Order order) {
        try {
            String query = "UPDATE rent SET status=paid, paidFor=17.99 WHERE rentID=" + order.getRentID() + ";";
            this.conn = establishConnection();
            try (Statement stmt = this.conn.createStatement()) {
                stmt.executeUpdate(query);
            }
            this.conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error payForMaxFee(): \r\n" + e.getMessage());
            return false;
        }
    }

    /**
     * Gets distinct entries for genres.
     *
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

    /**
     * Gets a copy of each movie based on the genre chosen.
     *
     * @param machine current machineID
     * @param genre genre chosen
     * @return list of movies of a given genre
     * @throws IOException
     */
    public ArrayList<Movie> getMoviesFromGenre(int machine, String genre) throws IOException {
        ArrayList<Movie> movies = new ArrayList<>();
        ArrayList<String> unique = new ArrayList<>();

        try {
            String query = "SELECT DISTINCT discs.movieID, discID, title, description, ageRestriction, thumbnail, duration, director FROM discs INNER JOIN movies ON discs.movieID = movies.movieID LEFT JOIN moviesGenre ON discs.movieID = moviesGenre.movieID WHERE genre=? AND machine=?;";
            this.conn = establishConnection();
            try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
                stmt.setString(1, query);
                stmt.setInt(2, machine);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        if (!unique.contains(rs.getString("discs.movieID"))) {
                            unique.add(rs.getString("discs.movieID"));
                            movies.add(new Movie(rs.getString("discID"), rs.getString("title"), rs.getString("description"), rs.getString("ageRestriction"), true, rs.getString("thumbnail"), rs.getString("duration"), rs.getString("director")));
                        }
                    }
                }
            }
            this.conn.close();
        } catch (SQLException e) {
            System.out.println("Error getMoviesFromGenre(): \r\n" + e.getMessage());
        }

        return movies;
    }

}
