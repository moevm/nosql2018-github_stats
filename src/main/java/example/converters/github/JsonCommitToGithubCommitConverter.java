package example.converters.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.model.github.GithubCommit;

import java.util.List;

public class JsonCommitToGithubCommitConverter {


    public static GithubCommit convertSingleCommit (String jsonCommit){
        ObjectMapper mapper = new ObjectMapper();
        GithubCommit githubCommit;
        try {
            githubCommit = mapper.readValue(jsonCommit, GithubCommit.class);
            return githubCommit;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<GithubCommit> convertMultipleCommits (String jsonCommits){
        ObjectMapper mapper = new ObjectMapper();
        List<GithubCommit> githubCommits;
        try {
            githubCommits = mapper.readValue(jsonCommits, mapper.getTypeFactory().constructCollectionType(List.class, GithubCommit.class));
            return githubCommits;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
