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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import model.movie.Movie;

/**
 *
 * @author thyago
 */
public class MovieScreen extends JPanel {
    
    private static String LINEBREAK = "<br>";
    
    private final Movie movie;
    private final Controller controller;
    
    public MovieScreen(Movie movie, Controller controller) {
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLayout(new BorderLayout());
        this.movie = movie;
        this.controller = controller;
        
        JPanel west = new JPanel();
        this.add(west, BorderLayout.WEST);
        west.setPreferredSize(new Dimension(windowSize.width / 4, windowSize.height));
        
        JLabel image = new JLabel();
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource("/Images/LogoVai.jpeg"));
            img = img.getScaledInstance(windowSize.width / 6, windowSize.height / 10, java.awt.Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            System.out.println("Image erro");
        }
        image.setIcon(new ImageIcon(img));
        west.add(image);
        image.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel center = new JPanel();
        this.add(center, BorderLayout.CENTER);
        
        center.setPreferredSize(new Dimension(windowSize.width / 2, windowSize.height));
        
        JPanel movieInfo = new JPanel();
        center.add(movieInfo);
        movieInfo.setLayout(new BorderLayout());
        movieInfo.setBorder(BorderFactory.createLineBorder(new Color(255, 210, 25), 2));
        movieInfo.setPreferredSize(new Dimension(windowSize.width / 2, (int) (windowSize.height)));
        
        JLabel movieImg = new JLabel();
        movieImg.setPreferredSize(new Dimension(windowSize.width / 2, (int) (windowSize.height * 0.34)));
        movieImg.setIcon(movie.getResizedThumbnail(200, 350));
        movieImg.setHorizontalAlignment(SwingConstants.CENTER);
        movieInfo.add(movieImg, BorderLayout.NORTH);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setPreferredSize(new Dimension(windowSize.width / 2, (int) (windowSize.height * 0.06)));
        movieInfo.add(titlePanel, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createLineBorder(new Color(255, 210, 25), 2));
        titlePanel.setBackground(new Color(255, 210, 25));
        
        JLabel movieName = new JLabel(movie.getTitle());
        titlePanel.add(movieName);
        
        movieName.setForeground(new Color(9, 9, 9));
        movieName.setFont(new Font(Font.SERIF, Font.BOLD, 36));
        
        JPanel gridPanel = new JPanel();
        movieInfo.add(gridPanel, BorderLayout.SOUTH);
        gridPanel.setPreferredSize(new Dimension(windowSize.width / 2, (int) (windowSize.height * 0.6)));
        gridPanel.setLayout(new BorderLayout(40, 40));
        gridPanel.setBackground(new Color(186, 199, 202));
        gridPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 210, 25), 2));
        
        JPanel infoPanel = new JPanel();
        gridPanel.add(infoPanel, BorderLayout.NORTH);
        infoPanel.setBackground(new Color(186, 199, 202));
        
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setPreferredSize(new Dimension(windowSize.width / 2, 250));
        descriptionPanel.setBackground(new Color(186, 199, 202));
        descriptionPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 210, 25), 50));
        
        JScrollPane scrollPane = new JScrollPane(descriptionPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(50, 30, 300, 50);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 210, 25), 2));
        
        infoPanel.setLayout(new GridLayout(4, 1, 10, 10));
        gridPanel.add(scrollPane, BorderLayout.CENTER);
        
        JLabel director = new JLabel("Director: " + movie.getDirector());
        infoPanel.add(director);
        
        JLabel duration = new JLabel("Duration: " + movie.getDuration());
        infoPanel.add(duration);
        
        JLabel restriction = new JLabel("Age Restriction: " + movie.getRestriction());
        infoPanel.add(restriction);
        
        String genre = "Genre: ";
        for (String g : movie.getCategory()) {
            genre += g + " ";
            
        }
        
        JLabel category = new JLabel(genre);
        infoPanel.add(category);
        
        JTextArea description = new JTextArea(movie.getDescription());
        description.setBackground(new Color(186, 199, 202));
        description.setEditable(false);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setColumns(30);
        description.setRows(5);
        descriptionPanel.add(description);
        description.setAlignmentY(CENTER_ALIGNMENT);
        
        Component[] components = infoPanel.getComponents();
        for (Component c : components) {
            c.setForeground(new Color(9, 9, 9));
            c.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        }
        Component[] comps = descriptionPanel.getComponents();
        for (Component c : comps) {
            c.setForeground(new Color(9, 9, 9));
            c.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        }
        JPanel east = new JPanel();
        this.add(east, BorderLayout.EAST);
        east.setPreferredSize(new Dimension(windowSize.width / 4, windowSize.height));
        
        east.setLayout(new BorderLayout());
        
        JPanel rentPanel = new JPanel();
        east.add(rentPanel, BorderLayout.NORTH);
        
        JButton rent = new JButton("RENT");
        rent.setActionCommand("rent " + movie.getId());
        rent.addActionListener(this.controller);
        rentPanel.add(rent);
        rent.setPreferredSize(new Dimension(windowSize.width / 7, windowSize.height / 8));
        rent.setBackground(new Color(186, 199, 202));
        rent.setForeground(new Color(9, 9, 9));
        rent.setFont(new Font(Font.SERIF, Font.BOLD, 30));;
        rent.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        
        
        JPanel returnPanel = new JPanel();
        east.add(returnPanel, BorderLayout.SOUTH);
        
        JButton returnButton = new JButton("HOME SCREEN");
        returnPanel.add(returnButton);
        returnButton.setPreferredSize(new Dimension(windowSize.width / 7, windowSize.height / 8));
        returnButton.setBackground(new Color(186, 199, 202));
        returnButton.setForeground(new Color(9, 9, 9));
        returnButton.setFont(new Font(Font.SERIF, Font.BOLD, 30));;
        returnButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        returnButton.setActionCommand("Go to rent home screem");
        returnButton.addActionListener(this.controller);
        
    }
    
}
