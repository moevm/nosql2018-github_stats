package example;

import example.converters.JsonCommitsToPojoCommitsConverter;
import example.model.Commit;
import example.repository.CommitsRepository;
import example.rest.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private CommitsRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {

//        Scanner inputReader = new Scanner(System.in);
//        System.out.println("Set owner name");
//        String owner = inputReader.nextLine();
//        System.out.println("Set repo name");
//        String repo = inputReader.nextLine();

        String owner = "vender98";
        String repo = "oop";

        String commitsJson = RestClient.get("/repos/" + owner + "/" + repo + "/commits");
        List<Commit> commits = JsonCommitsToPojoCommitsConverter.convertMultipleCommits(commitsJson);

        repository.saveAll(commits);

        System.out.println(repository.findAll());
        System.out.println(repository.customQueryFindExample());

        repository.deleteAll();


    }

}