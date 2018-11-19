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
                                            "?since=" + Constant.SINCE_PATTERN;
    public static final String REPOSITORY_OWNER_PATTERN = "{repositoryOwner}";
    public static final String REPOSITORY_NAME_PATTERN = "{repositoryName}";
    public static final String SINCE_PATTERN = "{since}";


    //Model
    public static final String INITIAL_LAST_UPDATE = "1970-01-01T00:00:00Z";
    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

}
