package example.model.mongo;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class Contributor {
    @Id
    private String name;

    private List<MongoCommit> commits;

    public Contributor(){
        commits = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MongoCommit> getCommits() {
        return commits;
    }

    public void setCommits(List<MongoCommit> commits) {
        this.commits = commits;
    }
}
