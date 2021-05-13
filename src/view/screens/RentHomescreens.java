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
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import model.movie.Movie;
import view.screens.RentHomescreens.NoArrowScrollBarUI;

/**
 *
 * @author thyago
 */
public class RentHomescreens extends JPanel {

    private final Controller controller;
    private final String currentGenre;

    public RentHomescreens(Controller controller, String selectedGenre) {

        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(new Color(9, 9, 9));
        Dimension winSize = Toolkit.getDefaultToolkit().getScreenSize();
        JPanel panel = new JPanel();
        this.controller = controller;
        ArrayList<String> genres = this.controller.getUniqueGenres();
        if (selectedGenre == null) {
            this.currentGenre = genres.get(0);
        } else {
            this.currentGenre = selectedGenre;
        }

        ArrayList< Movie> movies = this.controller.getMovieList();
        panel.setLayout(new GridLayout(2, movies.size() / 2, 15, 15));
        panel.setBackground(new Color(9, 9, 9));
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray, Color.yellow));
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getCategory().contains(this.currentGenre)) {
                Movie movie = movies.get(i);
                JButton p = new JButton();
                p.addActionListener(this.controller);
                p.setActionCommand("Selected movie " + movie.getId());
                p.setPreferredSize(new Dimension(winSize.width / 5, winSize.height / 4));
                p.setMaximumSize(new Dimension(winSize.width / 5, winSize.height / 4));
                p.setMinimumSize(new Dimension(winSize.width / 5, winSize.height / 4));
                p.setBackground(Color.WHITE);
                p.setBorder(BorderFactory.createLineBorder(new Color(255, 210, 25), 3));
                panel.add(p);
                p.setCursor(new Cursor(Cursor.HAND_CURSOR));
                p.setLayout(new BorderLayout());
                JPanel infoPanel = new JPanel();
                infoPanel.setBackground(Color.WHITE);
                infoPanel.setLayout(new BorderLayout(10, 10));
                JLabel title = new JLabel(movie.getTitle());
                title.setFont(new Font("DIALOG", Font.BOLD, 30));
                infoPanel.add(title, BorderLayout.NORTH);
                JPanel detailPanel = new JPanel();
                detailPanel.setBackground(Color.WHITE);
                detailPanel.setLayout(new GridLayout(4, 1, 10, 10));
                infoPanel.add(detailPanel, BorderLayout.CENTER);
                if (movie.getRestriction() != null) {
                    JLabel restriction = new JLabel("Age Restriction: " + movie.getRestriction());
                    detailPanel.add(restriction);

                }
                JLabel genre = new JLabel();
                if (!movie.getCategory().isEmpty()) {
                    String movieGenres = "Genre: ";
                    for (int j = 0; j < movie.getCategory().size(); j++) {
                        movieGenres += movie.getCategory().get(j);
                        if (j != movie.getCategory().size() - 1) {
                            movieGenres += " / ";
                        }
                    }
                    genre.setText(movieGenres);

                }
                detailPanel.add(genre);
                p.add(infoPanel, BorderLayout.CENTER);
                JLabel img = new JLabel();
                img.setIcon(movie.getThumbnail());
                img.setHorizontalAlignment(SwingConstants.CENTER);
                p.add(img, BorderLayout.NORTH);
            }
        }

        JScrollPane sp = new JScrollPane(panel);
        sp.getHorizontalScrollBar().setUI(new NoArrowScrollBarUI());
        sp.getHorizontalScrollBar().setUnitIncrement(25);
        this.add(sp, BorderLayout.CENTER);

        JPanel north = new JPanel();
        north.setLayout(new BorderLayout(10, 10));
        this.add(north, BorderLayout.NORTH);
        north.setBackground(new Color(9, 9, 9));
        north.setPreferredSize(new Dimension(winSize.width, winSize.height / 6));
        JPanel returnButtonPanel = new JPanel();
        north.add(returnButtonPanel, BorderLayout.EAST);
        returnButtonPanel.setPreferredSize(new Dimension(winSize.width / 12, winSize.height / 6));
        returnButtonPanel.setBackground(new Color(9, 9, 9));
        returnButtonPanel.setLayout(new BorderLayout(5, 5));
        
        JButton homeScreen = new JButton("HOME");
        homeScreen.setPreferredSize(new Dimension(75, 50));
        homeScreen.addActionListener(this.controller);
        homeScreen.setActionCommand("Go to main home screen");
        homeScreen.setBackground(new Color(9, 9, 9));
        homeScreen.setForeground(Color.WHITE);
        homeScreen.setBorder(BorderFactory.createLineBorder(new Color(255, 210, 25), 2));
        homeScreen.setFont(new Font("DIALOG", Font.BOLD, 20));
        homeScreen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        returnButtonPanel.add(homeScreen, BorderLayout.NORTH);
        
        JButton cart = new JButton();
        cart.setActionCommand("Check valid order");
        cart.addActionListener(this.controller);
        cart.setBackground(new Color(9, 9, 9));
        cart.setForeground(Color.WHITE);
        cart.setBorder(null);
        cart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cart.setText("" + this.controller.getSelectedMovies().size());
        cart.setForeground(new Color(255, 210, 25));
        cart.setFont(new Font("DIALOG", Font.BOLD, 30));
        cart.setVerticalTextPosition(JButton.NORTH);
        cart.setHorizontalTextPosition(SwingConstants.TRAILING);
        try {
            Image cartImage = ImageIO.read(getClass().getResource("/Images/cart.png"));
            cartImage = cartImage.getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            cart.setIcon(new ImageIcon(cartImage));
        } catch (IOException ex) {
            Logger.getLogger(RentHomescreens.class.getName()).log(Level.SEVERE, null, ex);
        }
        returnButtonPanel.add(cart, BorderLayout.SOUTH);

        JLabel topImage = new JLabel();
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource("/Images/LogoVai.jpeg"
                    + ""));
            img = img.getScaledInstance(winSize.width / 2, winSize.height / 6, java.awt.Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            System.out.println("Image error");
        }
        topImage.setIcon(new ImageIcon(img));
        north.add(topImage);
        topImage.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel northwest = new JPanel();
        northwest.setBackground(new Color(9, 9, 9));
        north.add(northwest, BorderLayout.WEST);

        if (this.controller.getCurrentCustomer() == null) {

            JButton login = new JButton("LOG IN");
            login.setActionCommand("log in");
            login.addActionListener(this.controller);
            login.setForeground(Color.WHITE);
            login.setBorder(BorderFactory.createLineBorder(new Color(250, 210, 25), 2));
            login.setFont(new Font("DIALOG", Font.BOLD, 20));
            login.setPreferredSize(new Dimension(75, 50));
            login.setCursor(new Cursor(Cursor.HAND_CURSOR));
            northwest.add(login);

        } else {

            northwest.setLayout(new BorderLayout());

            JButton logout = new JButton("LOG OUT");
            logout.setActionCommand("log out");
            logout.addActionListener(this.controller);
            logout.setForeground(Color.WHITE);
            logout.setBorder(BorderFactory.createLineBorder(new Color(250, 210, 25), 2));
            logout.setFont(new Font("DIALOG", Font.BOLD, 20));
            logout.setPreferredSize(new Dimension(75, 50));
            logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
            northwest.add(logout, BorderLayout.NORTH);

            JPanel userPanel = new JPanel();
            userPanel.setBackground(new Color(9, 9, 9));
            northwest.add(userPanel, BorderLayout.CENTER);

            if (this.controller.getCurrentCustomer().getEmail() != null) {
                userPanel.setLayout(new GridLayout(2, 1));

                JLabel eLabel = new JLabel("E-mail: " + this.controller.getCurrentCustomer().getEmail());
                eLabel.setForeground(Color.WHITE);
                eLabel.setFont(new Font("DIALOG", Font.BOLD, 20));
                userPanel.add(eLabel);
            }

            JLabel userLabel = new JLabel("Current loyalty: " + this.controller.getCurrentCustomer().getLoyalty().getName());
            userLabel.setForeground(Color.WHITE);
            userLabel.setFont(new Font("DIALOG", Font.BOLD, 20));
            userPanel.add(userLabel);

        }

        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(new Color(255, 210, 25));
        sidePanel.setPreferredSize(new Dimension(winSize.width / 5, winSize.height / 2));
        this.add(sidePanel, BorderLayout.WEST);
        sidePanel.setLayout(new GridLayout(genres.size() + 1, 1, 10, 5));
        JLabel label = new JLabel("Movie genres:");
        label.setFont(new Font("DIALOG", Font.BOLD, 32));
        label.setForeground(new Color(9, 9, 9));
        sidePanel.add(label);
        for (int i = 0; i < genres.size(); i++) {
            JButton genre = new JButton(genres.get(i));
            genre.setActionCommand("selected genre " + genres.get(i));
            genre.addActionListener(this.controller);
            sidePanel.add(genre);
            if (this.currentGenre.equals(genres.get(i))) {
                genre.setBackground(new Color(9, 9, 9));
                genre.setForeground(Color.WHITE);

            } else {
                genre.setBackground(new Color(186, 199, 202));
                genre.setForeground(new Color(9,9,9));
            }
            genre.setFont(new Font("DIALOG", Font.BOLD, 30));
            genre.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            genre.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    class NoArrowScrollBarUI extends BasicScrollBarUI {

        private NoArrowScrollBarUI() {
            super();
        }

        protected JButton createZeroButton(String pos) {
            JButton button = new JButton();
            Image img = null;
            try {
                if (pos.equals("left")) {
                    img = ImageIO.read(getClass().getResource("/Images/left_arrow.jpg"));
                } else {
                    img = ImageIO.read(getClass().getResource("/Images/right_arrow.jpg"));
                }
                img = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
            } catch (IOException ex) {
                System.out.println("Image error");
            }
            button.setIcon(new ImageIcon(img));
            Dimension zeroDim = new Dimension(20, 20);
            button.setPreferredSize(zeroDim);
            button.setMinimumSize(zeroDim);
            button.setMaximumSize(zeroDim);
            return button;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton("left");
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton("right");
        }

        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = Color.WHITE;
            this.trackColor = new Color(9, 9, 9);
        }

    }

}
