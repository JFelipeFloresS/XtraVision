/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.screens;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import model.movie.Movie;

/**
 *
 * @author thyago
 */
public class CheckOut extends JPanel {

    private final Controller controller;

    public CheckOut(Controller controller) {
        this.controller = controller;
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLayout(new BorderLayout());
       
       JPanel north = new JPanel();
       north.setLayout(new BorderLayout());
       this.add(north,BorderLayout.NORTH);
       
      
       JLabel image = new JLabel();
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource("/Images/ExtraVision.jpeg"));
            img = img.getScaledInstance(windowSize.width / 6, windowSize.height / 10, java.awt.Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            System.out.println("Image erro");
        }
        image.setIcon(new ImageIcon(img));
        north.add(image,BorderLayout.CENTER);
        image.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel northButtonPanel = new JPanel();
        north.add(northButtonPanel,BorderLayout.EAST);
        
        JButton cancelButton = new JButton("Cancel Order");
        northButtonPanel.add(cancelButton);
        cancelButton.addActionListener(this.controller);
        cancelButton.setActionCommand("Go to main home screen");
        
        
        
        
        JPanel center =  new JPanel();
        this.add(center,BorderLayout.CENTER);
        
        JPanel moviesCheckout = new JPanel();
        center.add(moviesCheckout,BorderLayout.CENTER);
        
        ArrayList< Movie> movies = this.controller.getSelectedMovies();
        for (Movie movie:movies){
            
            JPanel p = new JPanel();
            moviesCheckout.add(p);
            
            JLabel name = new JLabel(movie.getTitle());
            p.add(name);
            
            JButton x = new JButton();
             Image imgX = null;
        try {
            imgX = ImageIO.read(getClass().getResource("/Images/XXXXXX.jpeg"));
            imgX = imgX.getScaledInstance(10,10, java.awt.Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            System.out.println("Image erro");
        }
          x.setIcon(new ImageIcon(imgX));
          p.add(x);
          x.setActionCommand("remove movie " + movie.getId());
          x.addActionListener(this.controller);
       
        }
        
        
        
        JPanel east = new JPanel();
        this.add(east,BorderLayout.EAST);
        
        
        
        
        
        JPanel south = new JPanel();
        this.add(south,BorderLayout.SOUTH);
        south.setLayout(new BorderLayout());
        
        JPanel returnPanel = new JPanel();
        south.add(returnPanel,BorderLayout.WEST);
        
        JButton returnButton = new JButton("Return");
        returnPanel.add(returnButton);
        returnButton.addActionListener(this.controller);
        returnButton.setActionCommand("Go to rent home screem");
        
        JPanel paymentPanel = new JPanel();
        south.add(paymentPanel,BorderLayout.EAST);
        
        JButton paymentButton = new JButton("Checkout");
        paymentPanel.add(paymentButton);
        paymentButton.addActionListener(this.controller);
        paymentButton.setActionCommand("Go to check out");
        
        
        
        
        
        
        
        
    }
    

}
