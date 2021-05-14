
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
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import model.movie.Movie;

/**
 * @author Thyago De Oliveira Alves
 * @author José Felipe Flores da Silva
 */
public class CheckOut extends JPanel {

    private final Controller controller;

    public CheckOut(Controller controller) {
        this.controller = controller;
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLayout(new BorderLayout());

        JPanel north = new JPanel();
        north.setLayout(new BorderLayout());
        this.add(north, BorderLayout.NORTH);

        JLabel image = new JLabel();
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource("/Images/LogoVai.jpeg"));
            img = img.getScaledInstance((int) (windowSize.width * 0.35), (int) (windowSize.height * 0.20), java.awt.Image.SCALE_SMOOTH);

        } catch (IOException ex) {
            System.out.println("Image erro");
        }
        image.setIcon(new ImageIcon(img));
        north.add(image, BorderLayout.CENTER);
        image.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel northButtonPanel = new JPanel();
        north.add(northButtonPanel, BorderLayout.EAST);

        JButton cancelButton = new JButton("Cancel Order");
        northButtonPanel.add(cancelButton);
        
        cancelButton.addActionListener(this.controller);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setActionCommand("Go to main home screen");
        cancelButton.setPreferredSize(new Dimension(windowSize.width / 7, windowSize.height / 8));
        cancelButton.setBackground(new Color(186, 199, 202));
        cancelButton.setForeground(new Color(9, 9, 9));
        cancelButton.setFont(new Font(Font.SERIF, Font.BOLD, 30));;

        JPanel center = new JPanel();
        this.add(center, BorderLayout.CENTER);

        JPanel moviesCheckout = new JPanel();
        center.add(moviesCheckout, BorderLayout.CENTER);

        moviesCheckout.setPreferredSize(new Dimension((int) (windowSize.width / 2.5), (int) (windowSize.height * 0.6)));
        moviesCheckout.setLayout(new GridLayout(4, 1, 10, 10));
        moviesCheckout.setBackground(new Color(247, 247, 247));
        moviesCheckout.setBorder(BorderFactory.createLineBorder(new Color(255, 210, 25), 2));

        ArrayList< Movie> movies = this.controller.getSelectedMovies();
        for (Movie movie : movies) {

            JPanel p = new JPanel();
            moviesCheckout.add(p);
            p.setBackground(new Color(247, 247, 247));
            p.setBorder(BorderFactory.createLineBorder(new Color(255, 210, 25), 2));
            p.setBackground(new Color(186,199,202));
            
            JLabel name = new JLabel(movie.getTitle());
            p.add(name);
            name.setForeground(new Color(9, 9, 9));
            name.setFont(new Font(Font.SERIF, Font.BOLD, 38));
            

            JButton x = new JButton();
            Image imgX = null;
            try {
                imgX = ImageIO.read(getClass().getResource("/Images/XXXXXX.jpeg"));
                imgX = imgX.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
            } catch (IOException ex) {
                System.out.println("Image erro");
            }
            x.setIcon(new ImageIcon(imgX));
            p.add(x);
            x.setActionCommand("remove movie " + movie.getId());
            x.addActionListener(this.controller);
            x.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        JPanel east = new JPanel();
        this.add(east, BorderLayout.EAST);

        JPanel payment = new JPanel();
        east.add(payment, BorderLayout.EAST);
        payment.setBackground(Color.white);
        payment.setPreferredSize(new Dimension((int) (windowSize.width / 3.5), (int) (windowSize.height * 0.6)));
        payment.setLayout(new BorderLayout());
        payment.setBorder(BorderFactory.createLineBorder(new Color(255, 210, 25), 2));

        JLabel paymentLabel = new JLabel("      PAYMENT ");
        payment.add(paymentLabel, BorderLayout.NORTH);
        paymentLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 210, 25), 2));
        paymentLabel.setForeground(new Color(9, 9, 9));
        paymentLabel.setFont(new Font(Font.SERIF, Font.BOLD, 75));;
       

        double totalPrice = 2.99 * this.controller.getSelectedMovies().size();
        
        JLabel priceLabel = new JLabel("<html> Use the code FREEMOVIE for your first order be <u>FREE</u> of charge!!! <br> <br><br> Movies rented: "+this.controller.getSelectedMovies().size()+   "<br>Price per movie: €2.99<br>Total: €" + totalPrice+"</html>");
        payment.add(priceLabel, BorderLayout.CENTER);
        priceLabel.setBackground(new Color(186,199,202));
        priceLabel.setForeground(new Color(9, 9, 9));
        priceLabel.setVisible(true);
        priceLabel.setFont(new Font(Font.SERIF, Font.BOLD, 35));;
        priceLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 210, 25), 2));
         
        JLabel visa = new JLabel();
        payment.add(visa, BorderLayout.SOUTH);
        visa.setHorizontalAlignment(SwingConstants.CENTER);
        Image visa1 = null;

        try {
            visa1 = ImageIO.read(getClass().getResource("/Images/visa.jpg"));
            visa1 = visa1.getScaledInstance((int) (windowSize.width * 0.08), (int) (windowSize.height * 0.05), java.awt.Image.SCALE_SMOOTH);

        } catch (IOException ex) {
            System.out.println("Image erro");
        }

        visa.setIcon(new ImageIcon(visa1));

        JPanel south = new JPanel();
        this.add(south, BorderLayout.SOUTH);
        south.setLayout(new BorderLayout());

        JPanel returnPanel = new JPanel();
        south.add(returnPanel, BorderLayout.WEST);

        JButton returnButton = new JButton("Return");
        returnPanel.add(returnButton);
        returnButton.addActionListener(this.controller);
        returnButton.setActionCommand("Go to rent home screem");
        returnButton.setPreferredSize(new Dimension(windowSize.width / 7, windowSize.height / 8));
        returnButton.setBackground(new Color(186, 199, 202));
        returnButton.setForeground(new Color(9, 9, 9));
        returnButton.setFont(new Font(Font.SERIF, Font.BOLD, 30));;
        returnButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        
        
        JPanel paymentPanel = new JPanel();
        south.add(paymentPanel, BorderLayout.EAST);

        JButton paymentButton = new JButton("Checkout");
        paymentPanel.add(paymentButton);
        paymentButton.addActionListener(this.controller);
        paymentButton.setActionCommand("Go to check out");
        paymentButton.setPreferredSize(new Dimension(windowSize.width / 7, windowSize.height / 8));
        paymentButton.setBackground(new Color(186, 199, 202));
        paymentButton.setForeground(new Color(9, 9, 9));
        paymentButton.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        paymentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    }

}
