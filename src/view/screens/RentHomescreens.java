/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.screens;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import model.movie.Movie;

/**
 *
 * @author thyago
 */
public class RentHomescreens extends JPanel {

    private final Controller controller;

    public RentHomescreens(Controller controller) throws IOException {
        this.setLayout(new BorderLayout());
        Dimension winSize = Toolkit.getDefaultToolkit().getScreenSize();
        JPanel panel = new JPanel();
        this.controller = controller;

        ArrayList< Movie> movies = this.controller.getMovieList();
        panel.setLayout(new GridLayout(movies.size() / 4, movies.size() / 8, 15, 15));
        panel.setBackground(new Color(234, 63, 51));
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray, Color.yellow));
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            JButton p = new JButton();
            p.addActionListener(this.controller);
            p.setActionCommand("Selected movie" + movie.getId());
            p.setMinimumSize(new Dimension(winSize.width / 10, winSize.height / 4));
            p.setBackground(Color.WHITE);
            p.setBorder(BorderFactory.createLineBorder(new Color(217, 218, 64), 3));
            panel.add(p);
            p.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setBackground(Color.WHITE);
            infoPanel.setLayout(new GridLayout(5, 1, 10, 10));
            JLabel title = new JLabel(movie.getTitle());
            title.setFont(new Font("DIALOG", Font.BOLD, 30));
            infoPanel.add(title);
            if (movie.getRestriction() != null) {
                JLabel restriction = new JLabel("Age Restriction: " + movie.getRestriction());
                infoPanel.add(restriction);

            }
            JLabel genre = new JLabel();
            if (!movie.getCategory().isEmpty()) {
                String genres = "Genre: ";
                for (int j = 0; j < movie.getCategory().size(); j++) {
                    genres += movie.getCategory().get(j);
                    if (j != movie.getCategory().size() - 1) {
                        genres += " / ";
                    }
                }
                genre.setText(genres);

            }
            infoPanel.add(genre);
            p.add(infoPanel, BorderLayout.CENTER);
            JLabel img = new JLabel();
            img.setIcon(movie.getThumbnail());
            img.setHorizontalAlignment(SwingConstants.CENTER);
            p.add(img, BorderLayout.NORTH);

        }

        JScrollPane sp = new JScrollPane(panel);
        this.add(sp, BorderLayout.CENTER);
        // botar imagem da xtravision no topo
        // botar algum menu do lado?
    }
}
