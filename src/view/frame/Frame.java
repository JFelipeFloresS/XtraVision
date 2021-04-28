/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.frame;

import controller.Controller;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import view.screens.HomeScreen;

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
        changePanel(new HomeScreen(this.controller));
    }
    
    /**
     * Window settings, such as size, visible and close default operations
     */
    private void setUpWindow() {
        
        this.windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(this.windowSize);
        this.setResizable(false);
        this.setTitle("XtraVision");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                closeProgram();
            }
        });
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setVisible(true);
        
        
    }
    
    /**
     * Closes DB connection before exiting the system.
     */
    private void closeProgram() {
        this.controller.closeDBConnection();
        System.exit(0);
    }
    
    /**
     * Removes old panel from the frame's content pane, adds the new one, repaints and validates
     * @param panel New panel to be added
     */
    public void changePanel(JPanel panel) {
        
        this.getContentPane().removeAll();
        this.add(panel);
        this.validate();
        this.repaint();
        
    }
    

    
    
}
