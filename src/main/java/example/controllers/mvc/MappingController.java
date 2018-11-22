package example.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MappingController {

    @RequestMapping(value = "/")
    public String getWelcomeTemplate(){
        return "welcome";
    }

    @RequestMapping(value = "course_page")
    public String getCoursePage() {
        return "course";
    }
}
