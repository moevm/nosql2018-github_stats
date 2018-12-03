package example.model.mongo;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class Contributor {
    @Id
    private String name;

    private List<MongoCommit> commits;

    private List<MongoIssue> issues;

    private List<MongoPullRequest> pullRequests;

    public Contributor(){
        commits = new ArrayList<>();
        issues = new ArrayList<>();
        pullRequests = new ArrayList<>();
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

    public List<MongoIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<MongoIssue> issues) {
        this.issues = issues;
    }

    public List<MongoPullRequest> getPullRequests() {
        return pullRequests;
    }

    public void setPullRequests(List<MongoPullRequest> pullRequests) {
        this.pullRequests = pullRequests;
    }
}
