package example.services;

import example.constants.Constant;
import example.model.mongo.Contributor;
import example.model.mongo.abstractEntity.AnalyzedEntity;
import example.repository.CourseRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AnalyzedEntityService {
    @Autowired
    CourseRepository courseRepository;

    public Contributor getContributor(ObjectId courseId, ObjectId repositoryId, String contributorName) {
        Contributor contributor = null;
        try {
            contributor = courseRepository
                    .findByIdAndRepositoryIdAndContributorName(courseId,
                            repositoryId,
                            contributorName)
                    .get()
                    .getRepositories()
                    .stream()
                    .filter(repository -> repository.getId().equals(repositoryId))
                    .collect(Collectors.toList())
                    .get(0)
                    .getContributors()
                    .stream()
                    .filter(contributor1 -> contributor1.getName().equals(contributorName))
                    .collect(Collectors.toList())
                    .get(0);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            System.out.println("No such file");
        }

        return contributor;
    }
}
