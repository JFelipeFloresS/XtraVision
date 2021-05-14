/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import model.DBConnection.DBConnection;
import model.Validator;
import model.customer.Customer;
import model.movie.Movie;
import model.order.Order;
import view.frame.Frame;
import view.screens.CheckOut;
import view.screens.HomeScreen;
import view.screens.MovieScreen;
import view.screens.RentHomescreens;
import view.screens.ReturnHomeScreem;

/**
 * @author Thyago De Oliveira Alves
 * @author José Felipe Flores da Silva
 */
public class Controller implements ActionListener {

    private final double MOVIE_PRICE = 2.99;
    private final double LATE_DAY_PRICE = 1.5;
    private final String FREE_CODE = "FREEMOVIE";
    private final Frame frame;
    private final DBConnection conn;
    private ArrayList<Movie> movieList;
    private final ArrayList<Movie> selectedMovies;
    private int machineID;
    private Customer currentCustomer;

    public Controller() {
        this.machineID = 1;
        this.selectedMovies = new ArrayList<>();
        this.currentCustomer = null;

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

        if (e.getActionCommand().startsWith("rent ")) {
            String[] a = e.getActionCommand().split(" ");
            addMovieToCart(a[1]);
        }

        if (e.getActionCommand().startsWith("remove movie ")) {
            String[] a = e.getActionCommand().split(" ");
            removeMovieFromCart(a[2]);
        }
        switch (e.getActionCommand()) {
            case "Go to main home screen":
                resetSession();
                this.frame.changePanel(new HomeScreen(this));
                break;
            case "Go to rent home screem": {
                if (this.machineID != Integer.parseInt(HomeScreen.getmachineSelect())) {
                    this.machineID = Integer.parseInt(HomeScreen.getmachineSelect());
                    this.movieList = getMachineCurrentMoviesFromDB(this.machineID);
                    Collections.sort(this.movieList, Movie.MovieTitleComparator);
                }
                this.frame.changePanel(new RentHomescreens(this, null));
            }
            break;

            case "Go to return home screem":
                this.conn.getCustomerRentedMovies("4000400040004000");
                this.frame.changePanel(new ReturnHomeScreem(this));

                break;

            case "log in":
                logIn();
                break;
                
            case "log out":
                resetSession();
                break;

            case "return DVD":
                returnDVD(ReturnHomeScreem.getReturnIDInput());
                break;

            case "return DVD using card":
                returnUsingCard();
                break;

            case "Go to check out":
                rentMovies();
                break;

            case "Check valid order":
                if (this.selectedMovies.size() > 0) {
                    this.frame.changePanel(new CheckOut(this));
                } else {
                    JOptionPane.showMessageDialog(this.frame, "Please add a movie to your cart before proceeding to checkout.", "Oops...", JOptionPane.PLAIN_MESSAGE);
                }
                break;

            default:
                //System.out.println(e.getActionCommand());
                break;
        }
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
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

    public ArrayList<Movie> getSelectedMovies() {
        return selectedMovies;
    }

    /**
     * Adds movie to the ArrayList selectedMovies. Handles the movie having
     * already been added by the current user.
     *
     * @param movieID movie to be added
     */
    private void addMovieToCart(String movieID) {
        if (this.selectedMovies.size() >= 4) {
            JOptionPane.showMessageDialog(this.frame, "You can only rent four movies at a time.", "Movie limit", JOptionPane.PLAIN_MESSAGE);
            this.frame.changePanel(new RentHomescreens(this, null));
            return;
        } else if (this.selectedMovies.size() == 2 && this.currentCustomer == null) {
            int c = JOptionPane.showConfirmDialog(this.frame, "Only two movies are allowed for first time users. If you're using Xtra-Vision for the first time, you might have to remove some movies from your cart later. Would you like the movie to the cart anyway?", "Movie limit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (c == JOptionPane.NO_OPTION) {
                return;
            }
        }

        Movie movie = null;
        for (Movie m : this.movieList) {
            if (m.getId().equals(movieID)) {
                movie = m;
                break;
            }
        }
        if (this.selectedMovies.contains(movie)) {
            JOptionPane.showMessageDialog(this.frame, "You've already added " + movie.getTitle() + " to your cart.", "Oops...", JOptionPane.PLAIN_MESSAGE);
            this.frame.changePanel(new RentHomescreens(this, null));
        } else {
            JOptionPane.showMessageDialog(this.frame, "You've added " + movie.getTitle() + " to your cart.", "Woop!!", JOptionPane.PLAIN_MESSAGE);
            this.selectedMovies.add(movie);
            this.frame.changePanel(new RentHomescreens(this, null));
        }
    }

    /**
     * Removes movie from the ArrayList selectedMovies.
     *
     * @param movieID
     */
    private void removeMovieFromCart(String movieID) {
        Movie movie = null;
        for (Movie m : this.movieList) {
            if (m.getId().equals(movieID)) {
                movie = m;
                break;
            }
        }
        this.selectedMovies.remove(movie);
        this.frame.changePanel(new CheckOut(this));
        JOptionPane.showMessageDialog(this.frame, "You removed " + movie.getTitle() + " successfully!", "Movie removed", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Creates order and inserts them into DB, clears cart and creates customer
     * if non-existent. Gets card number. If unknown, ask user if they want an
     * account and proceed gathering that information to create a new user.
     * Sends receipt.
     */
    private void rentMovies() {
        if (this.currentCustomer != null) {
            rentMoviesLoggedIn();
            return;
        }

        String cardNumber = "";
        String email = "";
        String password = "";
        int currentMovies = selectedMovies.size();
        int totalMovies = selectedMovies.size();
        int limitOfMovies = 2;
        boolean isFirstFree = false;

        int tryAgain = JOptionPane.NO_OPTION;

        while (tryAgain == JOptionPane.NO_OPTION) {
            cardNumber = JOptionPane.showInputDialog(this.frame, "Please enter your card number", "Payment hub", JOptionPane.PLAIN_MESSAGE);
            if (cardNumber == null || cardNumber.equals("")) {
                return;
            }
            // validates card number
            if (Validator.isValidCreditCard(cardNumber)) {
                Customer customer = this.conn.getCustomerFromCreditCard(cardNumber); // get customer from credit card number

                // if non existing customer, ask for profile creation if wanted
                if (customer == null) {

                    customer = new Customer(cardNumber, currentMovies, totalMovies);

                    // 
                    if (this.selectedMovies.size() > limitOfMovies) {
                        JOptionPane.showMessageDialog(this.frame, "New customers can only rent " + limitOfMovies + " movies at a time. "
                                + "Please remove " + (this.selectedMovies.size() - limitOfMovies) + " from your cart before proceeding to payment.", "Oops...", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                   

                    JLabel freeLabel = new JLabel("Please enter code");
                    freeLabel.setForeground(Color.WHITE);
                    JTextField freeField = new JTextField(16);
                    freeField.setFont(new Font("DIALOG", Font.PLAIN, 30));
                    Box freeBox = Box.createVerticalBox();
                    freeBox.add(freeLabel);
                    freeBox.add(freeField);

                    int freeChoice = JOptionPane.showConfirmDialog(this.frame, freeBox, FREE_CODE, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (freeChoice == JOptionPane.YES_OPTION) {
                        if (freeField.getText().equalsIgnoreCase(FREE_CODE)) {
                            isFirstFree = true;
                            JOptionPane.showMessageDialog(this.frame, "Your order is free of charge, on us!", FREE_CODE, JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this.frame, "Invalid code", FREE_CODE, JOptionPane.PLAIN_MESSAGE);
                            return;
                        }
                    }

                    int wantsAccount = JOptionPane.showConfirmDialog(this.frame, "Would you like to create an account?", "Create account", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (wantsAccount == JOptionPane.YES_OPTION) {
                        while (!Validator.isValidEmail(email)) {
                            email = JOptionPane.showInputDialog(this.frame, "Please insert your email", "Email address", JOptionPane.PLAIN_MESSAGE);
                            if (!Validator.isValidEmail(email)) {
                                JOptionPane.showConfirmDialog(this.frame, "Please try a valid email address.", "Error", JOptionPane.PLAIN_MESSAGE);
                            }
                        }

                        customer.setEmail(email);

                        String confirmPassword = " ";
                        while (!Validator.isValidPassword(password) && !password.equals(confirmPassword)) {
                            JPasswordField passField = new JPasswordField(10);
                            passField.setFont(new Font("DIALOG", Font.PLAIN, 30));
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
                                confirm.setFont(new Font("DIALOG", Font.PLAIN, 30));
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
                        this.conn.createCustomer(cardNumber, email, password, currentMovies, totalMovies);
                    } else {
                        this.conn.createCustomer(cardNumber, null, null, currentMovies, totalMovies);

                    }

                } else {
                    email = customer.getEmail();
                    currentMovies = customer.getCurrentMovies();
                    totalMovies = customer.getTotalMovies();
                }

                customer = this.conn.getCustomerFromCreditCard(cardNumber);

                 // 
                    if (customer.getCurrentMovies() + this.selectedMovies.size() > limitOfMovies) {
                        JOptionPane.showMessageDialog(this.frame, "New customers can only rent " + limitOfMovies + " movies at a time. "
                                + "Please remove " + (this.selectedMovies.size() - limitOfMovies) + " from your cart before proceeding to payment.", "Oops...", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    
                double totalPrice = 0.0;
                for (Movie m : this.selectedMovies) {
                    if (!isFirstFree) {
                        totalPrice += MOVIE_PRICE;
                    }
                }

                int pay = JOptionPane.showConfirmDialog(this.frame, "Your order will total €" + totalPrice + "\r\n Proceed with payment?", "TOTAL", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (pay == JOptionPane.NO_OPTION) {
                    return;
                }

                JOptionPane.showInputDialog(this.frame, "Mock payment card PIN required (won't be checked now).", "Enter PIN", JOptionPane.PLAIN_MESSAGE);

                for (Movie m : this.selectedMovies) {
                    double price;
                    if (isFirstFree) {
                        price = 0.0;
                    } else {
                        price = MOVIE_PRICE;
                    }
                    if (this.conn.rentMovie(m.getId(), this.getMachineID(), customer.getId(), price)) {

                        currentMovies += 1;
                        totalMovies += 1;
                    }
                }
                this.conn.updateCustomerNumberOfMovies(currentMovies, totalMovies, customer.getId());
                int receipt = JOptionPane.showConfirmDialog(this.frame, "Your order was successful! Please don't forget to take your movies. Would you like your receipt?", "Thank you!", JOptionPane.YES_NO_OPTION);

                if (receipt == JOptionPane.YES_OPTION) {
                    // ****** BOTAR CODIGO DE MANDAR EMAIL AQUI **********
                }

                resetSession();
                return;
            } else {
                if (cardNumber.equals("")) {
                    return;
                } else {
                    JOptionPane.showMessageDialog(this.frame, "Please try a different card number", "Invalid card format", JOptionPane.ERROR_MESSAGE);
                }

            }
        }
    }

    private void rentMoviesLoggedIn() {
        int limitOfMovies = 4;
        String cardNumber = "";
        boolean isNextFree = false;

        int tryAgain = JOptionPane.NO_OPTION;

        while (tryAgain == JOptionPane.NO_OPTION) {
            cardNumber = JOptionPane.showInputDialog(this.frame, "Please enter your card number", "Payment hub", JOptionPane.PLAIN_MESSAGE);
            if (cardNumber == null || cardNumber.equals("")) {
                return;
            }
            // validates card number
            if (Validator.isValidCreditCard(cardNumber)) {
                if (!this.currentCustomer.getCreditCards().contains(cardNumber)) {
                    this.currentCustomer.addCreditCard(cardNumber);
                }

                // 
                if (this.getCurrentCustomer().getCurrentMovies() + this.selectedMovies.size() > limitOfMovies) {
                    JOptionPane.showMessageDialog(this.frame, "Customers can only rent " + limitOfMovies + " movies at a time. \r\n"
                            + "Please remove " + (this.getCurrentCustomer().getCurrentMovies() - limitOfMovies) + " movie(s) from your cart before proceeding to payment. \r\nOr return the movies you've already rented.", "Oops...", JOptionPane.PLAIN_MESSAGE);
                    return;
                }

                if (this.currentCustomer.getEmail() == null) {
                    String email = "";
                    String password = "";
                    int wantsAccount = JOptionPane.showConfirmDialog(this.frame, "Would you like to add an email to your account?", "Add email", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (wantsAccount == JOptionPane.YES_OPTION) {
                        while (!Validator.isValidEmail(email)) {
                            email = JOptionPane.showInputDialog(this.frame, "Please insert your email", "Email address", JOptionPane.PLAIN_MESSAGE);
                            if (!Validator.isValidEmail(email)) {
                                JOptionPane.showConfirmDialog(this.frame, "Please try a valid email address.", "Error", JOptionPane.PLAIN_MESSAGE);
                            }
                        }

                        this.currentCustomer.setEmail(email);

                        String confirmPassword = " ";
                        while (!Validator.isValidPassword(password) && !password.equals(confirmPassword)) {
                            JPasswordField passField = new JPasswordField(10);
                            passField.setFont(new Font("DIALOG", Font.PLAIN, 30));
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
                                confirm.setFont(new Font("DIALOG", Font.PLAIN, 30));
                                JLabel confirmLabel = new JLabel("Please confirm you password.");
                                Box confirmBox = Box.createHorizontalBox();
                                confirmBox.add(confirmLabel);
                                confirmBox.add(confirm);
                                int cAnswer = JOptionPane.showConfirmDialog(this.frame, confirmBox, "Confirm password", JOptionPane.PLAIN_MESSAGE);
                                if (cAnswer == JOptionPane.OK_OPTION) {
                                    confirmPassword = confirm.getText();
                                    if (!password.equals(confirmPassword)) {
                                        JOptionPane.showConfirmDialog(this.frame, "Your passwords don't match, please try again.");
                                        continue;
                                    }
                                    this.conn.updateCustomerPassword(password);
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }

                    }

                    this.conn.updateCustomerEmail(email);
                }

                boolean success = true;
                double totalPrice = 0.0;

                for (Movie m : this.selectedMovies) {
                    if (!isNextFree) {
                        totalPrice += MOVIE_PRICE;
                    }
                }

                int proceed = JOptionPane.showConfirmDialog(this.frame, "Your order will total €" + totalPrice + "\r\n Proceed with payment?", "TOTAL", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (proceed == JOptionPane.NO_OPTION) {
                    return;
                }

                JOptionPane.showInputDialog(this.frame, "Mock payment card PIN required (won't be checked now).", "Enter PIN", JOptionPane.PLAIN_MESSAGE);

                for (Movie m : this.selectedMovies) {
                    double price;
                    if (isNextFree) {
                        price = 0.0;
                    } else {
                        price = MOVIE_PRICE;
                    }

                    if (this.conn.rentMovie(m.getId(), this.getMachineID(), this.getCurrentCustomer().getId(), price)) {
                        this.currentCustomer.addMoviesRented(1);

                    }

                }

                this.conn.updateCustomerNumberOfMovies(this.getCurrentCustomer().getCurrentMovies(), this.getCurrentCustomer().getTotalMovies(), this.getCurrentCustomer().getId());
                if (success) {
                    int receipt = JOptionPane.showConfirmDialog(this.frame, "Your order was successful! Please don't forget to take your movies. Would you like your receipt?", "Thank you!", JOptionPane.YES_NO_OPTION);

                    if (receipt == JOptionPane.YES_OPTION) {
                        // ****** BOTAR CODIGO DE MANDAR EMAIL AQUI **********
                    }

                    resetSession();
                    return;
                } else {
                    JOptionPane.showMessageDialog(this.frame, "There seems to be an issue with your order. Please try again.", "Oops...", JOptionPane.PLAIN_MESSAGE);
                }
            } else {
                if (cardNumber.equals("")) {
                    return;
                } else {
                    JOptionPane.showMessageDialog(this.frame, "Please try a different card number", "Invalid card format", JOptionPane.ERROR_MESSAGE);
                }

            }
        }
    }

    /**
     * Login pop-up for user to log in using either a credit card or email and
     * password.
     */
    private void logIn() {
        String[] loginOptions = {"CARD", "EMAIL AND PASSWORD"};
        int loginType = JOptionPane.showOptionDialog(this.frame, "What type of login would you like?", "Log in type", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, loginOptions, loginOptions[0]);
        if (loginType == 0) {
            String cardNumber = "";
            while (!Validator.isValidCreditCard(cardNumber)) {
                JLabel cardLabel = new JLabel("Please insert your card number");
                cardLabel.setForeground(Color.WHITE);
                JTextField cardField = new JTextField(16);
                cardField.setFont(new Font("DIALOG", Font.PLAIN, 30));
                Box cardBox = Box.createVerticalBox();
                cardBox.add(cardLabel);
                cardBox.add(cardField);
                int a = JOptionPane.showConfirmDialog(this.frame, cardBox, "Credit card login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
                if (a == JOptionPane.CANCEL_OPTION) {
                    return;
                } else {
                    cardNumber = cardField.getText();
                }
                if (!Validator.isValidCreditCard(cardNumber)) {
                    JOptionPane.showMessageDialog(this.frame, "Please enter a valid credit card number.", "Oops...", JOptionPane.PLAIN_MESSAGE);
                }
            }

            this.currentCustomer = this.conn.getCustomerFromCreditCard(cardNumber);
            if (this.currentCustomer == null) {
                JOptionPane.showMessageDialog(this.frame, "This card number isn't in our system.", "Not found", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this.frame, "Successfully logged in", "Good to see you!", JOptionPane.PLAIN_MESSAGE);
                this.frame.changePanel(new RentHomescreens(this, null));
            }

        } else {
            String email = "";
            String password = "";
            int check = 0;
            while (check != 1) {
                JLabel emailLabel = new JLabel("Email:");
                emailLabel.setForeground(Color.WHITE);
                JTextField emailField = new JTextField(20);
                emailField.setFont(new Font("DIALOG", Font.PLAIN, 30));
                JLabel passLabel = new JLabel("Password:");
                passLabel.setForeground(Color.WHITE);
                JPasswordField passField = new JPasswordField(20);
                passField.setFont(new Font("DIALOG", Font.PLAIN, 30));
                Box logBox = Box.createVerticalBox();
                logBox.add(emailLabel);
                logBox.add(emailField);
                logBox.add(passLabel);
                logBox.add(passField);
                int a = JOptionPane.showConfirmDialog(this.frame, logBox, "Log in", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (a == JOptionPane.CANCEL_OPTION) {
                    return;
                } else {
                    email = emailField.getText();
                    password = passField.getText();
                    check = this.conn.checkPassword(email, password);
                }

                switch (check) {
                    case 0:
                        JOptionPane.showMessageDialog(this.frame, "Connection error, please inform a staff member.", "Oops...", JOptionPane.PLAIN_MESSAGE);
                        resetSession();
                        break;
                    case 1:
                        this.currentCustomer = this.conn.getCustomerFromEmailAdress(email, password);
                        JOptionPane.showMessageDialog(this.frame, "Successfully logged in", "Good to see you!", JOptionPane.PLAIN_MESSAGE);
                        this.frame.changePanel(new RentHomescreens(this, null));
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(this.frame, "Invalid email or password.", "Oops...", JOptionPane.PLAIN_MESSAGE);
                        break;
                }

            }
        }
    }

    /**
     * Resets the currentCustomer, selectMovies list and order list, then
     * returns the user to the home screen.
     */
    private void resetSession() {
        this.selectedMovies.removeAll(this.selectedMovies);
        this.currentCustomer = null;
        this.frame.changePanel(new HomeScreen(this));
    }

    private void returnDVD(String id) {
        Order o = this.conn.getOrderFromMovieID(id);
        if (o == null) {
            JOptionPane.showMessageDialog(this.frame, "Please try a different disc ID.", "Oops...", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        this.currentCustomer = this.conn.getCustomerFromID(id);
        double priceToBePaid = Movie.getLateReturnPrice(o.getMovie());
        if (priceToBePaid > 0) {
            payForLateReturn(o, priceToBePaid);
            return;
        } else {
            if (this.conn.returnMovie(o, this.machineID, 0.0)) {
                JOptionPane.showMessageDialog(this.frame, "Please insert yout DVD. No further charges are required.", "Thank you!", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this.frame, "Please try again. There has been a problem with your return.", "Thank you!", JOptionPane.PLAIN_MESSAGE);
            }
        }

        resetSession();
    }

    private void payForLateReturn(Order o, double price) {
        JLabel paidLabel = new JLabel("Your late return will total €" + price + ".\r\nPlease enter your card PIN (Mock PIN, won't be checked).");
        paidLabel.setForeground(Color.WHITE);
        JTextField paidField = new JTextField(4);
        Box paidBox = Box.createVerticalBox();
        paidBox.add(paidLabel);
        paidBox.add(paidField);
        int paid = JOptionPane.showConfirmDialog(this.frame, paidBox, "Total late return price.", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (paid == JOptionPane.CANCEL_OPTION) {
            return;
        }
        
        if ((LATE_DAY_PRICE * 10) <= price) {
            this.conn.payForMaxFee(o);
            JOptionPane.showMessageDialog(this.frame, "You can keep your disc, no need to return it.", "Keep your movie", JOptionPane.PLAIN_MESSAGE);
        } else {
            this.conn.returnMovie(o, this.machineID, price);
            JOptionPane.showMessageDialog(this.frame, "Please return your disc.", "Return movie", JOptionPane.PLAIN_MESSAGE);
        }
        
        resetSession();
    }

    private void returnUsingCard() {
        JLabel cardLabel = new JLabel("Please insert your card number.");
        cardLabel.setForeground(Color.WHITE);
        JTextField cardField = new JTextField(16);
        cardField.setFont(new Font("DIALOG", Font.PLAIN, 30));
        Box cardBox = Box.createVerticalBox();
        cardBox.add(cardLabel);
        cardBox.add(cardField);
        int answer = JOptionPane.showConfirmDialog(this.frame, cardBox, "Return using credit card", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (answer == JOptionPane.CANCEL_OPTION) {
            return;
        }
        if (cardField.getText().equals("")) {
            JOptionPane.showMessageDialog(this.frame, "Please enter a card number and try again.", "Oops...", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        if (answer == JOptionPane.OK_OPTION) {
            String cardNum = cardField.getText();
            if (!Validator.isValidCreditCard(cardNum)) {
                JOptionPane.showMessageDialog(this.frame, "Invalid card number, please try again.", "Oops...", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            ArrayList<Order> currentRented = this.conn.getCustomerRentedMovies(cardNum);
            if (currentRented == null || currentRented.isEmpty()) {
                JOptionPane.showMessageDialog(this.frame, "No rented movies found, please try again.", "Oops...", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            Movie[] movies = new Movie[currentRented.size()];
            for (int i = 0; i < movies.length; i++) {
                movies[i] = this.conn.getMovieFromID(currentRented.get(i).getMovie().getId());
            }

            int movieToReturn = JOptionPane.showOptionDialog(this.frame, "Which movie would you like to return?", "Return movie", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, movies, movies[0]);
            returnDVD(movies[movieToReturn].getId());
        }
    }

}
