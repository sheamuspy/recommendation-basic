package org.recommend.recommendbasic;

/**
 * Created by sheamus on 8/18/2017.
 */
public class IRater {

    private String user;
    private String item;
    private String rating;

    public IRater(){}

    public IRater(String user, String item, String rating){
        this.user = user;
        this.item = item;
        this.rating = rating;
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
