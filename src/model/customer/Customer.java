/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.customer;

import java.util.ArrayList;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class Customer {

    private int id;
    private String email;
    private ArrayList<String> creditCards;
    private int currentMovies, totalMovies;
    private Loyalty loyalty;

    public Customer(int id, String email, int moviesRented, int totalMovies, String loyalty) {
        this.id = id;
        this.email = email;
        this.creditCards = new ArrayList<>();
        this.currentMovies = moviesRented;    
        this.totalMovies = totalMovies;
        this.loyalty = Loyalty.getLoyalty(loyalty);
    }
    
    public Customer(String cardNumber, int moviesRented, int totalMovies) {
        this.creditCards = new ArrayList<>();
        this.creditCards.add(cardNumber);
        this.currentMovies = moviesRented;
        this.totalMovies = totalMovies;
        this.loyalty = Loyalty.NONE;
    }

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
    
    public void startLoyaltyProgramme() {
        this.loyalty = Loyalty.STANDARD;
    }

    public void addCreditCard(String creditCard) {
        this.creditCards.add(creditCard);
    }
/**
 * add movies currently rented by the account
 * @param moviesRented number of movies to add
 */
    public void addMoviesRented(int moviesRented) {
        this.currentMovies += moviesRented;
        this.totalMovies += moviesRented;
    }
    
  /**
   * remove movies rented currently by the account
   * @param moviesRented number of movies to remove
   */
    public void removeMoviesRented(int moviesRented) {
        this.currentMovies -= moviesRented;          
    }

    public ArrayList<String> getCreditCards() {
        return creditCards;
    }

    public Loyalty getLoyalty() {
        return loyalty;
    }

    public void setCreditCards(ArrayList<String> cards){
       this.creditCards = cards;
     
    }
    
    
    
    
}
