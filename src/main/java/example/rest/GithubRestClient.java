package example.rest;

import example.constants.Constant;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class GithubRestClient {

    private static final String API_URL = Constant.API_URL;

    private static final RestTemplate restTemplate = new RestTemplate();

    public static String get(String uri, String credentials) {
        try {
            ResponseEntity<String> response;

            if (credentials != null){
                HttpEntity<String> request = createCredentialsRequest(credentials);
                response = restTemplate.
                        exchange(API_URL + uri, HttpMethod.GET, request, String.class);
            } else {
                response = restTemplate.
                        getForEntity(API_URL + uri, String.class);
            }

            return response.getBody();
        } catch (HttpClientErrorException e){
            System.out.println("Not Found");
            return null;
        }
    }

    public static boolean isRepoExists(String uri, String credentials){
        try {
            if (credentials != null){
                HttpEntity<String> request = createCredentialsRequest(credentials);
                restTemplate.
                        exchange(API_URL + uri, HttpMethod.GET, request, String.class);
            } else {
                restTemplate.
                        getForEntity(API_URL + uri, String.class);
            }
            return true;
        } catch (HttpClientErrorException e) {
            System.out.println("Not Found");
            return false;
        }

    }

    private static HttpEntity<String> createCredentialsRequest(String credentials) {
        byte[] plainCredsBytes = credentials.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        return new HttpEntity<String>(headers);
    }
}