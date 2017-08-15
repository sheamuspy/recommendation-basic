package org.recommend.recommendbasic.engine;

import java.util.*;

/**
 * Created by sheamus on 8/14/2017.
 */
public class Similars {

    private String user;
    private HashMap<String, Double> others;

    public Similars(){}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public HashMap<String, Double> getOthers() {
        return others;
    }

    public void setOthers(HashMap<String, Double> others) {
        this.others = others;
    }
}