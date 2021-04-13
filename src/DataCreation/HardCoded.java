/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCreation;

import java.util.ArrayList;
import model.movie.Movie;

/**
 *
 * @author José Felipe Flores da Silva
 */
public class HardCoded {

    ArrayList< Movie> movies;

    public HardCoded() {
        
        this.movies = new ArrayList<>(); 

        this.movies.add(new Movie(1, "Avergers 1","Action","-14",true,"https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg") ); 
        this.movies.add(new Movie(1, "Avergers 2","Action","-14",true,"https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg") ); 
        this.movies.add(new Movie(1, "Avergers 3","Action","-14",true,"https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg") ); 
        this.movies.add(new Movie(1, "Avergers 4","Action","-14",true,"https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg") ); 
        this.movies.add(new Movie(1, "Avergers 5","Action","-14",true,"https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg") ); 
        this.movies.add(new Movie(1, "Avergers 6","Action","-14",true,"https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg") ); 
    }
    public ArrayList getMovies(){ 
                           
        return this.movies;
    
    }
    
        
        
        
        
        
    
}
