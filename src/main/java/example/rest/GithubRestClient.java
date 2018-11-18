package example.rest;

import example.constants.Constant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class GithubRestClient {

    private static final String API_URL = Constant.API_URL;

    private static final RestTemplate restTemplate = new RestTemplate();

    public static String get(String uri) {
        try {
            ResponseEntity<String> response
                    = restTemplate.getForEntity(API_URL + uri, String.class);
            return response.getBody();
        } catch (HttpClientErrorException e){
            System.out.println("Not Found");
            return null;
        }
    }
}