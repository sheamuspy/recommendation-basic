package org.recommend.recommendbasic.engine;

import org.springframework.data.annotation.Id;

/**
 * Created by sheamus on 8/14/2017.
 */
public class Rater {

    @Id
    private String id;
    private String rater;
    private String rated;
    private String rating;

    public Rater(){}

    public Rater(String rater, String rated, String rating){
        this.rater = rater;
        this.rated = rated;
        this.rating = rating;
    }

    @Override
    public String toString(){
        return String.format("Object: %s, rated item: %s as: %s",
                rater, rated, rating);
    }

}
