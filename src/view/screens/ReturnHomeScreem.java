/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.screens;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author thyag
 */
public class ReturnHomeScreem extends JPanel {
    
    private final Controller controller;
    private static JTextField idInput;
    
    public ReturnHomeScreem(Controller controller) {
        this.controller = controller;
        this.setLayout(new BorderLayout());
         Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
       
        JPanel north = new JPanel();
        this.add(north,BorderLayout.NORTH);
        
        JLabel image = new JLabel();
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource("/Images/LogoVai.jpeg"));
            img = img.getScaledInstance(windowSize.width / 6, windowSize.height / 10, java.awt.Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            System.out.println("Image erro");
        }
        image.setIcon(new ImageIcon(img));
        north.add(image);
        image.setHorizontalAlignment(SwingConstants.CENTER);
        
         
         
        JPanel center = new JPanel();
        this.add(center,BorderLayout.CENTER);
        
        JPanel center2 = new JPanel();
        center.add(center2, BorderLayout.CENTER);
        
        ReturnHomeScreem.idInput = new JTextField(20);
        center2.add(ReturnHomeScreem.idInput);
        
        JPanel centerText = new JPanel();
        center2.add(centerText,BorderLayout.CENTER);
      
        
        JButton returnDvd = new JButton("Return DVD");
        center.add(returnDvd,BorderLayout.WEST);
        
        returnDvd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        returnDvd.setActionCommand("return DVD");
        returnDvd.addActionListener(this.controller);
        returnDvd.setPreferredSize(new Dimension(windowSize.width / 7, windowSize.height / 8));
        returnDvd.setBackground(new Color(186, 199, 202));
        returnDvd.setForeground(new Color(9, 9, 9));
        returnDvd.setFont(new Font(Font.SERIF, Font.BOLD, 25));;
        
        
        
        JLabel label = new JLabel("Insert The DVD ID:");
        label.setForeground(Color.WHITE);
        
         
     
        
        JButton homeS = new JButton("Home Screen");
        center.add(homeS,BorderLayout.EAST);
        
        homeS.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeS.setActionCommand("Go to rent home screem");
        homeS.addActionListener(this.controller);        
        homeS.setPreferredSize(new Dimension(windowSize.width / 7, windowSize.height / 8));
        homeS.setBackground(new Color(186, 199, 202));
        homeS.setForeground(new Color(9, 9, 9));
        homeS.setFont(new Font(Font.SERIF, Font.BOLD, 25));;
        
        
        
        center.add(homeS,BorderLayout.EAST);
        center.add(label);
        center.add(ReturnHomeScreem.idInput);
        center.add(returnDvd,BorderLayout.WEST);
        
        
        JPanel south = new JPanel();
        this.add(south,BorderLayout.SOUTH);
        
        JButton dvdR = new JButton("return DVD using card");
        south.add(dvdR,BorderLayout.CENTER);
        dvdR.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dvdR.setActionCommand("return DVD using card");
        dvdR.addActionListener(this.controller);        
        dvdR.setPreferredSize(new Dimension(windowSize.width / 7, windowSize.height / 8));
        dvdR.setBackground(new Color(186, 199, 202));
        dvdR.setForeground(new Color(9, 9, 9));
        dvdR.setFont(new Font(Font.SERIF, Font.BOLD, 20));;
        
        
        
        
        
    }
    
    public static String getReturnIDInput() {
        return ReturnHomeScreem.idInput.getText();
    }
    
}
