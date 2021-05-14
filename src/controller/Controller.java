package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    private String currentGenre;

    /**
     * Constructor
     */
    public Controller() {
        this.machineID = 1;
        this.selectedMovies = new ArrayList<>();
        this.currentCustomer = null;
        this.currentGenre = null;
        this.conn = new DBConnection(this);
        this.movieList = getMachineCurrentMoviesFromDB(this.machineID);
        Collections.sort(this.movieList, Movie.MovieTitleComparator);
        this.frame = new Frame(this);
    }

    public ArrayList<Movie> getMovieList() {
        return movieList;
    }

    /**
     * Handles the action performed for all buttons
     *
     * @param e action event from button
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // When a specific ID is passed through the action command, handle it by split the string and sending only what's needed to the method
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

        // Unspecific commands are handled through this main switch
        switch (e.getActionCommand()) {

            case "Go to main home screen":
                resetSession();
                break;

            case "Go to rent home screem": {
                // if the currently selected movie list comes from the machine that's selected from the combo box, there's no need to retrieve all the information again
                if (this.machineID != Integer.parseInt(HomeScreen.getmachineSelect())) {
                    this.machineID = Integer.parseInt(HomeScreen.getmachineSelect());
                    this.movieList = getMachineCurrentMoviesFromDB(this.machineID);
                    Collections.sort(this.movieList, Movie.MovieTitleComparator);
                }

                this.frame.changePanel(new RentHomescreens(this, this.currentGenre));
            }
            break;

            case "Go to return home screem":
                this.frame.changePanel(new ReturnHomeScreem(this));
                break;

            case "log in":
                logIn();
                break;

            case "log out":
                resetSession();
                break;

            case "return DVD using card":
                returnUsingCard();
                break;

            case "Go to check out":
                rentMovies();
                break;

            case "Check valid order":
                if (this.selectedMovies.size() > 0) { // if the cart is empty, the user can't go into the checkout area
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

    /**
     *
     * @param id machine ID
     * @return a list of one of each movie there is in the machine
     */
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

    /**
     *
     * @return array with all machine ID's
     */
    public String[] getMachineIDs() {
        return this.conn.getMachineIDs();
    }

    /**
     *
     * @return list of distinct genres from the database
     */
    public ArrayList<String> getUniqueGenres() {
        return this.conn.getUniqueGenres();
    }

    /**
     * Redirects user to a screen with further details about a specific movie
     *
     * @param m the disc ID
     */
    private void goToMovieScreen(String m) {
        this.movieList.stream().filter((movie) -> (movie.getId().equals(m))).forEachOrdered((Movie movie) -> {
            Controller.this.frame.changePanel(new MovieScreen(movie, this));
        });
    }

    /**
     * Changes what movies are displayed based on a genre
     *
     * @param g genre to be displayed
     */
    private void changeGenreInMovieScreen(String g) {
        this.currentGenre = g;
        this.frame.changePanel(new RentHomescreens(this, this.currentGenre));
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
            this.frame.changePanel(new RentHomescreens(this, this.currentGenre));
            return;
        } else if (this.selectedMovies.size() == 2 && this.currentCustomer == null) {
            int c = JOptionPane.showConfirmDialog(this.frame, "Only two movies are allowed for first time users.\r\nIf you're using Xtra-Vision for the first time, you might have to remove some movies from your cart later.\r\nWould you like the movie to the cart anyway?", "Movie limit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
            this.frame.changePanel(new RentHomescreens(this, this.currentGenre));
        } else {
            JOptionPane.showMessageDialog(this.frame, "You've added " + movie.getTitle() + " to your cart.", "Woop!!", JOptionPane.PLAIN_MESSAGE);
            this.selectedMovies.add(movie);
            this.frame.changePanel(new RentHomescreens(this, this.currentGenre));
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
        if (this.selectedMovies.size() > 0) {
            this.frame.changePanel(new CheckOut(this));
        } else {
            this.frame.changePanel(new RentHomescreens(this, this.currentGenre));
        }
        JOptionPane.showMessageDialog(this.frame, "You removed " + movie.getTitle() + " successfully!", "Movie removed", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Looks for an account based on the credit card number, if no account is
     * found, ask the customer if they want to create one and handle the account
     * creation. Adjust the price, create rent row in DB and update the customer
     * row.
     */
    private void rentMovies() {
        if (this.currentCustomer != null) {
            rentMoviesLoggedIn(null);
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

                    // limits number of movies a new user can rent at a time
                    if (this.selectedMovies.size() >= limitOfMovies) {
                        JOptionPane.showMessageDialog(this.frame, "New customers can only rent " + limitOfMovies + " movies at a time.\r\n"
                                + "Please remove " + (this.selectedMovies.size() - limitOfMovies) + " from your cart before proceeding to payment.", "Oops...", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }

                    // FREEMOVIE handling
                    JLabel freeLabel = new JLabel("Please enter FREEMOVIE code to get your first order free!");
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

                    // Account creation
                    int wantsAccount = JOptionPane.showConfirmDialog(this.frame, "Would you like to create an account?", "Create account", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (wantsAccount == JOptionPane.YES_OPTION) {
                        // Email address
                        while (!Validator.isValidEmail(email)) {
                            email = JOptionPane.showInputDialog(this.frame, "Please insert your email", "Email address", JOptionPane.PLAIN_MESSAGE);
                            if (!Validator.isValidEmail(email)) {
                                JOptionPane.showConfirmDialog(this.frame, "Please try a valid email address.", "Error", JOptionPane.PLAIN_MESSAGE);
                            }
                        }

                        customer.setEmail(email);

                        // Password
                        String confirmPassword = " ";
                        while (!Validator.isValidPassword(password) || !password.equals(confirmPassword)) {
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
                                    JOptionPane.showConfirmDialog(this.frame, "Your password must contain at least 8 characters, one digit,\r\none lower case letter, one upper case letter and a symbol.");
                                    continue;
                                }

                                // Confirmation of password
                                JPasswordField confirm = new JPasswordField(10);
                                confirm.setFont(new Font("DIALOG", Font.PLAIN, 30));
                                JLabel confirmLabel = new JLabel("Please confirm you password.");
                                Box confirmBox = Box.createHorizontalBox();
                                confirmBox.add(confirmLabel);
                                confirmBox.add(confirm);
                                int cAnswer = JOptionPane.showConfirmDialog(this.frame, confirmBox, "Confirm password", JOptionPane.PLAIN_MESSAGE);
                                if (cAnswer == JOptionPane.OK_OPTION) {
                                    confirmPassword = confirm.getText();
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                            if (!password.equals(confirmPassword)) {
                                JOptionPane.showConfirmDialog(this.frame, "Your passwords don't match, please try again.");
                                password = "";
                                confirmPassword = " ";
                            }
                        }

                        // full account creation
                        this.conn.createCustomer(cardNumber, email, password, currentMovies, totalMovies);
                    } else {
                        // "no account" account creation
                        this.conn.createCustomer(cardNumber, null, null, currentMovies, totalMovies);
                    }

                } else { // existing customer will be handled by rentMoviesLoggedIn
                    this.currentCustomer = this.conn.getCustomerFromCreditCard(cardNumber);
                    rentMoviesLoggedIn(cardNumber);
                    return;
                }

                customer = this.conn.getCustomerFromCreditCard(cardNumber); // get account from DB using the credit card number to be abel to access customerID

                double totalPrice = 0.0;
                for (Movie m : this.selectedMovies) { // adds total price based on whether or not there is a discount
                    if (!isFirstFree) {
                        totalPrice += MOVIE_PRICE;
                    }
                }

                // inform the customer of total price before proceeding with order
                int pay = JOptionPane.showConfirmDialog(this.frame, "Your order will total €" + totalPrice + "\r\n Proceed with payment?", "TOTAL", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (pay == JOptionPane.NO_OPTION) {
                    return;
                }

                // card PIN simulation
                JOptionPane.showInputDialog(this.frame, "Mock payment card PIN required (won't be checked now).", "Enter PIN", JOptionPane.PLAIN_MESSAGE);

                for (Movie m : this.selectedMovies) {
                    double price = MOVIE_PRICE; // price to be sent to DB
                    if (isFirstFree) {
                        price = 0.0;
                    }
                    // create a rent row in DB
                    if (this.conn.rentMovie(m.getId(), this.getMachineID(), customer.getId(), price)) {
                        currentMovies += 1;
                        totalMovies += 1;
                    }
                }

                this.conn.updateCustomerNumberOfMovies(currentMovies, totalMovies, customer.getId());

                // receipt handling
                int receipt = JOptionPane.showConfirmDialog(this.frame, "Your order was successful!\r\nPlease don't forget to take your movies.", "Thank you!", JOptionPane.YES_NO_OPTION);

                if (receipt == JOptionPane.YES_OPTION) {
                    // email code would go here
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

    /**
     *
     * @param num
     */
    private void rentMoviesLoggedIn(String num) {
        int limitOfMovies = 4;
        String cardNumber = "";

        int tryAgain = JOptionPane.NO_OPTION;

        while (tryAgain == JOptionPane.NO_OPTION) {
            // because the class is called from two different ways, handle the string from parameter
            if (num == null) {
                cardNumber = JOptionPane.showInputDialog(this.frame, "Please enter your card number", "Payment hub", JOptionPane.PLAIN_MESSAGE);
                if (cardNumber == null || cardNumber.equals("")) {
                    return;
                }
                // validates card number
                if (Validator.isValidCreditCard(cardNumber)) {
                    this.currentCustomer.setCreditCards(this.conn.getCustomerCreditCards(this.currentCustomer.getId()));
                    if (!this.currentCustomer.getCreditCards().contains(cardNumber)) {
                        this.currentCustomer.addCreditCard(cardNumber);
                        this.conn.addCardNumberToCustomer(this.currentCustomer, cardNumber);
                    }
                } else {
                    if (cardNumber.equals("")) {
                        JOptionPane.showMessageDialog(this.frame, "Please insert a card number", "Invalid card format", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        JOptionPane.showMessageDialog(this.frame, "Please try a different card number", "Invalid card format", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            } else {
                cardNumber = num;
                this.currentCustomer = this.conn.getCustomerFromCreditCard(cardNumber);
            }

            // caps the number of movies to be rented by customer
            if (this.currentCustomer.getCurrentMovies() + this.selectedMovies.size() >= limitOfMovies) {
                JOptionPane.showMessageDialog(this.frame, "Customers can only rent " + limitOfMovies + " movies at a time.\r\n"
                        + "Please remove " + (this.getCurrentCustomer().getCurrentMovies() + this.selectedMovies.size() - limitOfMovies + 1) + " movie(s) from your cart before proceeding to payment. \r\nOr return the movies you've already rented.", "Oops...", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            // if customer doesn't have a full account, allow them to create one
            if (this.currentCustomer.getEmail() == null) {
                String email = "";
                String password = "";
                // Email address
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
                    // Password && confirm password
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
            HashMap<Movie, Boolean> movieMap = new HashMap<>(); // hash map to store which movies are free in case of loyalty usage
            for (Movie m : this.selectedMovies) {
                if (!this.currentCustomer.isNextFree()) {
                    movieMap.put(m, false);
                    totalPrice += MOVIE_PRICE;
                } else {
                    movieMap.put(m, true);
                    JOptionPane.showMessageDialog(this.frame, m.getTitle() + " is on us.\r\nYou have rented " + this.currentCustomer.getTotalMovies() + " movies with us so far!", "Loyalty at work", JOptionPane.PLAIN_MESSAGE);
                }

                // check for loyalty promotion
                if (this.currentCustomer.checkLoyaltyPromotion()) {
                    this.conn.updateCustomerLoyalty(this.currentCustomer);
                    JOptionPane.showMessageDialog(this.frame, "Thank you for staying loyal.\r\nYou have now reached the " + this.currentCustomer.getLoyalty().getName() + " loytalty!\r\nThis means every time you rent " + this.currentCustomer.getLoyalty().getNumberOfMovies() + " the last one is on us.", "CONGRATULATIONS", JOptionPane.PLAIN_MESSAGE);
                }
            }

            // give the customer a choice to proceed or not when checking the price
            int proceed = JOptionPane.showConfirmDialog(this.frame, "Your order will total €" + totalPrice + "\r\n Proceed with payment?", "TOTAL", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (proceed == JOptionPane.NO_OPTION) {
                return;
            }

            JOptionPane.showInputDialog(this.frame, "Mock payment card PIN required (won't be checked now).", "Enter PIN", JOptionPane.PLAIN_MESSAGE);
            movieMap.forEach((m, free) -> {
                double price = MOVIE_PRICE;
                if (free) {
                    price = 0.0;
                }
                
                if (!this.conn.rentMovie(m.getId(), this.getMachineID(), this.getCurrentCustomer().getId(), price)) {
                    this.currentCustomer.removeMoviesRented(1); // removes total and current movies from customer in case something goes wrong with the DB connection
                }
            });

            this.conn.updateCustomerNumberOfMovies(this.getCurrentCustomer().getCurrentMovies(), this.getCurrentCustomer().getTotalMovies(), this.getCurrentCustomer().getId());
            if (success) {
                int receipt = JOptionPane.showConfirmDialog(this.frame, "Your order was successful! Please don't forget to take your movies.", "Thank you!", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (receipt == JOptionPane.YES_OPTION) {
                    // ****** BOTAR CODIGO DE MANDAR EMAIL AQUI **********
                }

                resetSession();
                return;
            } else {
                JOptionPane.showMessageDialog(this.frame, "There seems to be an issue with your order. Please try again.", "Oops...", JOptionPane.PLAIN_MESSAGE);
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
                this.frame.changePanel(new RentHomescreens(this, this.currentGenre));
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
                        this.frame.changePanel(new RentHomescreens(this, this.currentGenre));
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(this.frame, "Invalid email or password.", "Oops...", JOptionPane.PLAIN_MESSAGE);
                        break;
                }

            }
        }
    }

    /**
     * Resets the currentCustomer, selectMovies list and currently selected
     * genre, then returns the user to the home screen.
     */
    private void resetSession() {
        this.selectedMovies.removeAll(this.selectedMovies);
        this.currentCustomer = null;
        this.currentGenre = null;
        this.frame.changePanel(new HomeScreen(this));
    }

    /**
     * Returns a DVD to the machine
     *
     * @param id discID
     */
    private void returnDVD(String id) {
        Order o = this.conn.getOrderFromMovieID(id);
        if (o == null) { // if no order is found, display an error message
            JOptionPane.showMessageDialog(this.frame, "Please try a different disc ID.", "Oops...", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        this.currentCustomer = this.conn.getCustomerFromID(o.getCustomerID());
        double priceToBePaid = Movie.getLateReturnPrice(o.getMovie());
        if (priceToBePaid > 0) { // if there's an extra charge, the user has to pay for late return, otherwise they just have to insert their movie
            payForLateReturn(o, priceToBePaid);
            return;
        } else {
            if (this.conn.returnMovie(o, this.machineID, 0.0)) {
                JOptionPane.showMessageDialog(this.frame, "Please insert your DVD. No further charges are required.", "Thank you!", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this.frame, "Please try again. There has been a problem with your return.", "Thank you!", JOptionPane.PLAIN_MESSAGE);
            }
        }

        resetSession();
    }

    /**
     * Handles late return and caps the fee
     *
     * @param o order to be returned
     * @param price price for the late return
     */
    private void payForLateReturn(Order o, double price) {
        if ((LATE_DAY_PRICE * 10) <= price) { // limits the price to 10 late days
            price = LATE_DAY_PRICE * 10;
        }
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

    /**
     * Shows all movies currently rented by a user and allows them to choose one
     * to return
     */
    private void returnUsingCard() {
        String cardNum = ReturnHomeScreem.getCardInput();

        if (cardNum.equals("")) {
            JOptionPane.showMessageDialog(this.frame, "Please enter a card number and try again.", "Oops...", JOptionPane.PLAIN_MESSAGE);
            return;
        }
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
