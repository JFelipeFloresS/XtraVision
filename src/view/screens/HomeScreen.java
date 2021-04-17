/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.screens;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JButton;
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
        this.setLayout(new BorderLayout());
        
        JPanel north = new JPanel();
        this.add(north,BorderLayout.NORTH);
        
       JPanel center = new JPanel();
       this.add(center,BorderLayout.CENTER);
     
       JButton rent = new JButton("Rent");
       center.add (rent);
       JButton returnButton = new JButton("Return");
       center.add (returnButton);
       
       changeButtons(center);
       
       
       
       
      }
     
    
    private void changeButtons(JPanel panel){
        
        Component [] components = panel.getComponents();
        for(Component c: components){
            if (c instanceof JButton) {
                c.setPreferredSize(new Dimension(this.controller.getWindowsSize()));
                
                
                
            }
 
            
            
            
            
        }
        
        
        
        
        
        
    }
   
    
    
    
    
    
    
    }
    
    
    
    
    
 
