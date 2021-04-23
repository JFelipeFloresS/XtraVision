/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.screens;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.ImageIcon;
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
        this.add(north,BorderLayout.CENTER);
        
       JPanel center = new JPanel();
       this.add(center,BorderLayout.CENTER);
   
       JLabel image = new JLabel();
            image.setIcon(new ImageIcon(getClass().getResource("/Images/HomeScreem.jpg")));
            center.add (image);
            
       
       
       
       
       JButton rent = new JButton("RENT");
       center.add (rent);
       JButton returnButton = new JButton("RETURN");
       center.add (returnButton);
       
       changeButtons(this);
       
       rent.setActionCommand("Go to rent home screem");
       returnButton.setActionCommand("Go to return home screem");
       
       
       
      }
     
    
    private void changeButtons(JPanel panel){
        panel.setBackground(Color.white);
        Component [] components = panel.getComponents();
        for(Component c: components){
            if (c instanceof JButton) {
               Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
                c.setPreferredSize (new Dimension(windowSize.width /4 , windowSize.height /3));
                c.setFont(new Font(Font.SERIF,Font.ROMAN_BASELINE,70));
                c.setForeground(new Color(255,193,17));
                
                
                ((JButton) c).addActionListener(this.controller);
                
                c.setBackground(new Color(204,0,0));
            }
 
            if (c instanceof JPanel)
               changeButtons ((JPanel)c);
            
            
            
        }
        
        
        
        
        
        
    }
   
    
    
    
    
    
    
    }
    
    
    
    
    
 