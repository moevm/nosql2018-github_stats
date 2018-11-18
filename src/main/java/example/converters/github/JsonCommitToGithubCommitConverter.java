package example.converters.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.model.github.GithubCommit;

import java.util.List;

public class JsonCommitToGithubCommitConverter {

    public static List<GithubCommit> convert (String jsonCommits){
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
}
