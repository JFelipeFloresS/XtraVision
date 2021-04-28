/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.DBConnection.DBConnection;
import model.movie.Movie;
import view.frame.Frame;
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
    private ArrayList<Movie> movieList;

    public Controller() {
        try {
            this.conn = new DBConnection();
            this.movieList = getMachineCurrentMoviesFromDB(1);
            Collections.sort(this.movieList, Movie.MovieTitleComparator);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showConfirmDialog(this.frame, "Fatal error, please restart the program.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        switch (e.getActionCommand()) {
            case "Go to rent home screem": {
                try {
                    this.frame.changePanel(new RentHomescreens(this));
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
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

    public ArrayList< Movie> getMachineCurrentMoviesFromDB (int id) throws IOException {

        return this.conn.getMachineCurrentMovies(id);
        /// vamos pegar numero da maquina em uso

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
    
    private void goToMovieScreen(String m) {
        this.movieList.stream().filter((movie) -> (movie.getId().equals(m))).forEachOrdered((Movie movie) -> {
            Controller.this.frame.changePanel(new MovieScreen(movie, Controller.this));
        });
    }

}
