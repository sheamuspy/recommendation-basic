package org.recommend.recommendbasic.engine;

import org.springframework.data.annotation.Id;

/**
 * Created by sheamus on 8/15/2017.
 */
public class Recommendation {

    @Id
    private String id;
    private String user;
    private String item;
    private Double weight;

    public Recommendation(){}

    public Recommendation(String user, String item, Double weight ){
        this.user = user;
        this.item = item;
        this.weight = weight;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
