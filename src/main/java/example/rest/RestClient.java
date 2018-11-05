package example.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class RestClient {

    private static final String API_URL = "https://api.github.com";

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