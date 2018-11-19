package example;

import example.repository.CourseRepository;
import example.services.CommitsService;
import example.services.ContributorService;
import example.services.CourseService;
import example.services.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Main extends SpringBootServletInitializer
                            //For debug
                            /*implements CommandLineRunner*/
{

    @Autowired
    private CourseService courseService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ContributorService contributorService;

    @Autowired
    private CommitsService commitsService;

    @Autowired
    private CourseRepository courseRepository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    //For debug
    /*
    @Override
    public void run(String... args) throws ParseException {

    }*/

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Main.class);
    }

}