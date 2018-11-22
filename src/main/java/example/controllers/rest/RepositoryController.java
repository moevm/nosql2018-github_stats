package example.controllers.rest;

import example.constants.ParamNames;
import example.constants.ParamValues;
import example.model.mongo.Contributor;
import example.services.ContributorService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/repository")
@ResponseBody
public class RepositoryController {
    @Autowired
    ContributorService contributorService;

    /*Get contributor list of repository
    * PARAMS:
    *
    * courseId: String
    * repositoryId: String
    *
    * RETURN VALUE:
    *
    * contributors: List of contributors
    *      or
    * error: String*/
    @RequestMapping(value = "/getContributors", method = RequestMethod.GET)
    public Map getContributors(@RequestBody Map<String, Object> body,
                                  HttpServletResponse httpServletResponse){
        Map<String, Object> response = new HashMap<>();
        ObjectId courseId;
        ObjectId repositoryId;
        List<Contributor> contributors = null;

        try {
            courseId = new ObjectId((String) body.get(ParamNames.COURSE_ID_KEY));
            repositoryId = new ObjectId((String) body.get(ParamNames.REPOSITORY_ID_KEY));
            contributors = contributorService.getContributors(courseId, repositoryId);
        } catch (Exception e){
            e.printStackTrace();
        }

        if (contributors != null){
            response.put(ParamNames.CONTRIBUTORS_KEY, contributors);
        } else {
            response.put(ParamNames.ERROR_KEY, ParamValues.COURSE_OR_REPOSITORY_DOES_NOT_EXIS);
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return response;
    }
}
