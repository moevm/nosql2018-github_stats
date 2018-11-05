package example.model.mongo;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class Course {
    @Id
    private String name;

    private List<Repository> repositories;

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    private String lastUpdated;

    public Course(){
        this.repositories = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Repository> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }
}
