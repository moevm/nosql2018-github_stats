package example;

import example.model.Customer;
import example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private CustomerRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {

        Customer newCus = new Customer("Alice", "Selezneva");

        //Insert
        repository.insert(newCus);

        //Update
        newCus.lastName = "p";
        repository.save(newCus);

        //Find
        List customers = repository.customQueryFindExample();
        customers.forEach(System.out::println);

        //Delete
        repository.customQueryDeleteExample();

    }

}