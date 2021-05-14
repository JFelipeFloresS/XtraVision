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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.customer.Customer;
import model.customer.Loyalty;
import model.order.Order;

/**
 * @author Thyago De Oliveira Alves
 * @author José Felipe Flores da Silva
 */
public class DBConnection {

    private Controller controller;
    private final Connection CONNECTION;

    public DBConnection(Controller controller) {
        this.controller = controller;
        this.CONNECTION = establishConnection();
    }

    public DBConnection() {
        this.CONNECTION = establishConnection();
    }

    /**
     *
     * @return connection to DB
     */
    private Connection establishConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://apontejaj.com:3306/Felipe_2019405?useSSL=false", "Felipe_2019405", "2019405");
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Closes DB connection
     */
    public void closeConnection() {
        try {
            this.CONNECTION.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Gets all credit card numbers from a given customer
     *
     * @param id customerID
     * @return list of card numbers
     */
    public ArrayList<String> getCustomerCreditCards(int id) {
        ArrayList<String> cards = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT cardNumber FROM creditCards WHERE customerID=?";
            stmt = this.CONNECTION.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                cards.add(rs.getString("cardNumber"));
            }

        } catch (SQLException e) {
            System.out.println("Error getCustomerCreditCards(): \r\n" + e.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM creditCards INNER JOIN customers ON creditCards.customerID=customers.customerID WHERE cardNumber=?";
            stmt = this.CONNECTION.prepareStatement(query);
            stmt.setString(1, card);
            rs = stmt.executeQuery();
            while (rs.next()) {
                customer = new Customer(rs.getInt("customers.customerID"), rs.getString("email"), rs.getInt("currentMovies"), rs.getInt("totalMovies"), rs.getString("loyalty"));
            }

        } catch (SQLException e) {
            System.out.println("Error getCustomerFromCreditCard(): \r\n" + e.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return customer;
    }

    public Customer getCustomerFromEmailAdress(String email, String password) {
        if (checkPassword(email, password) != 1) {
            return null;
        } else {
            Customer customer = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String query = "SELECT * FROM customers WHERE email=?;";
                stmt = this.CONNECTION.prepareStatement(query);
                stmt.setString(1, email);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    customer = new Customer(rs.getInt("customers.customerID"), rs.getString("email"), rs.getInt("currentMovies"), rs.getInt("totalMovies"), rs.getString("loyalty"));
                }
            } catch (SQLException e) {
                System.out.println("Error getCustomerFromEmailAddress(): \r\n" + e.getMessage());
            } finally {
                try {
                    rs.close();
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            return customer;
        }
    }

    public Customer getCustomerFromID(String id) {
        Customer customer = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM customers WHERE customerID=?;";
            stmt = this.CONNECTION.prepareStatement(query);
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                customer = new Customer(rs.getInt("customers.customerID"), rs.getString("email"), rs.getInt("currentMovies"), rs.getInt("totalMovies"), rs.getString("loyalty"));
            }
        } catch (SQLException e) {
            System.out.println("Error getCustomerFromEmailAddress(): \r\n" + e.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return customer;
    }

    /**
     * Creates a customer
     *
     * @param card credit card number (not null)
     * @param email email address (if not input, set to null)
     * @param passInput password (if not input, set to null)
     * @param currentMovies movies currently rented
     * @param totalMovies all movies
     * @return success of creation
     */
    public boolean createCustomer(String card, String email, String passInput, int currentMovies, int totalMovies) {
        String salt = null;
        String password = null;

        if (passInput != null) {
            salt = createSalt();
            password = createHash(passInput, salt);
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = "INSERT INTO customers(email, currentMovies, totalMovies, loyalty, initialCreditCard, password, salt) VALUES(?, ?, ?, ?, ?, ?, ?);";
            stmt = this.CONNECTION.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setInt(2, currentMovies);
            stmt.setInt(3, totalMovies);
            if (password == null) {

                stmt.setString(4, Loyalty.NONE.getName());
            } else {
                stmt.setString(4, Loyalty.STANDARD.getName());
            }
            stmt.setString(5, card);
            stmt.setString(6, password);
            stmt.setString(7, salt);
            stmt.execute();
            stmt.close();

            query = "SELECT customerID FROM customers WHERE initialCreditCard=?;";
            stmt = this.CONNECTION.prepareStatement(query);
            stmt.setString(1, card);
            rs = stmt.executeQuery();
            int customerID = -1;
            while (rs.next()) {
                customerID = rs.getInt("customerID");
            }
            rs.close();
            stmt.close();

            query = "INSERT INTO creditCards(cardNumber, customerID) VALUES(?,?)";
            stmt = this.CONNECTION.prepareStatement(query);
            stmt.setString(1, card);
            stmt.setInt(2, customerID);
            stmt.execute();

            return true;
        } catch (SQLException e) {
            System.out.println("Error createCustomerFromCreditCard(): \r\n" + e.getMessage());
            return false;
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
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
     *
     * @param customerID
     * @param passInput password
     * @return success of update
     */
    public boolean changePassword(int customerID, String passInput) {
        String salt = createSalt();
        String password = createHash(passInput, salt);
        PreparedStatement stmt = null;

        try {
            String query = "UPDATE customers SET password=?, salt=? WHERE customerID=?";
            stmt = this.CONNECTION.prepareStatement(query);
            stmt.setString(1, password);
            stmt.setString(2, salt);
            stmt.setInt(3, customerID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error changePassword(): \r\n" + e.getMessage());
            return false;
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether password is correct.
     *
     * @param email account email
     * @param passInput password input by user
     * @return 0: connection error \r\n 1: match \r\n 2: not a match
     */
    public int checkPassword(String email, String passInput) {
        String pass = "";
        String salt = "";
        String hashFromInput = "";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT password, salt FROM customers WHERE email=?";
            stmt = this.CONNECTION.prepareStatement(query);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            while (rs.next()) {
                pass = rs.getString("password");
                salt = rs.getString("salt");
            }

            hashFromInput = createHash(passInput, salt);

        } catch (SQLException e) {
            System.out.println("Error checkPassword(): \r\n" + e.getMessage());
            return 0;
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (hashFromInput.equals(pass) && !hashFromInput.equals("")) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * Gets one of each available movie in the machine
     *
     * @param id machineID
     * @return list of unique movies
     */
    public ArrayList<Movie> getMachineCurrentMovies(int id) {
        ArrayList<Movie> movies = new ArrayList<>();
        ArrayList<String> unique = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT * FROM discs LEFT JOIN movies ON discs.movieID = movies.movieID WHERE discs.machine=" + id + ";";
            stmt = this.CONNECTION.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                if (!unique.contains(rs.getString("discs.movieID"))) {
                    unique.add(rs.getString("discs.movieID"));
                    movies.add(new Movie(rs.getString("discID"), rs.getString("title"), rs.getString("description"), rs.getString("ageRestriction"), true, rs.getString("thumbnail"), rs.getString("duration"), rs.getString("director")));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error getMachineCurrentMovies(): \r\n" + e.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        movies = addCategoriesToMovies(movies);
        return movies;
    }

    /**
     * Creates a new row in the table rent and updates the current machine of
     * the movie to be rented - already sets 2.99 as paid for.
     *
     * @param discID
     * @param machine machineID (currently 1 to 6)
     * @param customerID
     * @param paidFor
     * @return success of rent
     */
    public boolean rentMovie(String discID, int machine, int customerID, double paidFor) {
        PreparedStatement stmt = null;

        try {
            String query = "INSERT INTO rent(discID, customerID, machineID, status, paidFor) VALUES(?, ?, ?, 'rented', ?);";
            stmt = this.CONNECTION.prepareStatement(query);
            stmt.setString(1, discID);
            stmt.setInt(2, customerID);
            stmt.setInt(3, machine);
            stmt.setDouble(4, paidFor);
            stmt.executeUpdate();
            stmt.close();

            String query2 = "UPDATE discs SET machine=-1 WHERE discID=?;";
            stmt = this.CONNECTION.prepareStatement(query2);
            stmt.setString(1, discID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error rentMovie(): \r\n" + e.getMessage());
            return false;
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return true;
    }

    /**
     * Gets current order of a disc
     *
     * @param movieID
     * @return an order if rented or null if not currently rented
     */
    public Order getOrderFromMovieID(String movieID) {
        Order order = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT * FROM rent WHERE discID=? AND status='rented';";
            stmt = this.CONNECTION.prepareStatement(query);
            stmt.setString(1, movieID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                order = new Order(rs.getInt("rentID"), rs.getInt("customerID"), rs.getInt("machineID"), rs.getString("status"), rs.getDate("rentDate"), rs.getDouble("paidFor"), rs.getString("discID"));
            }

        } catch (SQLException e) {
            System.out.println("Error getOrderFromMovieID(): \r\n" + e.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        Statement stmt = null;
        ResultSet rs = null;
        int custID = 0;
        Customer customer = null;

        try {
            String query = "SELECT customerID, paidFor FROM rent WHERE rentID=" + order.getRentID() + ";";
            stmt = this.CONNECTION.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                custID = rs.getInt("customerID");
                furtherPayment += rs.getDouble("paidFor");
            }
            rs.close();
            stmt.close();

            query = "SELECT * FROM customers WHERE customerID=" + order.getCustomerID() + ";";
            stmt = this.CONNECTION.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                customer = new Customer(rs.getInt("customerID"));
            }

        } catch (SQLException e) {
            System.out.println("Error returnMovie(): \r\n" + e.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            String query = "UPDATE rent SET status='available', paidFor=" + furtherPayment + " WHERE rentID=" + order.getRentID() + ";";
            stmt = this.CONNECTION.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            query = "UPDATE discs SET machine=" + machineID + " WHERE discID='" + order.getMovie().getId() + "';";
            stmt = this.CONNECTION.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            int curr = customer.getCurrentMovies() - 1;
            query = "UPDATE customers SET currentMovies=" + curr + " WHERE customerID='" + customer.getId() + "';";
            stmt = this.CONNECTION.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Error returnMovie(): \r\n" + e.getMessage());
            return false;
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return true;
    }

    /**
     * Updates the status of the rent to paid.
     *
     * @param order
     * @return success of payment
     */
    public boolean payForMaxFee(Order order) {
        Statement stmt = null;
        double paidFor = order.getPaidFor();
        paidFor += 15.00;

        try {
            String query = "UPDATE rent SET status='paid', paidFor=" + paidFor + " WHERE rentID=" + order.getRentID() + ";";
            stmt = this.CONNECTION.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println("Error payForMaxFee(): \r\n" + e.getMessage());
            return false;
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return true;
    }

    /**
     * Gets distinct entries for genres.
     *
     * @return list of genres
     */
    public ArrayList<String> getUniqueGenres() {
        ArrayList<String> genres = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT DISTINCT genre FROM moviesGenre;";
            stmt = this.CONNECTION.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                genres.add(rs.getString("genre"));
            }

        } catch (SQLException e) {
            System.out.println("Error getUniqueGenres(): \r\n" + e.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return genres;
    }

    /**
     * Gets a copy of each movie based on the genre chosen.
     *
     * @param machine current machineID
     * @param genre genre chosen
     * @return list of movies of a given genre
     */
    public ArrayList<Movie> getMoviesFromGenre(int machine, String genre) {
        ArrayList<Movie> movies = new ArrayList<>();
        ArrayList<String> unique = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT DISTINCT discs.movieID, discID, title, description, ageRestriction, thumbnail, duration, director FROM discs INNER JOIN movies ON discs.movieID = movies.movieID LEFT JOIN moviesGenre ON discs.movieID = moviesGenre.movieID WHERE genre=? AND machine=?;";
            stmt = this.CONNECTION.prepareStatement(query);
            stmt.setString(1, query);
            stmt.setInt(2, machine);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (!unique.contains(rs.getString("discs.movieID"))) {
                    unique.add(rs.getString("discs.movieID"));
                    movies.add(new Movie(rs.getString("discID"), rs.getString("title"), rs.getString("description"), rs.getString("ageRestriction"), true, rs.getString("thumbnail"), rs.getString("duration"), rs.getString("director")));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error getMoviesFromGenre(): \r\n" + e.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return movies;
    }

    private ArrayList<Movie> addCategoriesToMovies(ArrayList<Movie> movies) {
        ArrayList<Movie> newMovies = movies;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT * FROM moviesGenre;";
            stmt = this.CONNECTION.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString("movieID");
                String genre = rs.getString("genre");
                for (Movie m : newMovies) {
                    if (m.getId().substring(0, m.getId().length() - 2).equals(id)) {
                        m.addCategory(genre);
                        break;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error addCategoriesToMovies(): \r\n" + e.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return newMovies;
    }

    public String[] getMachineIDs() {
        String[] machines;
        Statement stmt = null;
        ResultSet rs = null;
        int i = 0;
        try {
            String query = "SELECT DISTINCT machine FROM discs;";
            stmt = this.CONNECTION.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                i++;
            }

        } catch (SQLException e) {
            System.out.println("Error getmachineIDs(): \r\n" + e.getMessage());
        }

        machines = new String[i];
        for (int j = 0; j < machines.length; j++) {
            machines[j] = "" + (j + 1);
        }
        return machines;
    }

    public boolean updateCustomerNumberOfMovies(int currentMovies, int totalMovies, int customerID) {
        Statement stmt = null;
        try {
            String query = "UPDATE customers SET currentMovies=" + currentMovies + ", totalMovies=" + totalMovies + " WHERE customerID=" + customerID + ";";
            stmt = this.CONNECTION.createStatement();
            stmt.executeUpdate(query);

            stmt.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updateCustomerNumberOfMovies(): \r\n" + e.getMessage());
            return false;
        }
    }

    public boolean addCardNumberToCustomer(String card) {
        PreparedStatement stmt = null;

        try {

            String query = "INSERT INTO creditCards(cardNumber, customerID) VALUES(?,?)";

            stmt = this.CONNECTION.prepareStatement(query);
            stmt.setString(1, card);
            stmt.setInt(2, this.controller.getCurrentCustomer().getId());

            stmt.execute();

            return true;
        } catch (SQLException e) {
            System.out.println("Error addCardNumberToCustomer(): \r\n" + e.getMessage());
            return false;
        }
    }

    public boolean updateCustomerEmail(String email) {
        PreparedStatement stmt = null;

        try {

            String query = "UPDATE customers SET email=? WHERE customerID=" + this.controller.getCurrentCustomer().getId() + ";";
            stmt = this.CONNECTION.prepareStatement(query);
            stmt.setString(1, email);

            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error updateCustomerEmail(): \r\n" + e.getMessage());
            return false;
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean updateCustomerPassword(String passwordInput) {
        String password = null;
        String salt = null;

        if (passwordInput != null) {
            salt = createSalt();
            password = createHash(passwordInput, salt);
        }
        PreparedStatement stmt = null;

        try {

            String query = "UPDATE customers SET password=?, salt=? WHERE customerID=" + this.controller.getCurrentCustomer().getId() + ";";
            stmt = this.CONNECTION.prepareStatement(query);
            stmt.setString(1, password);
            stmt.setString(2, salt);

            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error updateCustomerEmail(): \r\n" + e.getMessage());
            return false;
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public ArrayList<Order> getCustomerRentedMovies(String cardNumber) {
        Customer customer = getCustomerFromCreditCard(cardNumber);
        ArrayList<Order> rented = new ArrayList<>();

        Statement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT * FROM rent WHERE customerID=" + customer.getId() + " AND status='rented';";
            stmt = this.CONNECTION.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                rented.add(getOrderFromMovieID(rs.getString("discID")));
            }
        } catch (SQLException e) {
            System.out.println("Error getCustomerRentedMovies(): \r\n" + e.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return rented;
    }

    public Movie getMovieFromID(String id) {
        Movie movie = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM discs INNER JOIN movies ON discs.movieID=movies.movieID WHERE discID=?";
            stmt = this.CONNECTION.prepareStatement(query);
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            while(rs.next()) {
                movie = new Movie(rs.getString("discID"),rs.getString("title"),null);
            }
            
        } catch (SQLException e) {
            System.out.println("Error getMovieFromID(): \r\n" + e.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return movie;
    }
}
