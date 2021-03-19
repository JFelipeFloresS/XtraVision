/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.DBConnection;

import controller.Controller;
import java.util.ArrayList;

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

    public Connection(String dbServer, String dbUser, String dbPass) {
        this.dbServer = dbServer;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }
    
    /**
     * Sort out database first!!!!!
     * @param id
     * @return 
     */
    public static ArrayList<String> getCustomerCreditCards(int id) {
        return new ArrayList<>();
    }
    
}
