package example.repository;

import java.util.List;

import example.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface CustomerRepository extends MongoRepository<Customer, String> {

    @Query("{firstName: 'Alice'}")
    List<Customer> customQueryFindExample();

    @Query(value = "{firstName: 'Alice'}", delete = true)
    void customQueryDeleteExample();

}