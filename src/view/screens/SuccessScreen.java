/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.screens;

/**
 *
 * @author thyag
 */
public class SuccessScreen extends SuccessFailureScreen {
    
    public SuccessScreen() {
        super();
    }

    @Override
    protected void setImageMessageTitle() {
        this.imgString = "/Images/success.jpeg";
        this.message = "<html>Your rental was successful!<br>Thank you for using Xtra-Vision!</html>";
        this.title = "Success!!";
    }
    
}
