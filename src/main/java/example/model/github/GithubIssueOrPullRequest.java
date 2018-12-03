package example.model.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubIssueOrPullRequest {
    @Id
    @JsonProperty("created_at")
    private String date;

    private String login;

    private boolean isPullRequest;

    @JsonProperty("author_association")
    private String authorAssociation;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isPullRequest() {
        return isPullRequest;
    }

    public void setPullRequest(boolean pullRequest) {
        isPullRequest = pullRequest;
    }

    public String getAuthorAssociation() {
        return authorAssociation;
    }

    public void setAuthorAssociation(String authorAssociation) {
        this.authorAssociation = authorAssociation;
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("user")
    private void unpackNestedUser(Map<String,String> user) {
        this.login = user.get("login");
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("pull_request")
    private void unpackNestedPR(Map<String,String> PR) {
        this.isPullRequest = true;
    }
}
