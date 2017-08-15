package org.recommend.recommendbasic.engine;

import org.springframework.data.annotation.Id;

/**
 * Created by sheamus on 8/14/2017.
 */
public class User {

    @Id
    public String id;

    public String firstName;
    public String lastName;

    public User(){}

    public User(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString(){
        return  String.format(
                "Customer[id=%s, firstname='%s', lastname='%s']",
                id, firstName, lastName);
    }
}