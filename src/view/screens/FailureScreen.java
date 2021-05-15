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
public class FailureScreen extends SuccessFailureScreen {

    public FailureScreen() {
        super();
    }
    
    @Override
    protected void setImageMessageTitle() {
        this.imgString = "/Images/failure.jpeg";
        this.message = "There seems to be a problem with your order...";
        this.title = "Oops...";
    }
    
}
