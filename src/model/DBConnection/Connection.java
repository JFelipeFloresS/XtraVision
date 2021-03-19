/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.DBConnection;

import controller.Controller;
import java.util.ArrayList;
import model.movie.Movie;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class Connection {
    
    private final String dbServer, dbUser, dbPass;
    private Controller controller;
    
    public Connection (Controller controller) {
        this.controller = controller;
        this.dbServer = "jdbc:mysql://apontejaj.com:3306/Felipe_2019405?useSSL=false";
        this.dbUser = "Felipe_2019405";
        this.dbPass = "2019405";
    }

    public Connection() {
        this.dbServer = "jdbc:mysql://apontejaj.com:3306/Felipe_2019405?useSSL=false";
        this.dbUser = "Felipe_2019405";
        this.dbPass = "2019405";
    }
    
    /**
     * Sort out database first!!!!!
     * @param id
     * @return 
     */
    public ArrayList<String> getCustomerCreditCards(int id) {
        return new ArrayList<>();
    }
    
    /**
     * Sort out database first!!!!!
     * @param id
     * @return 
     */
    public ArrayList<Movie> getMachineCurrentMovies(int id) {
        return new ArrayList<>();
    }
    
}
