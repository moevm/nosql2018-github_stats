package example.services;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.UpdateOptions;
import example.constants.Constant;
import example.converters.github.JsonCommitToGithubCommitConverter;
import example.converters.mongo.GithubCommitToMongoCommitConverter;
import example.database.MongoDB;
import example.model.github.GithubCommit;
import example.model.mongo.Contributor;
import example.model.mongo.MongoCommit;
import example.repository.CourseRepository;
import example.rest.GithubRestClient;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

@Service
public class ContributorService {
    @Autowired
    CourseRepository courseRepository;

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

        if (!mongoContributor.iterator().hasNext()){
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

    private void updateCommits(ObjectId courseId, String repoOwner, String repoName, Date since){

        List<Contributor> contributors = new ArrayList<>();

        String commitsJson = GithubRestClient.get(Constant.COMMITS_URI
                .replace(Constant.REPOSITORY_OWNER_PATTERN, repoOwner)
                .replace(Constant.REPOSITORY_NAME_PATTERN, repoName)
                .replace(Constant.SINCE_PATTERN, new SimpleDateFormat(Constant.DATE_PATTERN).format(since)));

        List<GithubCommit> githubCommits = JsonCommitToGithubCommitConverter.convert(commitsJson);

        if (githubCommits != null){
            githubCommits.sort(Comparator.comparing(GithubCommit::getContributor));

            String currentContributorName = null;
            Contributor currentContributor = null;
            for (int i = 0; i < githubCommits.size(); ++i){
                GithubCommit githubCommit = githubCommits.get(i);
                MongoCommit mongoCommit = GithubCommitToMongoCommitConverter.convert(githubCommit);
                if (Objects.equals(currentContributorName, githubCommits.get(i).getContributor())){
                    currentContributor.getCommits().add(mongoCommit);
                } else {
                    if (currentContributor != null){
                        contributors.add(currentContributor);
                    }
                    currentContributorName = githubCommit.getContributor();
                    currentContributor = new Contributor();
                    currentContributor.setName(currentContributorName);
                    currentContributor.getCommits().add(mongoCommit);
                }

                if (i == githubCommits.size() - 1){
                    contributors.add(currentContributor);
                }
            }
        }

        for (Contributor contributor : contributors){
            checkForExistenceContributorAndCreateOtherwise(courseId, repoName,
                    contributor.getName(), repoOwner);

            Bson where = new Document()
                    .append("_id", courseId);

            List<Bson> commits = new ArrayList<>();
            for (MongoCommit commit : contributor.getCommits()){
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

    public void updateContributorsOfRepository(ObjectId courseId, String owner, String repo, Date since) {
        updateCommits(courseId, owner, repo, since);
    }

    public List<Contributor> getContributors(ObjectId courseId, ObjectId repositoryId){

        List contributors = null;
        try {
            contributors = courseRepository
                    .findByIdAndRepositoryId(courseId, repositoryId)
                    .get()
                    .getRepositories()
                    .get(0)
                    .getContributors();
        } catch (NoSuchElementException e){
            System.out.println("No such file");
        }
        return contributors;
    }
}
