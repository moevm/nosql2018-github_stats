package example;

import example.model.mongo.Course;
import example.model.mongo.Repository;
import example.repository.CourseRepository;
import example.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {

        Repository first = new Repository();
        first.setName("Test_for_NoSQL");
        first.setOwner("vender98");

        Course course = new Course();
        course.setName("TestCourse");
        course.setLastUpdated("2017-11-05T18:07:36Z");
        course.getRepositories().add(first);

        Optional<Course> mongoCourse = courseRepository.findById(course.getName());
        course.setLastUpdated(mongoCourse.get().getLastUpdated());

        courseService.updateCourse(course);

    }

}