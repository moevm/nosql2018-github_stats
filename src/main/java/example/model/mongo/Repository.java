package example.model.mongo;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    @Id
    private String name;

    private String owner;

    private List<Contributor> contributors;

    public Repository() {
        this.contributors = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
