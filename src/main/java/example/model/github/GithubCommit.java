package example.model.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubCommit {
    @Id
    private String date;

    private String contributor;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("commit")
    private void unpackNested(Map<String,Object> commit) {
        Map<String, String> author = (Map<String,String>)commit.get("author");
        this.date = author.get("date");
        this.contributor = author.get("name");
    }
}
