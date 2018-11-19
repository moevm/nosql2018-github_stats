package example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CourseAddingBody {

    @JsonProperty("courseName")
    private String courseName;

    @JsonProperty("repositories")
    private List<CourseAddingBodyRepository> repositories;

    public CourseAddingBody(){}

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<CourseAddingBodyRepository> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<CourseAddingBodyRepository> repositories) {
        this.repositories = repositories;
    }
}
