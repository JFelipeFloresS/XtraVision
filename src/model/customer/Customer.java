package model.customer;

import java.util.ArrayList;

/**
 * @author Thyago De Oliveira Alves
 * @author José Felipe Flores da Silva
 */
public class Customer {

    private int id;
    private String email;
    private ArrayList<String> creditCards;
    private int currentMovies, totalMovies;
    private Loyalty loyalty;

    /**
     * Constructor with no credit card numbers
     * @param id customerID
     * @param email email address
     * @param moviesRented number of movies currently rented
     * @param totalMovies number of movies rented in total
     * @param loyalty name of loyalty status
     */
    public Customer(int id, String email, int moviesRented, int totalMovies, String loyalty) {
        this.id = id;
        this.email = email;
        this.creditCards = new ArrayList<>();
        this.currentMovies = moviesRented;
        this.totalMovies = totalMovies;
        this.loyalty = Loyalty.getLoyalty(loyalty);
    }

    /**
     * Constructor with no ID and email. Loyalty set to NONE
     * @param cardNumber credit card number to be added to credit cards
     * @param moviesRented number of movies currently rented
     * @param totalMovies number of movies rented in total
     */
    public Customer(String cardNumber, int moviesRented, int totalMovies) {
        this.creditCards = new ArrayList<>();
        this.creditCards.add(cardNumber);
        this.currentMovies = moviesRented;
        this.totalMovies = totalMovies;
        this.loyalty = Loyalty.NONE;
    }

    /**
     * Constructor nearly empty
     * @param id customerID
     */
    public Customer(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getCurrentMovies() {
        return currentMovies;
    }

    public int getTotalMovies() {
        return totalMovies;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the customer's loyalty to the most basic programme
     */
    public void startLoyaltyProgramme() {
        this.loyalty = Loyalty.STANDARD;
    }

    /**
     * Adds a credit card number to the array list of credit cards
     * @param creditCard card number
     */
    public void addCreditCard(String creditCard) {
        this.creditCards.add(creditCard);
    }

    /**
     * Adds movies to the account currentMovies and totalMovies
     *
     * @param moviesRented number of movies to add
     */
    public void addMoviesRented(int moviesRented) {
        this.currentMovies += moviesRented;
        this.totalMovies += moviesRented;
    }

    /**
     * Remove currentMovies and totalMovies rented by the account
     *
     * @param moviesRented number of movies to remove
     */
    public void removeMoviesRented(int moviesRented) {
        this.currentMovies -= moviesRented;
        this.totalMovies -= moviesRented;
    }

    public ArrayList<String> getCreditCards() {
        return creditCards;
    }

    public Loyalty getLoyalty() {
        return loyalty;
    }

    public void setCreditCards(ArrayList<String> cards) {
        this.creditCards = cards;

    }

    /**
     * Adds 1 movie to the account and checks the current number against the user's current loyalty next number
     * @return true if the movie is free or false if it isn't
     */
    public boolean isNextFree() {
        this.addMoviesRented(1);
        return this.totalMovies % this.loyalty.getNumberOfMovies() == 0;
    }
    
    /**
     * Checks current number of movies against the user's current loyalty promotion threshold number
     * @return true if it's a promoting rent, false if it isn't
     */
    public boolean checkLoyaltyPromotion() {
        if (this.loyalty.getPromotionThreshold() == 0) {
            return false;
        }
        if (this.totalMovies % this.loyalty.getPromotionThreshold() == 0) {
            this.loyalty = Loyalty.promoteLoyalty(this.loyalty);
            return true;
        } 
        return false;
    }

}
