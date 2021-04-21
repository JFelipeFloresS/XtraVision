/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCreation;

import controller.Controller;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import model.movie.Movie;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class DistributeMoviesToMachines {

    private String dbServer, dbUser, dbPass;
    private final Controller controller;
    private Connection conn;
    private Random rd;
    private final ArrayList<Movie> movies;

    public DistributeMoviesToMachines(Controller controller) throws IOException {
        this.controller = controller;
        this.movies = getMovies();
        setUpCredentials();
    }

    private void setUpCredentials() {
        this.dbServer = "jdbc:mysql://apontejaj.com:3306/Felipe_2019405?useSSL=false";
        this.dbUser = "Felipe_2019405";
        this.dbPass = "2019405";
    }

    public ArrayList<Movie> getMovies() throws IOException {
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            String query = "SELECT * FROM movies";
            this.conn = DriverManager.getConnection(this.dbServer, this.dbUser, this.dbPass);
            Statement stmt = this.conn.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Movie m = new Movie(rs.getString("movieID"), rs.getString("title"), rs.getString("description"), rs.getString("ageRestriction"), rs.getString("thumbnail"));
                movies.add(m);
            }

            rs.close();
            stmt.close();
            this.conn.close();

        } catch (SQLException e) {
            System.out.println(e);
        }

        //createDiscs(movies);
        return movies;
    }

    public void createDiscs(ArrayList<Movie> movies) throws IOException {
        ArrayList<Movie> discs = new ArrayList<>();
        int c = 1;
        for (Movie m : movies) {
            for (int i = 0; i < 20; i++) {
                String id = m.getId();
                if (i < 10) {
                    id += "0";
                }
                id += i;
                discs.add(new Movie(id, m.getTitle(), m.getDescription(), m.getRestriction(), m.getThumbnail().toString(), c));
                c++;
                if (c > 6) {
                    c = 1;
                }
            }
        }
        
        String query = "INSERT INTO discs(discID, movieID, machine) VALUES ";
        for (int i = 0; i < discs.size() - 1; i++) {
            query += "(?,?,?),";
        }
        query += "(?,?,?);";
        
        try {
            this.conn = DriverManager.getConnection(this.dbServer, this.dbUser, this.dbPass);
            PreparedStatement ps = this.conn.prepareStatement(query);
            int paramIndex = 1;
            for(Movie m: discs) {
                ps.setString(paramIndex, m.getId());
                paramIndex++;
                ps.setString(paramIndex, m.getId().substring(0, m.getId().length()-2));
                paramIndex++;
                ps.setInt(paramIndex, m.getMachine());
                paramIndex++;
            }
            ps.execute();
            ps.close();
            this.conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

}
