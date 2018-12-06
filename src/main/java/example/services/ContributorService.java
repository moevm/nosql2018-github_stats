package example.services;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.UpdateOptions;
import example.constants.Constant;
import example.converters.github.JsonToGithubEntityConverter;
import example.converters.mongo.GithubCommitToMongoCommitConverter;
import example.converters.mongo.GithubIssueOrPullRequestToMongoIssueOrPullRequestConverter;
import example.database.MongoDB;
import example.model.github.GithubCommit;
import example.model.github.GithubIssueOrPullRequest;
import example.model.github.GithubUser;
import example.model.mongo.*;
import example.model.mongo.abstractEntity.AnalyzedEntity;
import example.repository.CourseRepository;
import example.rest.GithubRestClient;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContributorService {
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CredentialsSession credentialsSession;

    public void updateContributorsOfRepository(ObjectId courseId, String owner, String repo, Date since) {
        updateCommits(courseId, owner, repo, since);
        updateIssuesAndPullRequests(courseId, owner, repo, since);
    }

    private void updateCommits(ObjectId courseId, String repoOwner, String repoName, Date since) {

        List<Contributor> contributors = new ArrayList<>();
        List<GithubCommit> githubCommits = new ArrayList<>();
        String credentials = credentialsSession.getCredentials();

        for (int page = 1; page < Integer.MAX_VALUE; page++){
            String commitsJson = GithubRestClient.get(Constant.COMMITS_URI
                    .replace(Constant.REPOSITORY_OWNER_PATTERN, repoOwner)
                    .replace(Constant.REPOSITORY_NAME_PATTERN, repoName)
                    .replace(Constant.SINCE_PATTERN, new SimpleDateFormat(Constant.DATE_PATTERN).format(since))
                    .replace(Constant.PAGE_PATTERN, Integer.toString(page)),
                    credentials);
            if (commitsJson == null || commitsJson.equals("[]")){
                break;
            } else {
                githubCommits.addAll(Objects.requireNonNull(JsonToGithubEntityConverter.convertCommits(commitsJson)));
            }
        }

        if (githubCommits.size() != 0) {
            githubCommits.sort(Comparator.comparing(GithubCommit::getContributor));

            String currentContributorName = null;
            Contributor currentContributor = null;
            for (int i = 0; i < githubCommits.size(); ++i) {
                GithubCommit githubCommit = githubCommits.get(i);
                MongoCommit mongoCommit = GithubCommitToMongoCommitConverter.convert(githubCommit);
                if (Objects.equals(currentContributorName, githubCommits.get(i).getContributor())) {
                    currentContributor.getCommits().add(mongoCommit);
                } else {
                    if (currentContributor != null) {
                        contributors.add(currentContributor);
                    }
                    currentContributorName = githubCommit.getContributor();
                    currentContributor = new Contributor();
                    currentContributor.setName(currentContributorName);
                    currentContributor.getCommits().add(mongoCommit);
                }

                if (i == githubCommits.size() - 1) {
                    contributors.add(currentContributor);
                }
            }
        }

        for (Contributor contributor : contributors) {
            checkForExistenceContributorAndCreateOtherwise(courseId, repoName,
                    contributor.getName(), repoOwner);

            Bson where = new Document()
                    .append("_id", courseId);

            List<Bson> commits = new ArrayList<>();
            for (AnalyzedEntity commit : contributor.getCommits()) {
                commits.add(new Document()
                        .append("_id", commit.getDate()));
            }

            Bson update = new Document()
                    .append("$addToSet", new Document()
                            .append("repositories.$[i].contributors.$[j].commits",
                                    new Document().
                                            append("$each", commits)));

            List<Bson> arrayFilters = Arrays.asList(new Document()
                            .append("i.name", repoName)
                            .append("i.owner", repoOwner),
                    new Document()
                            .append("j._id", contributor.getName()));

            MongoDB.courses.updateOne(where, update, new UpdateOptions()
                    .arrayFilters(arrayFilters)
                    .upsert(true));
        }
    }

    private void updateIssuesAndPullRequests(ObjectId courseId, String repoOwner, String repoName, Date since) {

        List<Contributor> contributors = new ArrayList<>();
        List<GithubIssueOrPullRequest> githubIssuesAndPullRequests = new ArrayList<>();
        String credentials = credentialsSession.getCredentials();

        for (int page = 1; page < Integer.MAX_VALUE; page++){
            String issuesJson = GithubRestClient.get(Constant.ISSUES_URI
                    .replace(Constant.REPOSITORY_OWNER_PATTERN, repoOwner)
                    .replace(Constant.REPOSITORY_NAME_PATTERN, repoName)
                    .replace(Constant.SINCE_PATTERN, new SimpleDateFormat(Constant.DATE_PATTERN).format(since))
                    .replace(Constant.PAGE_PATTERN, Integer.toString(page)),
                    credentials);
            if (issuesJson == null || issuesJson.equals("[]")){
                break;
            } else {
                githubIssuesAndPullRequests
                        .addAll(Objects.requireNonNull(JsonToGithubEntityConverter
                                .convertIssues(issuesJson)));
            }
        }

        if (githubIssuesAndPullRequests.size() != 0) {
            githubIssuesAndPullRequests.sort(Comparator.comparing(GithubIssueOrPullRequest::getLogin));

            String currentContributorLogin = null;
            String currentContributorName = null;
            Contributor currentContributor = null;
            GithubUser currentGithubUser = null;
            for (int i = 0; i < githubIssuesAndPullRequests.size(); ++i) {
                GithubIssueOrPullRequest githubIssueOrPullRequest = githubIssuesAndPullRequests.get(i);
                Map mongoIssueOrPullRequestMap = GithubIssueOrPullRequestToMongoIssueOrPullRequestConverter
                        .convert(githubIssueOrPullRequest);
                boolean isPullRequest = mongoIssueOrPullRequestMap.get(Constant.PULL_REQUEST_KEY) != null;
                AnalyzedEntity mongoIssueOrPullRequest = (AnalyzedEntity) (isPullRequest
                                        ? mongoIssueOrPullRequestMap.get(Constant.PULL_REQUEST_KEY)
                                        : mongoIssueOrPullRequestMap.get(Constant.ISSUE_KEY));


                if (Objects.equals(currentContributorName, githubIssuesAndPullRequests.get(i).getLogin())) {
                    if (isPullRequest){
                        currentContributor.getPullRequests().add((MongoPullRequest) mongoIssueOrPullRequest);
                    } else {
                        currentContributor.getIssues().add((MongoIssue) mongoIssueOrPullRequest);
                    }
                } else {
                    if (currentContributor != null) {
                        contributors.add(currentContributor);
                    }
                    currentContributorLogin = githubIssueOrPullRequest.getLogin();
                    currentGithubUser = JsonToGithubEntityConverter.convertUser(GithubRestClient.get(Constant.USERS_URI
                                    .replace(Constant.USER_LOGIN_PATTERN, currentContributorLogin), null));
                    currentContributorName = (currentGithubUser != null && currentGithubUser.getName() != null)
                            ? currentGithubUser.getName()
                            : currentContributorLogin;
                    currentContributor = new Contributor();
                    currentContributor.setName(currentContributorName);
                    if (isPullRequest){
                        currentContributor.getPullRequests().add((MongoPullRequest) mongoIssueOrPullRequest);
                    } else {
                        currentContributor.getIssues().add((MongoIssue) mongoIssueOrPullRequest);
                    }
                }

                if (i == githubIssuesAndPullRequests.size() - 1) {
                    contributors.add(currentContributor);
                }
            }
        }

        for (Contributor contributor : contributors) {
            checkForExistenceContributorAndCreateOtherwise(courseId, repoName,
                    contributor.getName(), repoOwner);

            Bson where = new Document()
                    .append("_id", courseId);

            List<Bson> issues = new ArrayList<>();
            for (AnalyzedEntity issue : contributor.getIssues()) {
                issues.add(new Document()
                        .append("_id", issue.getDate()));
            }

            List<Bson> pullRequests = new ArrayList<>();
            for (AnalyzedEntity pullRequest : contributor.getPullRequests()) {
                pullRequests.add(new Document()
                        .append("_id", pullRequest.getDate()));
            }

            Bson updateIssues = new Document()
                    .append("$addToSet", new Document()
                            .append("repositories.$[i].contributors.$[j].issues",
                                    new Document().
                                            append("$each", issues)));

            Bson updatePullRequests = new Document()
                    .append("$addToSet", new Document()
                            .append("repositories.$[i].contributors.$[j].pullRequests",
                                    new Document().
                                            append("$each", pullRequests)));

            List<Bson> arrayFilters = Arrays.asList(new Document()
                            .append("i.name", repoName)
                            .append("i.owner", repoOwner),
                    new Document()
                            .append("j._id", contributor.getName()));

            MongoDB.courses.updateOne(where, updateIssues, new UpdateOptions()
                    .arrayFilters(arrayFilters)
                    .upsert(true));

            MongoDB.courses.updateOne(where, updatePullRequests, new UpdateOptions()
                    .arrayFilters(arrayFilters)
                    .upsert(true));
        }
    }

    private void checkForExistenceContributorAndCreateOtherwise(ObjectId courseId,
                                                                String repoName,
                                                                String contributor,
                                                                String repoOwner) {
        FindIterable mongoContributor = MongoDB.courses.find(new Document()
                .append("_id", courseId)
                .append("repositories", new Document()
                        .append("$elemMatch", new Document()
                                .append("name", repoName)
                                .append("owner", repoOwner)
                                .append("contributors", new Document()
                                        .append("$elemMatch", new Document()
                                                .append("_id", contributor))))));

        if (!mongoContributor.iterator().hasNext()) {
            Bson where = new Document()
                    .append("_id", courseId)
                    .append("repositories", new Document()
                            .append("$elemMatch", new Document()
                                    .append("name", repoName)
                                    .append("owner", repoOwner)));
            Bson newContributor = new Document()
                    .append("_id", contributor)
                    .append("commits", new ArrayList<Bson>());
            Bson updateContributor = new Document()
                    .append("$addToSet", new Document()
                            .append("repositories.$.contributors", newContributor));
            MongoDB.courses.updateOne(where, updateContributor);
        }
    }

    public List<Contributor> getContributors(ObjectId courseId, ObjectId repositoryId) {

        List contributors = null;
        try {

            contributors = courseRepository
                    .findByIdAndRepositoryId(courseId, repositoryId)
                    .get()
                    .getRepositories()
                    .stream()
                    .filter(repository -> repository.getId().equals(repositoryId))
                    .collect(Collectors.toList())
                    .get(0)
                    .getContributors();

        } catch (NoSuchElementException e) {
            e.printStackTrace();
            System.out.println("No such file");
        }
        return contributors;
    }

    public List<String> getContributorNames(ObjectId courseId, ObjectId repositoryId) {
        Optional<Course> course = courseRepository.findContributorNames(courseId, repositoryId);
        List<String> contributorNames = null;
        if (course.isPresent()) {
            Optional<List<Contributor>> contributors = course.get().getRepositories()
                    .stream()
                    .filter(repository -> repository.getId().equals(repositoryId))
                    .map(Repository::getContributors)
                    .findFirst();

            if (contributors.isPresent()) {
                contributorNames = contributors
                        .get()
                        .stream()
                        .map(Contributor::getName)
                        .collect(Collectors.toList());
            }

        }
        return contributorNames;
    }
}
