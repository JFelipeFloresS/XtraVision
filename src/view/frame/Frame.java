/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.frame;

import controller.Controller;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class Frame extends JFrame {
    
    private final Controller controller;
    private Dimension windowSize;
    
    public Frame (Controller controller) {
        this.controller = controller;
        setUpWindow();
        changePanel(new JPanel());
    }
    
    /**
     * Window settings, such as size, visible and close default operations
     */
    private void setUpWindow() {
        
    }
    
    /**
     * Removes old panel from the frame's content pane, adds the new one, repaints and validates
     * @param panel New panel to be added
     */
    private void changePanel(JPanel panel) {
        
    }
}
