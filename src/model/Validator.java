/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Thyago De Oliveira Alves
 * @author José Felipe Flores da Silva
 */
public class Validator {

    /**
     * Validates email address
     * @param input email address
     * @return if is valid email address
     */
    public static boolean isValidEmail(String input) {
        if (input == null || input.equals("")) {
            return false;
        }
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    
    /**
     * Validates password
     * @param password password to be checked
     * @return if is valid password
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.equals("")) {
            return false;
        }
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    
    /**
     * Validates credit card number
     * @param card card to be checked
     * @return if is valid credit card number
     */
    public static boolean isValidCreditCard(String card) {
        if (card == null || card.equals("")) {
            return false;
        }
        String regex = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|(?<mastercard>5[1-5][0-9]{14}))$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(card);
        return matcher.matches();
    }
    
    
}
