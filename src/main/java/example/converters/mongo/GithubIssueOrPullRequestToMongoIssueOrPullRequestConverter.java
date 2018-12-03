package example.converters.mongo;

import example.constants.Constant;
import example.model.github.GithubIssueOrPullRequest;
import example.model.mongo.MongoIssue;
import example.model.mongo.MongoPullRequest;

import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;

public class GithubIssueOrPullRequestToMongoIssueOrPullRequestConverter {
    public static Map<String, Object> convert(GithubIssueOrPullRequest issueOrPullRequest) {
        Map<String, Object> mongoIssueOrPullRequest = new HashMap<>();

        if (issueOrPullRequest.isPullRequest()) {
            mongoIssueOrPullRequest.put(Constant.PULL_REQUEST_KEY,
                    new MongoPullRequest(DatatypeConverter.parseDate(issueOrPullRequest.getDate()).getTime()));
        } else {
            mongoIssueOrPullRequest.put(Constant.ISSUE_KEY,
                    new MongoIssue(DatatypeConverter.parseDate(issueOrPullRequest.getDate()).getTime()));
        }

        return mongoIssueOrPullRequest;
    }
}
