package example.services;

import org.springframework.stereotype.Service;

@Service
public class CredentialsSession {
    private String credentials;

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }
}
