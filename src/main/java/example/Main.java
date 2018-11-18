package example;

import example.model.mongo.Contributor;
import example.model.mongo.Course;
import example.model.mongo.MongoCommit;
import example.model.mongo.Repository;
import example.repository.CourseRepository;
import example.services.CommitsService;
import example.services.ContributorService;
import example.services.CourseService;
import example.services.RepositoryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private CourseService courseService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ContributorService contributorService;

    @Autowired
    private CommitsService commitsService;


    @Autowired
    private CourseRepository courseRepository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {

        Course course = courseRepository.findAll().get(0);

        repositoryService.deleteRepository(course.getId(),
                new ObjectId("5bf1c60093c2fe44519e791d"));

        Repository repository = new Repository();
        repository.setId(new ObjectId());
        repository.setOwner("moevm");
        repository.setName("oop");
        repository.setContributors(new ArrayList<>());

        //courseService.createCourse("TestCourse", Collections.singletonList(repository));

        repositoryService.addRepository(course.getId(), repository);
    }

}