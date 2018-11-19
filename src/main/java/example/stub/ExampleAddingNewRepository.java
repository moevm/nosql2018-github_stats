package example.stub;

import example.model.mongo.Course;
import example.model.mongo.Repository;
import example.repository.CourseRepository;
import example.services.RepositoryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class ExampleAddingNewRepository {
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RepositoryService repositoryService;

    public void add(){
        Course course = courseRepository.findAll().get(0);


        Repository repository = new Repository();
        repository.setId(new ObjectId());
        repository.setOwner("vender98");
        repository.setName("Test_for_NoSQL");
        repository.setContributors(new ArrayList<>());

        //courseService.createCourse("TestCourse", Collections.singletonList(repository));

        repositoryService.addRepository(course.getId(), repository);
    }
}
