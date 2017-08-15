package org.recommend.recommendbasic.engine;

import org.springframework.data.annotation.Id;

/**
 * Created by sheamus on 8/14/2017.
 */
public class Rater {

    @Id
    private String id;
    private String user;
    private String item;
    private String rating;

    public Rater(){}

    public Rater(String user, String item, String rating){
        this.user = user;
        this.item = item;
        this.rating = rating;
    }

    @Override
    public String toString(){
        return String.format("Object: %s, rated item: %s as: %s",
                user, item, rating);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
