package org.recommend.recommendbasic;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sheamus on 8/13/2017.
 */
public interface CustomerRepository extends MongoRepository<Customer, String> {

    public Customer findByFirstName(String firstName);
    public List<Customer> findByFirstNameAndLastName(String firstName, String lastName);

}
