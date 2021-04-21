/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.DBConnection.DBConnection;
import model.movie.Movie;
import view.frame.Frame;
import view.screens.RentHomescreens;
import view.screens.ReturnHomeScreem;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class Controller implements ActionListener {

    private Frame frame;
    private DBConnection conn;
    
    public Controller() {
        this.conn = new DBConnection();
        this.frame = new Frame(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Go to rent home screem":
        {
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
                //System.out.println(e.getActionCommand());
                break;
        }
    }

    public ArrayList < Movie > getMachineCurrentMovies() throws IOException{
        
       return this.conn.getMachineCurrentMovies(1);
        /// vamos pegar numero da maquina em uso
        
        
        
    
        
    }
    
    public Frame getFrame(){
        return this.frame;
        
    }
    
    
    
    
}
