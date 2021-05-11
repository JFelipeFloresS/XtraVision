/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.screens;

import controller.Controller;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author thyag
 */
public class ReturnHomeScreem extends JPanel {

    private final Controller controller;
    private static JTextField idInput;

    public ReturnHomeScreem(Controller controller) {
        this.controller = controller;

        ReturnHomeScreem.idInput = new JTextField(20);

        JButton returnDvd = new JButton("Return DVD");
        returnDvd.setActionCommand("return DVD");
        returnDvd.addActionListener(this.controller);
        JLabel label = new JLabel("Insert The DVD ID:");
        label.setForeground(Color.WHITE);

        this.add(label);
        this.add(ReturnHomeScreem.idInput);
        this.add(returnDvd);

    }

    public static String getReturnIDInput() {
        return ReturnHomeScreem.idInput.getText();
    }

}
