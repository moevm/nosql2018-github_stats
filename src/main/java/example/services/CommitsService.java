package example.services;

import example.model.mongo.MongoCommit;
import example.repository.CourseRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommitsService {
    @Autowired
    CourseRepository courseRepository;

    public List<MongoCommit> getCommits(ObjectId courseId,
                                        ObjectId repositoryId,
                                        String contributorName){
        List commits = null;
        try {
            commits = courseRepository
                    .findByIdAndRepositoryIdAndContributorName(courseId,
                            repositoryId,
                            contributorName)
                    .get()
                    .getRepositories()
                    .get(0)
                    .getContributors()
                    .get(0)
                    .getCommits();
        } catch (NoSuchElementException e){
            System.out.println("No such file");
        }
        return commits;
    }
}
