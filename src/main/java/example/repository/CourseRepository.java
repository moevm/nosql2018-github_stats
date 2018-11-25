package example.repository;

import example.model.mongo.Course;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course, String> {
    @Query(value = "{'_id': ?0, 'repositories._id': ?1}", fields = "{'repositories': 1}")
    Optional<Course> findByIdAndRepositoryId(ObjectId courseId, ObjectId repositoryId);

    @Query(value = "{'_id': ?0, 'repositories._id': ?1, 'repositories.contributors._id': ?2}",
            fields = "{'repositories.contributors': 1}")
    Optional<Course> findByIdAndRepositoryIdAndContributorName(ObjectId courseId,
                                                               ObjectId repositoryId,
                                                               String contributorName);

    @Query(value = "{}", fields = "{'name': 1}")
    Optional<List<Course>> findCourseNames();

    @Query(value = "{'_id': ?0}", fields = "{'repositories.name': 1}")
    Optional<Course> findRepositoryNames(ObjectId courseId);

    @Query(value = "{'_id': ?0, 'repositories._id': ?1}", fields = "{'repositories.contributors._id': 1}")
    Optional<Course> findContributorNames(ObjectId courseId, ObjectId repositoryId);
}
