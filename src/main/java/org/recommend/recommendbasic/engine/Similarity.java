package org.recommend.recommendbasic.engine;

import org.springframework.data.annotation.Id;

/**
 * Created by sheamus on 8/14/2017.
 */
public class Similarity {

    @Id
    private String id;
    private String user;
    private String other;


    private Double similarityIndex;

    public Similarity(){}

    public Similarity(String user){
        this.user = user;
    }

    public Similarity(String user, String other){
        this.user = user;
        this.other = other;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getSimilarityIndex() {
        return similarityIndex;
    }

    public void setSimilarityIndex(Double similarityIndex) {
        this.similarityIndex = similarityIndex;
    }
}
