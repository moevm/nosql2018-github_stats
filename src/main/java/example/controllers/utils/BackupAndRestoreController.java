package example.controllers.utils;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@ResponseBody
public class BackupAndRestoreController {
    @RequestMapping(value = "/backup", method = RequestMethod.POST)
    public Map backupDump() throws IOException {
        Map<String, Object> response = new HashMap<>();

        String workingDir = System.getProperty("user.dir");
        StringBuilder query = new StringBuilder();

        query.append("cd ").append(workingDir)
                .append(" && ");

        query.append("mkdir dump && cd ./dump || cd ./dump ")
                .append(" && ");

        query.append("rm -r ./*")
                .append(" && ");

        query.append("mongodump " +
                "--collection course " +
                "--db github " +
                "--out ./")
                .append(" && ");

        query.append("zip -r ")
                .append(workingDir)
                .append("/dump/dump.zip ")
                .append(workingDir).append("/dump/*");

        Runtime.getRuntime().exec(query.toString());

        return response;
    }

    @RequestMapping(value = "/restore", method = RequestMethod.POST)
    public Map restoreDump(@RequestBody Map<String, Object> body,
                          HttpServletResponse httpServletResponse){
        Map<String, Object> response = new HashMap<>();


        return response;
    }
}
