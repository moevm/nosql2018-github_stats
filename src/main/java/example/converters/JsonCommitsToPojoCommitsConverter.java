package example.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.model.Commit;

import java.io.IOException;
import java.util.List;

public class JsonCommitsToPojoCommitsConverter {


    public static Commit convertSingleCommit (String jsonCommit){
        ObjectMapper mapper = new ObjectMapper();
        Commit commit = null;
        try {
            commit = mapper.readValue(jsonCommit, Commit.class);
            return commit;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Commit> convertMultipleCommits (String jsonCommits){
        ObjectMapper mapper = new ObjectMapper();
        List<Commit> commits = null;
        try {
            commits = mapper.readValue(jsonCommits, mapper.getTypeFactory().constructCollectionType(List.class, Commit.class));
            return commits;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
