package example.controllers.utils;

import example.constants.Constant;
import example.constants.ParamNames;
import example.constants.ParamValues;
import example.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@ResponseBody
public class BackupAndRestoreController {
    @Autowired
    private FileStorageService fileStorageService;

    @RequestMapping(value = "/backup", method = RequestMethod.GET)
    public ResponseEntity<Resource> backupDump(HttpServletRequest request) throws Exception {
        String workingDir = System.getProperty("user.dir");
        String query = "mongoexport " +
                "--collection " + Constant.COLLECTION_NAME + " " +
                "--db " + Constant.DATABASE_NAME + " " +
                "--out " + workingDir + "/dump/dump.json";

        Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", query});
        int i;
        while( (i=p.getInputStream().read()) != -1) {
            System.out.write(i);
        }
        while( (i=p.getErrorStream().read()) != -1) {
            System.err.write(i);
        }
        p.waitFor();


        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(workingDir + "/dump/dump.json");

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }


        Runtime.getRuntime().exec(new String[]{"bash", "-c", "rm -rf " + workingDir + "/dump/*"});

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @RequestMapping(value = "/restore", method = RequestMethod.POST)
    public Map<String, Object> restoreDump(@RequestParam("file") MultipartFile file,
                                           @RequestParam(value = "dropOld", required = false) boolean dropFlag,
                                           HttpServletResponse httpServletResponse) {
        Map<String, Object> response = new HashMap<>();

        try {
            String fileName = fileStorageService.storeFile(file);

            String workingDir = System.getProperty("user.dir");
            String query = "mongoimport " +
                    "--collection " + Constant.COLLECTION_NAME + " " +
                    "--db " + Constant.DATABASE_NAME + " " +
                    (dropFlag ? "--drop " : "") +
                    workingDir + "/dump/" + fileName + " ";
            query += "&& rm -rf " + workingDir + "/dump/*";

            Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", query});
            int i;
            while( (i=p.getInputStream().read()) != -1) {
                System.out.write(i);
            }
            while( (i=p.getErrorStream().read()) != -1) {
                System.err.write(i);
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            response.put(ParamNames.ERROR_KEY, ParamValues.ERROR_IMPORTING_DATABASE);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return response;
    }
}
