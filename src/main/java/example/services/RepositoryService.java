package example.services;

import example.model.mongo.Contributor;
import example.model.mongo.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryService {

    @Autowired ContributorService contributorService;

    public void updateRepositories(List<Repository> repositories, String since){
        repositories.forEach(repository -> updateRepository(repository, since));
    }

    private void updateRepository(Repository repository, String since){
        List<Contributor> contributors = contributorService
                .getUpdatedContributorsOfRepository(repository.getOwner(),
                        repository.getName(),
                        since);

        //TODO
        //Сейчас сервис возвращает ВСЕ имеющиеся данные по контрбьюторам
        //При получении НОВЫХ данных начиная с некоторой даты
        //необходимо будет переделать на
        //repository.getContributors().addAll(contributors);


        //repository.setContributors(contributors);
        repository.getContributors().addAll(contributors);
    }
}
