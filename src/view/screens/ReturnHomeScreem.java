/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.screens;

import controller.Controller;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author thyag
 */
public class ReturnHomeScreem extends JPanel {
    
    private final Controller controller;
    private JTextField idInput;
    
    public ReturnHomeScreem(Controller controller) {
        this.controller = controller;
        
       this.idInput = new JTextField(20);
        
     JButton returnDvd = new JButton("Return DvD");
     JLabel label = new JLabel("Insert The Dvd Id!");
     
     this.add(label);
     this.add(this.idInput);
     this.add(returnDvd);
     
        
        
    }
    
    
    
    
    
    
    
    
}
