package example.converters.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.model.github.GithubCommit;
import example.model.github.GithubIssueOrPullRequest;
import example.model.github.GithubUser;

import java.util.List;
import java.util.stream.Collectors;

public class JsonToGithubEntityConverter {
    public static List<GithubCommit> convertCommits (String jsonCommits){
        ObjectMapper mapper = new ObjectMapper();
        List<GithubCommit> githubCommits;
        try {
            githubCommits = mapper.readValue(jsonCommits, mapper.getTypeFactory().constructCollectionType(List.class, GithubCommit.class));
            return githubCommits.size() > 0 ? githubCommits : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<GithubIssueOrPullRequest> convertIssues (String jsonIssues){
        ObjectMapper mapper = new ObjectMapper();
        List<GithubIssueOrPullRequest> githubIssuesAndPullRequests;
        try {
            githubIssuesAndPullRequests = mapper.readValue(jsonIssues, mapper.getTypeFactory().constructCollectionType(List.class, GithubIssueOrPullRequest.class));
            githubIssuesAndPullRequests = githubIssuesAndPullRequests
                    .stream()
                    .filter(issueOrPullRequest ->
                            !issueOrPullRequest
                                    .getAuthorAssociation()
                                    .equalsIgnoreCase("NONE"))
                    .collect(Collectors.toList());
            return githubIssuesAndPullRequests.size() > 0 ? githubIssuesAndPullRequests : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static GithubUser convertUser (String jsonUser){
        ObjectMapper mapper = new ObjectMapper();
        GithubUser githubUser = null;
        try {
            githubUser = mapper.readValue(jsonUser, GithubUser.class);
            return githubUser;
        } catch (Exception e) {
            return null;
        }
    }
}
