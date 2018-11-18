package example.services;

import example.constants.Constant;
import example.database.MongoDB;
import example.model.mongo.Course;
import example.model.mongo.Repository;
import example.repository.CourseRepository;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CourseService {
    @Autowired
    RepositoryService repositoryService;

    @Autowired
    CourseRepository courseRepository;

    public void updateCourse(ObjectId id){

        Course course = courseRepository.findById(id.toString()).get();

        String oldLastUpdate = course.getLastUpdate();
        String newLastUpdate = LocalDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));

        updateLastUpdate(id, newLastUpdate);

        try {
            repositoryService.updateRepositories(course.getRepositories(), oldLastUpdate);
        } catch (Exception e){
            updateLastUpdate(id, oldLastUpdate);
        }
    }

    public ObjectId createCourse(String name, List<Repository> repositories){
        Course course = new Course();
        ObjectId courseId = new ObjectId();
        course.setName(name);
        course.setId(courseId);
        course.setLastUpdate(Constant.INITIAL_LAST_UPDATE);
        course.setRepositories(repositories);

        courseRepository.save(course);
        updateCourse(courseId);

        return courseId;
    }

    public void updateLastUpdate(ObjectId courseId, String lastUpdate){
        Bson where = new Document()
                .append("_id", courseId);
        Bson update = new Document()
                .append("$set", new Document()
                        .append("lastUpdate", lastUpdate));
        MongoDB.courses.updateOne(where, update);
    }

    public void deleteCourse(ObjectId courseId){
        Bson where = new Document()
                .append("_id", courseId);

        MongoDB.courses.deleteOne(where);
    }
}
