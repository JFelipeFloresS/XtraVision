
package view.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Thyago De Oliveira Alves
 * @author José Felipe Flores da Silva
 */
abstract class SuccessFailureScreen extends JFrame {
    
    private ImageIcon image;
    protected String imgString, message, title;
    
    public SuccessFailureScreen() {
        setImageMessageTitle();
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource(this.imgString));
            img = img.getScaledInstance(1300, 600, java.awt.Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            System.out.println("Image erro");
        }
        this.image = new ImageIcon(img);
        setWindow();
    }
    
    private void setWindow() {
        this.setSize(1300, 700);
        this.setResizable(false);
        this.setTitle(this.title);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(255, 210, 25), 2));
        this.add(contentPane);
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(new Color(9,9,9));
        
        JPanel center = new JPanel();
        center.setBackground(new Color(9,9,9));
        contentPane.add(center, BorderLayout.CENTER);
        
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(this.image);
        center.add(imageLabel);
        
        JPanel south = new JPanel();
        south.setBackground(new Color(9,9,9));
        contentPane.add(south, BorderLayout.SOUTH);
        
        JLabel messageLabel = new JLabel(this.message);
        messageLabel.setFont(new Font("DIALOG", Font.PLAIN, 36));
        messageLabel.setForeground(Color.WHITE);
        south.add(messageLabel);
        
        this.validate();
        this.repaint();
        
    }
    
    abstract protected void setImageMessageTitle();
}
