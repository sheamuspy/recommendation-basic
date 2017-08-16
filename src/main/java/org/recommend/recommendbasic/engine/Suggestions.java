package org.recommend.recommendbasic.engine;

import org.springframework.data.annotation.Id;

import java.util.HashMap;

/**
 * Created by sheamus on 8/15/2017.
 */
public class Suggestions {

    @Id
    private String id;
    private String user;
    private HashMap<String, Double> items;

    public Suggestions(){}

    public Suggestions(String user, HashMap<String, Double> items){
        this.user = user;
        this.items = items;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public HashMap<String, Double> getItems() {
        return items;
    }

    public void setItems(HashMap<String, Double> items) {
        this.items = items;
    }






}
