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

    public static String get(String uri) {
        try {
            String plainCreds = "vender98:qpc734b83ak47bdy";
            byte[] plainCredsBytes = plainCreds.getBytes();
            byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
            String base64Creds = new String(base64CredsBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + base64Creds);

            HttpEntity<String> request = new HttpEntity<String>(headers);

            ResponseEntity<String> response
                    = restTemplate.exchange(API_URL + uri, HttpMethod.GET, request, String.class);

            /*ResponseEntity<String> response
                    = restTemplate.getForEntity(API_URL + uri, String.class);*/
            return response.getBody();
        } catch (HttpClientErrorException e){
            System.out.println("Not Found");
            return null;
        }
    }

    public static boolean isRepoExists(String uri){
        String plainCreds = "vender98:qpc734b83ak47bdy";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        try {
            final String URL = (API_URL + uri);
            ResponseEntity<String> response
                    = restTemplate.exchange(URL, HttpMethod.GET, request, String.class);
            /*ResponseEntity<String> response
                    = restTemplate.getForEntity(URL, String.class);*/
            return true;
        } catch (HttpClientErrorException e) {
            System.out.println("Not Found");
            return false;
        }

    }
}