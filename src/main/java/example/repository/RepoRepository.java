package example.repository;

import example.model.mongo.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepoRepository extends MongoRepository<Repository, String> {
}
