package com.mayflowertech.chilla.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mayflowertech.chilla.GoogleTokenInfo;

@Service
public class GoogleAuthService {

    private static final String GOOGLE_TOKEN_VERIFICATION_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=%s";

    public boolean verifyGoogleIdToken(String idToken) {
        String verificationUrl = String.format(GOOGLE_TOKEN_VERIFICATION_URL, idToken);

        RestTemplate restTemplate = new RestTemplate();
        //GoogleTokenInfo tokenInfo = restTemplate.getForObject(verificationUrl, GoogleTokenInfo.class);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(verificationUrl, String.class);
        System.out.println(responseEntity.toString());
        
        // Check if the token is valid based on the response from Google
        return true;
    }
}
