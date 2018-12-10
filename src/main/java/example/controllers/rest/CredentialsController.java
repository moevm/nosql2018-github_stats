package example.controllers.rest;

import example.constants.ParamNames;
import example.constants.ParamValues;
import example.services.CredentialsSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/credentials")
@ResponseBody
public class CredentialsController {
    @Autowired
    CredentialsSession credentialsSession;

    @RequestMapping(value = "/set", method = RequestMethod.POST)
    public Map setCredentials(@RequestBody Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        String credentials = (String) body.get("credentials");
        if (credentials != null){
            credentialsSession.setCredentials(credentials);
            response.put(ParamNames.RESULT_KEY, ParamValues.SUCCESS_SAVING_CREDENTIALS);
        }
        return response;
    }

    @RequestMapping(value = "/isCredentialsSetted", method = RequestMethod.GET)
    public Map isCredentialsSetted() {
        Map<String, Object> response = new HashMap<>();
        String credentials = credentialsSession.getCredentials();
        if (credentials != null){
            response.put(ParamNames.RESULT_KEY, true);
        } else {
            response.put(ParamNames.RESULT_KEY, false);
        }
        return response;
    }

    @RequestMapping(value = "/clear", method = RequestMethod.DELETE)
    public Map clearCredentials() {
        Map<String, Object> response = new HashMap<>();
        credentialsSession.setCredentials(null);
        response.put(ParamNames.RESULT_KEY, ParamValues.SUCCESS_CLEARING_CREDENTIALS);
        return response;
    }
}
