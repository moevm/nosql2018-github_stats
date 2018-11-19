package example.controllers.rest;

import example.model.mongo.Course;
import example.model.mongo.Repository;
import example.model.rest.CourseAddingBody;
import example.services.CourseService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Map addCourse(@RequestBody CourseAddingBody body){
        Map<String, Object> response = new HashMap<>();
        List<Repository> repositories = new ArrayList<>();

        body.getRepositories().forEach(requestBodyRepository -> {
            Repository mongoRepository = new Repository();
            mongoRepository.setId(new ObjectId());
            mongoRepository.setName(requestBodyRepository.getRepositoryName());
            mongoRepository.setOwner(requestBodyRepository.getRepositoryOwner());
            mongoRepository.setContributors(new ArrayList<>());
            repositories.add(mongoRepository);
        });

        Course course = courseService.createCourse(body.getCourseName(), repositories);
        response.put("course", course);

        return response;
    }
}
