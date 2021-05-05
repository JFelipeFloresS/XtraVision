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
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author thyag
 */
public class HomeScreen extends JPanel{

    Controller controller;
    static JComboBox machineSelect;
    
    public HomeScreen(Controller controller) {
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.controller = controller;
        this.setLayout(new BorderLayout());
        
        JPanel north = new JPanel();
        this.add(north,BorderLayout.NORTH);
        
         north.setLayout(new BorderLayout());

         JPanel machinePanel = new JPanel();
         JLabel temporary = new JLabel("<html>(temporary)<br>choose machine to browse</html>");
         temporary.setForeground(Color.WHITE);
         machinePanel.add(temporary);
         machineSelect = new JComboBox(this.controller.getMachineIDs());
         machineSelect.setPreferredSize(new Dimension(50, 30));
         machinePanel.add(machineSelect);
        north.add(machinePanel, BorderLayout.EAST);
        
        
        
        
        
        
        JLabel image2 = new JLabel();
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource("/Images/ExtraVision.jpeg"));
            img = img.getScaledInstance(windowSize.width / 2,windowSize.height / 8 , java.awt.Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            System.out.println("Image erro");
        }
        image2.setIcon(new ImageIcon(img));
        north.add (image2);
        image2.setHorizontalAlignment(SwingConstants.CENTER);
        
        
        
         
       JPanel center = new JPanel();
       this.add(center,BorderLayout.CENTER);
       
       center.setLayout(new BorderLayout());
       
       
       JPanel south = new JPanel();
       this.add(south,BorderLayout.SOUTH);
    
        south.setPreferredSize(new Dimension(windowSize.width, windowSize.height /10));
       
     
   
       JLabel image = new JLabel();
            image.setIcon(new ImageIcon(getClass().getResource("/Images/HomeScreem.jpeg")));
            center.add (image,BorderLayout.CENTER);
            image.setHorizontalAlignment(SwingConstants.CENTER);
            JPanel buttonPanel = new JPanel();
            center.add(buttonPanel,BorderLayout.SOUTH);
       
       
       
       
       JButton rent = new JButton("RENT");
       buttonPanel.add (rent);
       JButton returnButton = new JButton("RETURN");
       buttonPanel.add (returnButton);
       
       changeButtons(this);
       
       rent.setActionCommand("Go to rent home screem");
       returnButton.setActionCommand("Go to return home screem");
       
       
       
      }
     
    
    private void changeButtons(JPanel panel){
        panel.setBackground(new Color(9,9,9));
        Component [] components = panel.getComponents();
        for(Component c: components){
            if (c instanceof JButton) {
               Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
                c.setPreferredSize (new Dimension(windowSize.width /6 , windowSize.height /7));
                c.setFont(new Font(Font.SERIF,Font.BOLD,60));
                c.setForeground(new Color(255,210,25));
                ((JButton) c).setBorder(BorderFactory.createLineBorder(Color.WHITE,4,true));
                c.setCursor(new Cursor(Cursor.HAND_CURSOR));
                ((JButton) c).addActionListener(this.controller);
                
            ///    c.setBackground(new Color(243,242,237));
                c.setBackground(null);
            
            }  
 
            if (c instanceof JPanel)
               changeButtons ((JPanel)c);
            
            
            
        }
        
        
        
        
        
        
    }
   
    
    public static String getmachineSelect() {
        return machineSelect.getSelectedItem().toString();

    }
    
    
    
    
    
    }
    
    
    
    
    
 
