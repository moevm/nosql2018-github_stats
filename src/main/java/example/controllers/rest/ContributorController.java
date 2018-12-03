package example.controllers.rest;

import example.constants.ItemType;
import example.constants.ParamNames;
import example.constants.ParamValues;
import example.model.mongo.Contributor;
import example.services.AnalyzedEntityService;
import example.utils.GraphDataParse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
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
    @RequestMapping(value = "/getItems", method = RequestMethod.POST)
    public Map getItems(@RequestBody Map<String, Object> body, HttpServletResponse httpServletResponse){
        Map<String, Object> response = new HashMap<>();
        Contributor contributor = null;
        ItemType type = null;

        try {
            ObjectId courseId = new ObjectId((String) body.get(ParamNames.COURSE_ID_KEY));
            ObjectId repositoryId = new ObjectId((String) body.get(ParamNames.REPOSITORY_ID_KEY));
            type = ItemType.Companion.getByName((String) body.get(ParamNames.ITEM_TYPE));
            String contributorName = (String) body.get(ParamNames.CONTRIBUTOR_NAME_KEY);
            contributor = analyzedEntityService.getContributor(courseId, repositoryId, contributorName);
        } catch (Exception e){
            e.printStackTrace();
        }

        if (contributor != null){
            response.put(ParamNames.RESULT_KEY, GraphDataParse.INSTANCE.parseContributorToData(contributor, type));
        } else {
            response.put(ParamNames.ERROR_KEY, ParamValues.COURSE_OR_REPOSITORY_OR_CONTRIBUTOR_DOES_NOT_EXIST);
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return response;
    }
}
