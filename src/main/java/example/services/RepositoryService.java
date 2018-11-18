package example.services;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.UpdateOptions;
import example.constants.Constant;
import example.database.MongoDB;
import example.model.mongo.Repository;
import example.repository.CourseRepository;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RepositoryService {

    @Autowired
    ContributorService contributorService;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseService courseService;

    public void updateRepositories(List<Repository> repositories, String since){
        repositories.forEach(repository -> updateRepository(repository, since));
    }

    private void updateRepository(Repository repository, String since){
        contributorService.updateContributorsOfRepository(repository.getOwner(),
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
            System.out.println("No such file");
        }
        return repositories;
    }

    public void addRepository(ObjectId courseId, Repository repository){

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

            String currentLastUpdate = courseRepository.findById(courseId.toString()).get().getLastUpdate();
            String initialLastUpdate = Constant.INITIAL_LAST_UPDATE;

            courseService.updateLastUpdate(courseId, initialLastUpdate);
            courseService.updateCourse(courseId);
            courseService.updateLastUpdate(courseId, currentLastUpdate);
        }
        else {
            System.out.println("Repository with name:   " + repository.getName() +
                    "   and owner:  " + repository.getOwner() + "   already exists !");
        }

    }

    public void deleteRepository(ObjectId courseId, ObjectId repositoryId){
        Bson where = new Document()
                .append("_id", courseId);

        Bson update = new Document()
                .append("$pull", new Document()
                        .append("repositories", new Document()
                                .append("_id", repositoryId)));
        MongoDB.courses.updateOne(where, update);
    }
}
