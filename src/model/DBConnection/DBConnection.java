/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.DBConnection;

import DataCreation.DistributeMoviesToMachines;
import DataCreation.HardCoded;
import controller.Controller;
import java.io.IOException;
import java.util.ArrayList;
import model.movie.Movie;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class DBConnection {
    
    private String dbServer, dbUser, dbPass;
    private Controller controller;
    private Connection conn;
    
    public DBConnection (Controller controller) {
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
    
    private Connection establishConnection() throws SQLException {
        return  DriverManager.getConnection(this.dbServer, this.dbUser, this.dbPass);
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
    public ArrayList<Movie> getMachineCurrentMovies(int id) throws IOException {
        ArrayList<Movie> movies = new ArrayList<>();
        ArrayList<String> unique = new ArrayList<>();
        
        try {
            String query = "SELECT * FROM discs LEFT JOIN movies ON discs.movieID = movies.movieID WHERE discs.machine=" + id +";";
            this.conn = establishConnection();
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                if (!unique.contains(rs.getString("discs.movieID"))) {
                    unique.add(rs.getString("discs.movieID"));
                    movies.add(new Movie(rs.getString("discID"), rs.getString("title"), rs.getString("description"), rs.getString("ageRestriction"), true, rs.getString("thumbnail")));
                }
            }
            rs.close();
            stmt.close();
            this.conn.close();
        } catch (SQLException e) {
            System.out.println("Error getMachineCurrentMovies(): \r\n" + e.getMessage());
        }
        
        return movies;
    }
    
    
}
