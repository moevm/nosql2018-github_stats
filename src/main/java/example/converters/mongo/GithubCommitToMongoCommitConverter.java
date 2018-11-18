package example.converters.mongo;

import example.model.github.GithubCommit;
import example.model.mongo.MongoCommit;

import javax.xml.bind.DatatypeConverter;

public class GithubCommitToMongoCommitConverter {
    public static MongoCommit convert(GithubCommit githubCommit){
        return new MongoCommit(DatatypeConverter.parseDate(githubCommit.getDate()).getTime());
    }
}
