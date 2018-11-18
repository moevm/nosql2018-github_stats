package example.stub;

import example.model.mongo.Course;
import example.model.mongo.Repository;
import example.repository.CourseRepository;
import example.services.CourseService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class ExampleCreatingNewCourse {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    public void create(){
        //Создание нового репозитория
        Repository first = new Repository();
        first.setName("Test_for_NoSQL");
        first.setOwner("vender98");
        first.setId(new ObjectId());

        //Создание нового курса с одним репозиторием и сохранение его в БД
        Course course = new Course();
        ObjectId courseId = new ObjectId();
        course.setName("TestCourse");
        course.setId(courseId);
        course.setLastUpdate("1970-01-01T00:00:00Z");
        course.setRepositories(new ArrayList<>());
        course.getRepositories().add(first);
        courseRepository.save(course);

        //Инициализация данных по курсу
        courseService.updateCourse(courseId);

        //Получение проинициализированного курса для проверки
        Course savedCourse = courseRepository.findById(courseId.toString()).get();
    }
}
