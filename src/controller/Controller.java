/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import model.DBConnection.DBConnection;
import model.Validator;
import model.customer.Customer;
import model.movie.Movie;
import model.order.Order;
import view.frame.Frame;
import view.screens.HomeScreen;
import view.screens.MovieScreen;
import view.screens.RentHomescreens;
import view.screens.ReturnHomeScreem;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class Controller implements ActionListener {

    private Frame frame;
    private DBConnection conn;
    private ArrayList<Movie> movieList, selectedMovies;
    private ArrayList<Order> order;
    private int machineID;

    public Controller() {
        this.machineID = 1;
        this.order = new ArrayList<>();
        this.selectedMovies = new ArrayList<>();

        this.conn = new DBConnection();
        this.movieList = getMachineCurrentMoviesFromDB(this.machineID);
        Collections.sort(this.movieList, Movie.MovieTitleComparator); 
        this.frame = new Frame(this);
    }

    public ArrayList<Movie> getMovieList() {
        return movieList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().startsWith("Selected movie ")) {
            String[] a = e.getActionCommand().split(" ");
            goToMovieScreen(a[2]);
        }

        if (e.getActionCommand().startsWith("selected genre ")) {
            String[] a = e.getActionCommand().split(" ");
            changeGenreInMovieScreen(a[2]);
        }
        switch (e.getActionCommand()) {
            case "Go to main home screen":
                this.frame.changePanel(new HomeScreen(this));
                break;
            case "Go to rent home screem":  {
                if (this.machineID != Integer.parseInt(HomeScreen.getmachineSelect())) {
                    this.machineID = Integer.parseInt(HomeScreen.getmachineSelect());
                    this.movieList = getMachineCurrentMoviesFromDB(this.machineID);
                    Collections.sort(this.movieList, Movie.MovieTitleComparator);
                }
                this.frame.changePanel(new RentHomescreens(this, null));
            }
            break;

            case "Go to return home screem":
                this.frame.changePanel(new ReturnHomeScreem(this));

                break;

            default:
                System.out.println(e.getActionCommand());
                break;
        }
    }

    public ArrayList<Order> getOrder() {
        return order;
    }

    public int getMachineID() {
        return machineID;
    }

    public ArrayList< Movie> getMachineCurrentMoviesFromDB(int id) {

        return this.conn.getMachineCurrentMovies(id);
        
    }

    public Frame getFrame() {
        return this.frame;

    }

    /**
     * Closes the DB connection.
     */
    public void closeDBConnection() {
        this.conn.closeConnection();
    }

    public String[] getMachineIDs() {
        return this.conn.getMachineIDs();
    }

    public ArrayList<String> getUniqueGenres() {
        return this.conn.getUniqueGenres();
    }

    private void goToMovieScreen(String m) {
        this.movieList.stream().filter((movie) -> (movie.getId().equals(m))).forEachOrdered((Movie movie) -> {
            Controller.this.frame.changePanel(new MovieScreen(movie, Controller.this));
        });
    }

    private void changeGenreInMovieScreen(String g) {

        this.frame.changePanel(new RentHomescreens(this, g));

    }

    /**
     * Adds movie to the ArrayList selectedMovies. Handles the movie having already been added by the current user.
     * @param movieID movie to be added
     */
    private void addMovieToCart(String movieID) {
        Movie movie = null;
        for (Movie m : this.movieList) {
            if (m.getId().equals(movieID)) {
                movie = m;
                break;
            }
        }
        if (this.selectedMovies.contains(movie)) {
            JOptionPane.showMessageDialog(this.frame, "You've already added this movie to your cart.", "Oops...", JOptionPane.PLAIN_MESSAGE);
        } else {
            this.selectedMovies.add(movie);
        }
    }

    /**
     * Removes movie from the ArrayList selectedMovies.
     * @param movieID 
     */
    private void removeMovieFromCart(String movieID) {
        Movie movie = null;
        for (Movie m : this.movieList) {
            if (m.getId().equals(movieID)) {
                movie = m;
            }
        }
        this.selectedMovies.remove(movie);
    }

    /**
     * Creates order and inserts them into DB, clears cart and creates customer if non-existent.
     * Gets card number. If unknown, ask user if they want an account and proceed gathering that information to create a new user.
     * Sends receipt.
     */
    private void rentMovies() {
        String cardNumber = "";
        String email = "";
        String password = "";
        int currentMovies = selectedMovies.size();
        int totalMovies = selectedMovies.size();

        int tryAgain = JOptionPane.NO_OPTION;

        while (tryAgain == JOptionPane.NO_OPTION) {
            cardNumber = JOptionPane.showInputDialog(this.frame, "Please enter your card number", "Payment hub", JOptionPane.PLAIN_MESSAGE);
            // validates card number
            if (Validator.isValidCreditCard(cardNumber)) {
                Customer customer = this.conn.getCustomerFromCreditCard(cardNumber); // get customer from credit card number

                // if non existing customer, ask for profile creation if wanted
                if (customer == null) {

                    int wantsAccount = JOptionPane.showConfirmDialog(this.frame, "Would you like to create an account?", "Create account", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (wantsAccount == JOptionPane.YES_OPTION) {
                        while (!Validator.isValidEmail(email)) {
                            email = JOptionPane.showInputDialog(this.frame, "Please insert your email", "Email address", JOptionPane.PLAIN_MESSAGE);
                            if (!Validator.isValidEmail(email)) {
                                JOptionPane.showConfirmDialog(this.frame, "Please try a valid email address.", "Error", JOptionPane.PLAIN_MESSAGE);
                            }
                        }

                        String confirmPassword = " ";
                        while (!Validator.isValidPassword(password) && !password.equals(confirmPassword)) {
                            JPasswordField passField = new JPasswordField(10);
                            JLabel passLabel = new JLabel("Please enter your password");
                            Box passBox = Box.createHorizontalBox();
                            passBox.add(passLabel);
                            passBox.add(passField);
                            int pAnswer = JOptionPane.showConfirmDialog(this.frame, passBox, "Password", JOptionPane.PLAIN_MESSAGE);
                            if (pAnswer == JOptionPane.OK_OPTION) {
                                password = passField.getText();
                                if (!Validator.isValidPassword(password)) {
                                    JOptionPane.showConfirmDialog(this.frame, "Your password must contain at least 8 characters, one digit, one lower case letter, one upper case letter and a symbol.");
                                    continue;
                                }

                                JPasswordField confirm = new JPasswordField(10);
                                JLabel confirmLabel = new JLabel("Please confirm you password.");
                                Box confirmBox = Box.createHorizontalBox();
                                confirmBox.add(confirmLabel);
                                confirmBox.add(confirm);
                                int cAnswer = JOptionPane.showConfirmDialog(this.frame, confirmBox, "Confirm password", JOptionPane.PLAIN_MESSAGE);
                                if (cAnswer == JOptionPane.OK_OPTION) {
                                    confirmPassword = confirm.getText();
                                    if (!password.equals(confirmPassword)) {
                                        JOptionPane.showConfirmDialog(this.frame, "Your passwords don't match, please try again.");
                                    }
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                       // this.conn.createCustomer(cardNumber, email, password, currentMovies, totalMovies);
                    } else {
                       // this.conn.createCustomer(cardNumber, null, null, currentMovies, totalMovies);

                    }

                } else {
                    email = customer.getEmail();
                    currentMovies = customer.getCurrentMovies() + selectedMovies.size();
                    totalMovies = customer.getTotalMovies() + selectedMovies.size();
                }

                boolean success = true;
                for (Order o : this.order) {
                    // success = success && this.conn.rentMovie(o.getDiscID(), o.getMachineID(), o.getCustomerID());
                    // this.conn.updateCustomer(currentMovies, totalMovies);
                }
                if (success) {
                    int receipt = JOptionPane.showConfirmDialog(this.frame, "Your order was successful! Please don't forget to take your movies. Would you like your receipt?", "Thank you!", JOptionPane.YES_NO_OPTION);

                    if (receipt == JOptionPane.YES_OPTION) {
                        // ****** BOTAR CODIGO DE MANDAR EMAIL AQUI **********
                    } 
                    
                    this.order.removeAll(this.order);
                } else {
                    JOptionPane.showMessageDialog(this.frame, "There seems to be an issue with your order. Please try again.", "Oops...", JOptionPane.PLAIN_MESSAGE);
                }
            } else {
                if (cardNumber.equals("")) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(this.frame, "Please try a different card number", "Invalid card format", JOptionPane.ERROR_MESSAGE);
                }

            }
        }
    }

}
