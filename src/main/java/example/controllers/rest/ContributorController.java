package example.controllers.rest;

import example.constants.ParamNames;
import example.constants.ParamValues;
import example.model.mongo.MongoCommit;
import example.services.CommitsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contributor")
@ResponseBody
public class ContributorController {
    @Autowired
    CommitsService commitsService;

    /*Get commits of contributor
     * PARAMS:
     *
     * courseId: String
     * repositoryId: String
     * contributorName: String
     *
     * RETURN VALUE:
     *
     * commits: List<MongoCommit>
     *      or
     * error: String*/
    @RequestMapping(value = "/getCommits", method = RequestMethod.POST)
    public Map getCommits(@RequestBody Map<String, Object> body,
                               HttpServletResponse httpServletResponse){
        Map<String, Object> response = new HashMap<>();
        List<MongoCommit> commits = null;

        try {
            ObjectId courseId = new ObjectId((String) body.get(ParamNames.COURSE_ID_KEY));
            ObjectId repositoryId = new ObjectId((String) body.get(ParamNames.REPOSITORY_ID_KEY));
            String contributorName = (String) body.get(ParamNames.CONTRIBUTOR_NAME_KEY);
            commits = commitsService.getCommits(courseId, repositoryId, contributorName);
        } catch (Exception e){
            e.printStackTrace();
        }

        if (commits != null){
            response.put(ParamNames.COMMITS_KEY, commits);
        } else {
            response.put(ParamNames.ERROR_KEY, ParamValues.COURSE_OR_REPOSITORY_OR_CONTRIBUTOR_DOES_NOT_EXIST);
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return response;
    }
}
