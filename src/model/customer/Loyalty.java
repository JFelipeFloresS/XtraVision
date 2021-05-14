/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.customer;

/**
 * @author Thyago De Oliveira Alves
 * @author José Felipe Flores da Silva
 */
public enum Loyalty {
    NONE("none", -1, 0),
    STANDARD("standard", 15, 30),
    PREMIUM("premium", 10, 50),
    ULTIMATE("ultimate", 5, 0);
    
    private final String name;
    private final int numberOfMovies;
    private final int promotionThreshold;
    
    Loyalty(String loyalName, int num, int threshold) {
        name = loyalName;
        numberOfMovies = num;
        promotionThreshold = threshold;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfMovies() {
        return numberOfMovies;
    }
    
    public int getPromotionThreshold() {
        return promotionThreshold;
    }
    
    public static Loyalty getLoyalty(String loyalName) {
        for(Loyalty l: Loyalty.values()) {
            if (l.getName().equalsIgnoreCase(loyalName)) {
                return l;
            }
        }
        return Loyalty.NONE;
    }
    
    public static Loyalty promoteLoyalty(Loyalty loyalty) {
        switch(loyalty) {
            case STANDARD:
                return PREMIUM;
            case PREMIUM:
                return ULTIMATE;
            default:
                return loyalty;
        }
        
        
    }
    
}
