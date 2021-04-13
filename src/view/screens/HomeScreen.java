/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.screens;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.movie.Movie;

/**
 *
 * @author thyag
 */
public class HomeScreen extends JPanel{

    Controller controller;
    
    public HomeScreen(Controller controller) {
        
        this.controller = controller;
        this.setLayout(new GridLayout(2,3));
        ArrayList < Movie > movies = this.controller.getMachineCurrentMovies();
        for(Movie movie:movies){
            JPanel p = new JPanel();
            this.add(p);
            p.setLayout(new BorderLayout());
            JLabel title = new JLabel(movie.getTitle());
            p.add(title,BorderLayout.CENTER);
            System.out.println(movie.getTitle());
        }
        
    }
    
    
    
    
    
    
}
