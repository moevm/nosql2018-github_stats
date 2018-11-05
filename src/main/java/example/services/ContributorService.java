package example.services;

import example.converters.github.JsonCommitToGithubCommitConverter;
import example.converters.mongo.GithubCommitToMongoCommitConverter;
import example.model.github.GithubCommit;
import example.model.mongo.Contributor;
import example.model.mongo.MongoCommit;
import example.rest.RestClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContributorService {

    private Contributor findContributorByName(List<Contributor> contributors, String name){
        return contributors
                .stream()
                .filter(contributor -> contributor.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private void updateCommits(List<Contributor> contributors, String owner, String repo, String since){
        String commitsJson = RestClient.get("/repos/" + owner + "/" + repo + "/commits" + "?since=" + since);

        List<GithubCommit> githubCommits = JsonCommitToGithubCommitConverter.convertMultipleCommits(commitsJson);
        if (githubCommits != null){
            for (GithubCommit githubCommit : githubCommits){
                Contributor contributor = findContributorByName(contributors, githubCommit.getContributor());
                MongoCommit mongoCommit = GithubCommitToMongoCommitConverter.convert(githubCommit);
                if (contributor != null){
                    contributor.getCommits().add(mongoCommit);
                } else {
                    Contributor newContributor = new Contributor();
                    newContributor.setName(githubCommit.getContributor());
                    newContributor.getCommits().add(mongoCommit);
                    contributors.add(newContributor);
                }
            }
        }
    }

    public List<Contributor> getUpdatedContributorsOfRepository(String owner, String repo, String since) {

        List<Contributor> contributors = new ArrayList<>();

        updateCommits(contributors, owner, repo, since);

        return contributors;
    }
}
