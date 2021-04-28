/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.screens;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JLabel;
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
      Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLayout(new BorderLayout());
        this.movie = movie;
        this.controller = controller;
        
        
        JPanel north = new JPanel();
        this.add(north,BorderLayout.NORTH);
        
        north.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(this.movie.getTitle());
        north.add(titleLabel,BorderLayout.NORTH);

        JLabel descriptionLable = new JLabel(this.movie.getDescription());
        north.add(descriptionLable,BorderLayout.NORTH);

        JLabel director = new JLabel(this.movie.getDirector());
        north.add(director,BorderLayout.NORTH);
        
      ///criar GridLayout



        
         JPanel east = new JPanel();
       this.add(east,BorderLayout.EAST);
       
       east.setLayout(new BorderLayout());
       
       
       JPanel buttonPanel = new JPanel();
    





     



        
    }
    
    
}
