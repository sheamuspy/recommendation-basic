package org.recommend.recommendbasic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RecommendBasicApplication implements CommandLineRunner{

    @Autowired
    private CustomerRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(RecommendBasicApplication.class, args);
	}

    @Override
    public void run(String... strings) throws Exception {

	    repository.deleteAll();

	    //save a couple of customers
        repository.save(new Customer("Alice", "Smith"));
        repository.save(new Customer("Bob", "Smith"));

        //fetch all customers
        System.out.println("Customer found with findAll()");
        System.out.println("-----------------------------");
        for (Customer customer: repository.findByFirstNameAndLastName("Alice", "Smith")){
            System.out.println(customer);
        }

    }
}
