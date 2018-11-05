package example.repository;

import example.model.mongo.Contributor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContributorsRepository extends MongoRepository<Contributor, String> {
}
