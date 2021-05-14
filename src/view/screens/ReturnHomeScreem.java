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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * @author Thyago De Oliveira Alves
 * @author José Felipe Flores da Silva
 */
public class ReturnHomeScreem extends JPanel {

    private final Controller controller;
    private static JTextField cardInput;

    public ReturnHomeScreem(Controller controller) {
        this.controller = controller;
        this.setLayout(new BorderLayout());
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();

        JPanel north = new JPanel();
        this.add(north, BorderLayout.NORTH);

        JButton homeS = new JButton("Home Screen");
        north.setLayout(new BorderLayout());
        north.add(homeS, BorderLayout.WEST);
        homeS.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeS.setActionCommand("Go to rent home screem");
        homeS.addActionListener(this.controller);
        homeS.setPreferredSize(new Dimension(windowSize.width / 7, windowSize.height / 8));
        homeS.setBackground(new Color(186, 199, 202));
        homeS.setForeground(new Color(9, 9, 9));
        homeS.setFont(new Font(Font.SERIF, Font.BOLD, 25));

       
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
        this.add(center, BorderLayout.CENTER);

      

        JPanel center2 = new JPanel();
        center.add(center2, BorderLayout.CENTER);

       

        JLabel label = new JLabel("<html><u>Choose DVD to return</u></html> :");
        center.add(label);
        label.setFont(new Font(Font.SERIF, Font.BOLD, 33));
        label.setForeground(Color.WHITE);
       
        
        
        ReturnHomeScreem.cardInput = new JTextField(15);
        cardInput.setPreferredSize(new Dimension((int) (windowSize.width / 6), windowSize.height / 12));
        center.add(ReturnHomeScreem.cardInput);
        center.add(ReturnHomeScreem.cardInput);
        cardInput.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        cardInput.setForeground(Color.red);
        cardInput.setBorder(BorderFactory.createLineBorder(new Color(255, 210, 25), 3));

       
        
        JButton returnDvd = new JButton("Return DVD");
        center.add(returnDvd, BorderLayout.SOUTH);
        returnDvd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        returnDvd.setActionCommand("return DVD using card");
        returnDvd.addActionListener(this.controller);
        returnDvd.setPreferredSize(new Dimension(windowSize.width / 7, windowSize.height / 12));
        returnDvd.setBackground(new Color(186, 199, 202));
        returnDvd.setForeground(new Color(9, 9, 9));
        returnDvd.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        
        
        
        
        
        
        
        
    }

    public static String getCardInput() {
        return ReturnHomeScreem.cardInput.getText();
    }

}
