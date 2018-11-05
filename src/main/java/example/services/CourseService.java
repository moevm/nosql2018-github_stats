package example.services;

import example.model.mongo.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class CourseService {
    @Autowired
    RepositoryService repositoryService;

    public void updateCourse(Course course){
        String newLastUpdated = LocalDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));

        repositoryService.updateRepositories(course.getRepositories(),
                course.getLastUpdated());

        course.setLastUpdated(newLastUpdated);
    }
}
