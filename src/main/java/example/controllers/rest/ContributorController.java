package example.controllers.rest;

import example.constants.Constant;
import example.constants.ParamNames;
import example.constants.ParamValues;
import example.model.mongo.abstractEntity.AnalyzedEntity;
import example.services.AnalyzedEntityService;
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
    AnalyzedEntityService analyzedEntityService;

    /*Get commits of contributor
     * PARAMS:
     *
     * courseId: String
     * repositoryId: String
     * contributorName: String
     *
     * RETURN VALUE:
     *
     * commits: List<AnalyzedEntity>
     *      or
     * error: String*/
    @RequestMapping(value = "/getCommits", method = RequestMethod.POST)
    public Map getCommits(@RequestBody Map<String, Object> body,
                               HttpServletResponse httpServletResponse){
        Map<String, Object> response = new HashMap<>();
        List<? extends AnalyzedEntity> commits = null;

        try {
            ObjectId courseId = new ObjectId((String) body.get(ParamNames.COURSE_ID_KEY));
            ObjectId repositoryId = new ObjectId((String) body.get(ParamNames.REPOSITORY_ID_KEY));
            String contributorName = (String) body.get(ParamNames.CONTRIBUTOR_NAME_KEY);
            commits = analyzedEntityService.getAnalyzedEntities(courseId, repositoryId, contributorName, Constant.COMMIT_KEY);
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

    /*Get issues of contributor
     * PARAMS:
     *
     * courseId: String
     * repositoryId: String
     * contributorName: String
     *
     * RETURN VALUE:
     *
     * commits: List<AnalyzedEntity>
     *      or
     * error: String*/
    @RequestMapping(value = "/getIssues", method = RequestMethod.POST)
    public Map getIssues(@RequestBody Map<String, Object> body,
                          HttpServletResponse httpServletResponse){
        Map<String, Object> response = new HashMap<>();
        List<? extends AnalyzedEntity> issues = null;

        try {
            ObjectId courseId = new ObjectId((String) body.get(ParamNames.COURSE_ID_KEY));
            ObjectId repositoryId = new ObjectId((String) body.get(ParamNames.REPOSITORY_ID_KEY));
            String contributorName = (String) body.get(ParamNames.CONTRIBUTOR_NAME_KEY);
            issues = analyzedEntityService.getAnalyzedEntities(courseId, repositoryId, contributorName, Constant.ISSUE_KEY);
        } catch (Exception e){
            e.printStackTrace();
        }

        if (issues != null){
            response.put(ParamNames.ISSUES_KEY, issues);
        } else {
            response.put(ParamNames.ERROR_KEY, ParamValues.COURSE_OR_REPOSITORY_OR_CONTRIBUTOR_DOES_NOT_EXIST);
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return response;
    }

    /*Get pull requests of contributor
     * PARAMS:
     *
     * courseId: String
     * repositoryId: String
     * contributorName: String
     *
     * RETURN VALUE:
     *
     * commits: List<AnalyzedEntity>
     *      or
     * error: String*/
    @RequestMapping(value = "/getPullRequests", method = RequestMethod.POST)
    public Map getPullRequests(@RequestBody Map<String, Object> body,
                          HttpServletResponse httpServletResponse){
        Map<String, Object> response = new HashMap<>();
        List<? extends AnalyzedEntity> pullRequests = null;

        try {
            ObjectId courseId = new ObjectId((String) body.get(ParamNames.COURSE_ID_KEY));
            ObjectId repositoryId = new ObjectId((String) body.get(ParamNames.REPOSITORY_ID_KEY));
            String contributorName = (String) body.get(ParamNames.CONTRIBUTOR_NAME_KEY);
            pullRequests = analyzedEntityService.getAnalyzedEntities(courseId, repositoryId, contributorName, Constant.PULL_REQUEST_KEY);
        } catch (Exception e){
            e.printStackTrace();
        }

        if (pullRequests != null){
            response.put(ParamNames.PULL_REQUESTS_KEY, pullRequests);
        } else {
            response.put(ParamNames.ERROR_KEY, ParamValues.COURSE_OR_REPOSITORY_OR_CONTRIBUTOR_DOES_NOT_EXIST);
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return response;
    }
}
