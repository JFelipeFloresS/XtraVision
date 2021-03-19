/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.customer;

import java.util.ArrayList;
import model.DBConnection.DBConnection;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class Customer {

    private final int id;
    private String email;
    private ArrayList<String> creditCards;
    private int moviesRented;
    private DBConnection connection;
    //private Loyalty loyalty;

    public Customer(int id, String email, int moviesRented) {
        this.id = id;
        this.email = email;
        this.creditCards = new ArrayList<>();
        this.moviesRented = moviesRented;
        this.connection = new DBConnection();    
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

    public int getMoviesRented() {
        return moviesRented;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addCreditCard(String creditCard) {
        this.creditCards.add(creditCard);
    }
/**
 * add movies currently rented by the account
 * @param moviesRented number of movies to add
 */
    public void addMoviesRented(int moviesRented) {
        this.moviesRented += moviesRented;
    }
    
  /**
   * remove movies rented currently by the account
   * @param moviesRented number of movies to remove
   */
    public void removeMoviesRented(int moviesRented) {
        this.moviesRented -= moviesRented;

        
          
    }

    public void getCreditCardsFromDB(){
       this.creditCards = this.connection.getCustomerCreditCards(this.id);
     
    }
    
    
    
    
}
