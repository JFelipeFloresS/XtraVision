/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.customer;

/**
 *
 * @author José Felipe Flores da Silva
 */
public enum Loyalty {
    NONE("none", -1),
    STANDARD("standard", 15),
    PREMIUM("premium", 10),
    ULTIMATE("ultimate", 5);
    
    private final String name;
    private final int numberOfMovies;
    
    Loyalty(String loyalName, int num) {
        name = loyalName;
        numberOfMovies = num;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfMovies() {
        return numberOfMovies;
    }
    
    public static Loyalty getLoyalty(String loyalName) {
        for(Loyalty l: Loyalty.values()) {
            if (l.getName().equalsIgnoreCase(loyalName)) {
                return l;
            }
        }
        return Loyalty.NONE;
    }
    
    public static boolean isNextFree(String loyalName, int rentedMovies) {
        Loyalty loyalty = getLoyalty(loyalName);
        return rentedMovies == loyalty.getNumberOfMovies();
    }
}
