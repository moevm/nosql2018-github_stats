package example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseAddingBodyRepository {
    @JsonProperty("repositoryName")
    String repositoryName;

    @JsonProperty("repositoryOwner")
    String repositoryOwner;

    public CourseAddingBodyRepository(){}

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryOwner() {
        return repositoryOwner;
    }

    public void setRepositoryOwner(String repositoryOwner) {
        this.repositoryOwner = repositoryOwner;
    }
}
