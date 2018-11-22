package example.controllers.rest;

import example.constants.ParamNames;
import example.constants.ParamValues;
import example.model.mongo.Course;
import example.model.mongo.Repository;
import example.services.CourseService;
import example.services.RepositoryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course")
@ResponseBody
public class CourseController {
    @Autowired
    CourseService courseService;

    @Autowired
    RepositoryService repositoryService;

    /*Add new course
     * PARAMS:
     *
     * courseName: String
     * repositories: List of objects with params
     *      repositoryName: String
     *      repositoryOwner: String
     *
     * RETURN VALUE:
     *
     * course: Course
     *      or
     * error: String*/
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Map addCourse(@RequestBody Map<String, Object> body){
        Map<String, Object> response = new HashMap<>();
        List<Repository> repositories = new ArrayList<>();

        List<Map<String, String>> requestBodyRepositories =
                ((List) body.get(ParamNames.REPOSITORIES_KEY));
        requestBodyRepositories.forEach(requestBodyRepository -> {
            Repository mongoRepository = new Repository();
            mongoRepository.setId(new ObjectId());
            mongoRepository.setName(requestBodyRepository.get(ParamNames.REPOSITORY_NAME_KEY));
            mongoRepository.setOwner(requestBodyRepository.get(ParamNames.REPOSITORY_OWNER_KEY));
            mongoRepository.setContributors(new ArrayList<>());
            repositories.add(mongoRepository);
        });

        if (!repositoryService.repositoriesExists(repositories)){
            response.put(ParamNames.ERROR_KEY, ParamValues.REPOSITORY_DOES_NOT_EXIST);
        } else {
            Course course = courseService
                    .createCourse((String) body
                            .get(ParamNames.COURSE_NAME_KEY), repositories);
            response.put(ParamNames.COURSE_KEY, course);
        }

        return response;
    }

    @RequestMapping(value = "/getRepositoryNames", method = RequestMethod.GET)
    public Map getRepositoryNames(@RequestBody Map<String, Object> body,
                                  HttpServletResponse httpServletResponse){
        Map<String, Object> response = new HashMap<>();
        ObjectId courseId ;
        List<String> repositories = null;

        try {
            courseId = new ObjectId((String) body.get(ParamNames.COURSE_ID_KEY));
            repositories = repositoryService.getRepositoryNames(courseId);
        } catch (Exception e){
            e.printStackTrace();
        }

        if (repositories != null){
            response.put(ParamNames.REPOSITORY_NAMES_KEY, repositories);
        } else {
            response.put(ParamNames.ERROR_KEY, ParamValues.COURSE_DOES_NOT_EXIST);
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return response;
    }

    @RequestMapping(value = "/updateCourse", method = RequestMethod.PUT)
    public Map updateCourse(@RequestBody Map<String, Object> body,
                            HttpServletResponse httpServletResponse){
        Map<String, Object> response = new HashMap<>();
        ObjectId courseId;
        Course course = null;

        try {
            courseId = new ObjectId((String) body.get(ParamNames.COURSE_ID_KEY));
            courseService.updateCourse(courseId);
            course = courseService.getCourse(courseId);
        } catch (Exception e){
            e.printStackTrace();
        }

        if (course != null){
            response.put(ParamNames.COURSE_KEY, course);
        } else {
            response.put(ParamNames.ERROR_KEY, ParamValues.COURSE_DOES_NOT_EXIST);
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return response;
    }

    @RequestMapping(value = "/deleteCourse", method = RequestMethod.DELETE)
    public Map deleteCourse(@RequestBody Map<String, Object> body,
                            HttpServletResponse httpServletResponse){
        Map<String, Object> response = new HashMap<>();
        ObjectId courseId;

        try {
            courseId = new ObjectId((String) body.get(ParamNames.COURSE_ID_KEY));
            courseService.deleteCourse(courseId);
            response.put(ParamNames.COURSE_ID_KEY, courseId.toString());
        } catch (Exception e){
            e.printStackTrace();
            response.put(ParamNames.ERROR_KEY, ParamValues.COURSE_DOES_NOT_EXIST);
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return response;
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public Map getAllCourses(){
        Map<String, Object> response = new HashMap<>();

        List<Course> courses = courseService.getCourseCollection();

        response.put(ParamNames.COURSES_KEY, courses);

        return response;
    }

    @RequestMapping(value = "/getRepositories", method = RequestMethod.GET)
    public Map getRepositories(@RequestBody Map<String, Object> body,
                                  HttpServletResponse httpServletResponse){
        Map<String, Object> response = new HashMap<>();
        ObjectId courseId ;
        List<Repository> repositories = null;

        try {
            courseId = new ObjectId((String) body.get(ParamNames.COURSE_ID_KEY));
            repositories = repositoryService.getRepositories(courseId);
        } catch (Exception e){
            e.printStackTrace();
        }

        if (repositories != null){
            response.put(ParamNames.REPOSITORIES_KEY, repositories);
        } else {
            response.put(ParamNames.ERROR_KEY, ParamValues.COURSE_DOES_NOT_EXIST);
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return response;
    }
}
