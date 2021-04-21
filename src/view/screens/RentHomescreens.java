/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.screens;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import model.movie.Movie;

/**
 *
 * @author thyago
 */
public class RentHomescreens extends JPanel {

    Controller controller;

    public RentHomescreens(Controller controller) throws IOException {
        this.setLayout(new BorderLayout());
        Dimension winSize = Toolkit.getDefaultToolkit().getScreenSize();
        JPanel panel = new JPanel();
        panel.setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.controller = controller;

        ArrayList< Movie> movies = this.controller.getMachineCurrentMovies();
        panel.setLayout(new GridLayout(movies.size() / 4, movies.size() / 8, 15, 15));
        for (Movie movie : movies) {
            JPanel p = new JPanel();
            p.setMinimumSize(new Dimension(winSize.width / 10, winSize.height / 4));
            panel.add(p);
            p.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(5, 1, 10, 10));
            JLabel title = new JLabel(movie.getTitle());
            infoPanel.add(title);
            JLabel restriction = new JLabel("Age Restriction: " + movie.getRestriction());
            infoPanel.add(restriction);
            p.add(infoPanel, BorderLayout.CENTER);
            JLabel img = new JLabel();
            img.setIcon(movie.getThumbnail());
            p.add(img, BorderLayout.NORTH);

        }

        JScrollPane sp = new JScrollPane(panel);
        sp.setMinimumSize(winSize);
        this.add(sp, BorderLayout.CENTER);
    }
}
