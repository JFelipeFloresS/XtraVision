/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.frame;

import controller.Controller;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import view.screens.HomeScreen;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class Frame extends JFrame {

    private final Controller controller;
    private Dimension windowSize;

    public Frame(Controller controller) {
        this.controller = controller;
        setUpWindow();
        changePanel(new HomeScreen(this.controller));
    }

    /**
     * Window settings, such as size, visible and close default operations
     */
    private void setUpWindow() {

        this.windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(this.windowSize);
        this.setResizable(false);
        this.setTitle("XtraVision");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                closeProgram();
            }
        });
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setVisible(true);
        UIManager UI = new UIManager();
        UIManager.put("OptionPane.background", new ColorUIResource(9, 9, 9));
        UIManager.put("OptionPane.buttonPadding", 50);
        UIManager.put("OptionPane.questionIcon", null);
        UIManager.put("OptionPane.warningIcon", null);
        UIManager.put("OptionPane.messageForeground", new Color(255,255,255));
        UIManager.put("OptionPane.minimumSize", new Dimension(500, 200));
        UIManager.put("Panel.background", new ColorUIResource(9, 9, 9));
        UIManager.put("Viewport.background", new Color(255, 210, 25));
        UIManager.put("Button.foreground", new Color(255, 255, 255));
        UIManager.put("Button.background", new Color(9,9,9));
        UIManager.put("Button.border", BorderFactory.createLineBorder(new Color(255, 210, 25), 2));

    }

    /**
     * Closes DB connection before exiting the system.
     */
    private void closeProgram() {
        this.controller.closeDBConnection();
        System.exit(0);
    }

    /**
     * Removes old panel from the frame's content pane, adds the new one,
     * repaints and validates
     *
     * @param panel New panel to be added
     */
    public void changePanel(JPanel panel) {

        this.getContentPane().removeAll();
        this.add(panel);
        this.validate();
        this.repaint();

    }

    public void update() {
        this.validate();
        this.repaint();
    }

}
