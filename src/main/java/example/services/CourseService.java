package example.services;

import example.constants.Constant;
import example.database.MongoDB;
import example.model.mongo.Course;
import example.model.mongo.IdAndName;
import example.model.mongo.Repository;
import example.repository.CourseRepository;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    RepositoryService repositoryService;

    @Autowired
    CourseRepository courseRepository;

    public List<Course> getCourseCollection(){
        return courseRepository.findAll();
    }

    public Course getCourse(ObjectId courseId){
        return courseRepository.findById(courseId.toString()).orElse(null);
    }

    public boolean updateCourse(ObjectId id) {

        Course course = courseRepository.findById(id.toString()).get();

        if (!repositoryService.areRepositoriesExists(course.getRepositories())){
            return false;
        }

        Date oldLastUpdate = course.getLastUpdate();
        Date newLastUpdate = null;
        try {
            newLastUpdate = new SimpleDateFormat(Constant.DATE_PATTERN).parse(LocalDateTime.now(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern(Constant.DATE_PATTERN)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        updateLastUpdate(id, newLastUpdate);

        try {
            repositoryService.updateRepositories(id, course.getRepositories(), oldLastUpdate);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            updateLastUpdate(id, oldLastUpdate);
            return false;
        }
    }

    public Course createCourse(String name, List<Repository> repositories) {
        ObjectId courseId = new ObjectId();

        try {
            List<Document> mongoRepositories = new ArrayList<>();
            repositories.forEach(repository -> mongoRepositories.add(new Document()
                    .append("_id", repository.getId())
                    .append("name", repository.getName())
                    .append("owner", repository.getOwner())
                    .append("contributors", repository.getContributors())));

            Document course = new Document()
                    .append("_id", courseId)
                    .append("name", name)
                    .append("lastUpdate", new SimpleDateFormat(Constant.DATE_PATTERN).parse(Constant.INITIAL_LAST_UPDATE))
                    .append("repositories", mongoRepositories);

            MongoDB.courses.insertOne(course);

            updateCourse(courseId);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return courseRepository.findById(courseId.toString()).get();
    }

    public void updateLastUpdate(ObjectId courseId, Date lastUpdate){
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

    public List<IdAndName> getCourseNames(){
        Optional<List<Course>> courses = courseRepository.findCourseNames();
        List<IdAndName> courseNames = new ArrayList<>();
        if (courses.isPresent()){
            courseNames = courses.get()
                    .stream()
                    .map(course -> new IdAndName(course.getId().toString(), course.getName()))
                    .collect(Collectors.toList());
        }

        return courseNames;
    }
}
