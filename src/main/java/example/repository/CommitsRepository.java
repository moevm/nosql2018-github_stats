package example.repository;

import example.model.Commit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CommitsRepository extends MongoRepository<Commit, String> {
    @Query ("{date: '2017-03-26T20:55:16Z'}")
    List<Commit> customQueryFindExample();
}
