package example.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class MappingController {

    @RequestMapping(value = "/")
    public String getWelcomeTemplate(){
        return "welcome";
    }

    @RequestMapping(value = "mainStatistics")
    public String getCourseMainStatisticsCommits() {
        return "mainStatistics";
    }
}
