package example.controllers.rest;

import example.constants.ParamNames;
import example.constants.ParamValues;
import example.model.mongo.Contributor;
import example.model.mongo.IdAndName;
import example.services.ContributorService;
import example.utils.GraphDataParse;
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

    /*Get contributors of repository
    * PARAMS:
    *
    * courseId: String
    * repositoryId: String
    *
    * RETURN VALUE:
    *
    * contributors: List<Contributor>
    *      or
    * error: String*/
    @RequestMapping(value = "/getContributors", method = RequestMethod.POST)
    public Map getContributors(@RequestBody Map<String, Object> body,
                                  HttpServletResponse httpServletResponse){
        Map<String, Object> response = new HashMap<>();
        List<Contributor> contributors = null;

        try {
            ObjectId courseId = new ObjectId((String) body.get(ParamNames.COURSE_ID_KEY));
            ObjectId repositoryId = new ObjectId((String) body.get(ParamNames.REPOSITORY_ID_KEY));
            contributors = contributorService.getContributors(courseId, repositoryId);
        } catch (Exception e){
            e.printStackTrace();
        }

        if (contributors != null){
            response.put(ParamNames.RESULT_KEY, GraphDataParse.INSTANCE.parseRepositoryToData(contributors));
        } else {
            response.put(ParamNames.ERROR_KEY, ParamValues.COURSE_OR_REPOSITORY_DOES_NOT_EXIST);
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return response;
    }

    /*Get names of all contributors of repository
     * PARAMS:
     *
     * courseId: String
     * repositoryId: String
     *
     * RETURN VALUE:
     *
     * contributors: List<String>
     *      or
     * error: String*/
    @RequestMapping(value = "/getContributorNames", method = RequestMethod.POST)
    public Map getContributorNames(@RequestBody Map<String, Object> body,
                                  HttpServletResponse httpServletResponse){
        Map<String, Object> response = new HashMap<>();
        List<String> contributorNames = null;

        try {
            ObjectId courseId = new ObjectId((String) body.get(ParamNames.COURSE_ID_KEY));
            ObjectId repositoryId = new ObjectId((String) body.get(ParamNames.REPOSITORY_ID_KEY));
            contributorNames = contributorService.getContributorNames(courseId, repositoryId);
        } catch (Exception e){
            e.printStackTrace();
        }

        if (contributorNames != null){
            response.put(ParamNames.CONTRIBUTOR_NAMES_KEY, contributorNames);
        } else {
            response.put(ParamNames.ERROR_KEY, ParamValues.COURSE_OR_REPOSITORY_DOES_NOT_EXIST);
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return response;
    }
}
