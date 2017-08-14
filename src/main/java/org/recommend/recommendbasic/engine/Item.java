package org.recommend.recommendbasic.engine;

import org.springframework.data.annotation.Id;

/**
 * Created by sheamus on 8/14/2017.
 */
public class Item {

    @Id
    private String id;

    private String name;
    private String description;


    public Item(){}

    public Item(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        return String.format("ITEM: %s, DESCRIPTION: %s",
                name, description);
    }
}
