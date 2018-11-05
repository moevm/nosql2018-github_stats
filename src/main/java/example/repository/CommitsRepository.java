package example.repository;

import example.model.github.GithubCommit;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommitsRepository extends MongoRepository<GithubCommit, String> {
}
