/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.screens;

import controller.Controller;
import javax.swing.JPanel;
import model.movie.Movie;

/**
 *
 * @author thyag
 */
public class MovieScreen extends JPanel {
    
    private final Movie movie;
    private final Controller controller;

    public MovieScreen(Movie movie, Controller controller) {
        this.movie = movie;
        this.controller = controller;
    }
    
    
}
