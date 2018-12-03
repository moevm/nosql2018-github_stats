package example.constants;

public class Constant {
    //Database
    public static final String DATABASE_NAME = "github";
    public static final String COLLECTION_NAME = "course";

    //GitHub API
    public static final String API_URL = "https://api.github.com";
    public static final String COMMITS_URI = "/repos/" + Constant.REPOSITORY_OWNER_PATTERN +
                                             "/" + Constant.REPOSITORY_NAME_PATTERN +
                                             "/commits" +
                                             "?since=" + Constant.SINCE_PATTERN + Constant.PAGE_URI;
    public static final String ISSUES_URI = "/repos/" + Constant.REPOSITORY_OWNER_PATTERN +
                                            "/" + Constant.REPOSITORY_NAME_PATTERN +
                                            "/issues" +
                                            "?since=" + Constant.SINCE_PATTERN + Constant.PAGE_URI;
    public static final String PAGE_URI = "&page=" + Constant.PAGE_PATTERN +
                                          "&per_page=100";
    public static final String REPO_URI = "/repos/" + Constant.REPOSITORY_OWNER_PATTERN +
                                          "/" + Constant.REPOSITORY_NAME_PATTERN;
    public static final String REPOSITORY_OWNER_PATTERN = "{repositoryOwner}";
    public static final String REPOSITORY_NAME_PATTERN = "{repositoryName}";
    public static final String SINCE_PATTERN = "{since}";
    public static final String PAGE_PATTERN = "{page}";
    public static final String NOT_FOUND = "Not Found";


    //Model
    public static final String INITIAL_LAST_UPDATE = "1970-01-01T00:00:00Z";
    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String ISSUE_KEY = "issue";
    public static final String PULL_REQUEST_KEY = "pullRequest";
    public static final String COMMIT_KEY = "commit";

}
