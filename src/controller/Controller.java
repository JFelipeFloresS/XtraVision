/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import model.DBConnection.DBConnection;
import model.movie.Movie;
import view.frame.Frame;

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
            default:
                System.out.println(e.getActionCommand());
                break;
        }
    }

    public ArrayList < Movie > getMachineCurrentMovies(){
        
       return this.conn.getMachineCurrentMovies(0);
        /// vamos pegar numero da maquina em uso
        
        
        
    }
    
    public Dimension getWindowsSize(){
        
       return this.frame.getsize();
               
        
    }
    
    
    
    
}
