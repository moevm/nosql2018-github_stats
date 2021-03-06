package example.services;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.UpdateOptions;
import example.constants.Constant;
import example.database.MongoDB;
import example.model.mongo.Course;
import example.model.mongo.IdAndName;
import example.model.mongo.Repository;
import example.repository.CourseRepository;
import example.rest.GithubRestClient;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RepositoryService {

    @Autowired
    ContributorService contributorService;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseService courseService;

    @Autowired
    CredentialsSession credentialsSession;

    public void updateRepositories(ObjectId courseId, List<Repository> repositories, Date since){
        repositories.forEach(repository -> updateRepository(courseId, repository, since));
    }

    private void updateRepository(ObjectId courseId, Repository repository, Date since){
        contributorService.updateContributorsOfRepository(courseId,
                repository.getOwner(),
                repository.getName(),
                since);
    }

    public List<Repository> getRepositories(ObjectId courseId){
        List repositories = null;
        try {
            repositories = courseRepository
                    .findById(courseId.toString())
                    .get()
                    .getRepositories();
        } catch (NoSuchElementException e){
            e.printStackTrace();
            System.out.println("No such file");
        }
        return repositories;
    }

    public Course addRepository(ObjectId courseId, Repository repository){

        FindIterable alreadyExists = MongoDB.courses.find(new Document()
                .append("repositories", new Document()
                        .append("$elemMatch", new Document()
                                .append("name", repository.getName())
                                .append("owner", repository.getOwner()))));
        if (!alreadyExists.iterator().hasNext()){
            Bson where = new Document()
                    .append("_id", courseId);

            Bson update = new Document()
                    .append("$addToSet", new Document()
                            .append("repositories", new Document()
                                    .append("_id", repository.getId())
                                    .append("name", repository.getName())
                                    .append("owner", repository.getOwner())
                                    .append("contributors", repository.getContributors())));
            MongoDB.courses.updateOne(where, update, new UpdateOptions().upsert(true));

            Date currentLastUpdate = courseRepository.findById(courseId.toString()).get().getLastUpdate();
            Date initialLastUpdate = null;
            try {
                initialLastUpdate = new SimpleDateFormat(Constant.DATE_PATTERN).parse(Constant.INITIAL_LAST_UPDATE);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            courseService.updateLastUpdate(courseId, initialLastUpdate);
            courseService.updateCourse(courseId);
            courseService.updateLastUpdate(courseId, currentLastUpdate);

            return courseRepository.findById(courseId.toString()).get();
        }
        else {
            System.out.println("Repository with name:   " + repository.getName() +
                    "   and owner:  " + repository.getOwner() + "   already exists !");
            return null;
        }

    }

    public Course deleteRepository(ObjectId courseId, ObjectId repositoryId){

        try {
            Bson where = new Document()
                    .append("_id", courseId);

            Bson update = new Document()
                    .append("$pull", new Document()
                            .append("repositories", new Document()
                                    .append("_id", repositoryId)));
            MongoDB.courses.updateOne(where, update);

            return courseRepository.findById(courseId.toString()).get();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean areRepositoriesExists(List<Repository> repositories){
        String credentials = credentialsSession.getCredentials();
        for (Repository repository : repositories){
            if (!GithubRestClient
                    .isRepoExists(Constant.REPO_URI
                            .replace(Constant.REPOSITORY_NAME_PATTERN, repository.getName())
                            .replace(Constant.REPOSITORY_OWNER_PATTERN, repository.getOwner()),
                            credentials)){
                return false;
            }
        }
        return true;
    }

    public List<IdAndName> getRepositoryNames(ObjectId courseId){
        Optional<Course> course = courseRepository.findRepositoryNames(courseId);
        List<IdAndName> repositoryNames = null;
        if (course.isPresent()){
            repositoryNames = course.get()
                    .getRepositories()
                    .stream()
                    .map(rep -> new IdAndName(rep.getId().toString(), rep.getName()))
                    .collect(Collectors.toList());
        }
        return repositoryNames;
    }
}
