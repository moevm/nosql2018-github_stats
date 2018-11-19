package example.stub;

import example.constants.Constant;
import example.model.mongo.Course;
import example.model.mongo.Repository;
import example.repository.CourseRepository;
import example.services.CourseService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExampleCreatingNewCourse {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    public void create() throws ParseException {
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
        course.setLastUpdate(new SimpleDateFormat(Constant.DATE_PATTERN).parse(Constant.INITIAL_LAST_UPDATE));
        course.setRepositories(new ArrayList<>());
        course.getRepositories().add(first);
        courseRepository.save(course);

        //Инициализация данных по курсу
        courseService.updateCourse(courseId);

        //Получение проинициализированного курса для проверки
        Course savedCourse = courseRepository.findById(courseId.toString()).get();
    }
}
