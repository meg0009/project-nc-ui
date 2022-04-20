package com.chivapchichi.service.api;

import com.chivapchichi.model.AuthenticationRequestDTO;
import com.chivapchichi.service.GetUriService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

@Service
public class LoginService extends GetUriService {

    private final RestTemplate restTemplate;

    private final String auth;

    public LoginService(RestTemplate restTemplate, @Value("${jwt.header}") String auth) {
        this.restTemplate = restTemplate;
        this.auth = auth;
    }

    public void login(AuthenticationRequestDTO request, HttpServletResponse response) {
        ResponseEntity<Map> post = restTemplate.exchange(
                getUri("/login-api/auth/login"),
                HttpMethod.POST,
                new HttpEntity<>(request),
                Map.class
        );

        Cookie jwtCookie = new Cookie(auth, (String) post.getBody().get("token"));
        response.addCookie(jwtCookie);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie delete = new Cookie(auth, null);
        delete.setMaxAge(0);

//        restTemplate.postForObject(getUri("/login-api/auth/logout"), null, Void.class);
        HttpHeaders headers = new HttpHeaders();
        Cookie a = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(auth)).findAny().get();
        headers.set(auth, a.getValue());
        restTemplate.exchange(
                getUri("/login-api/auth/logout"),
                HttpMethod.POST,
                new HttpEntity<>(headers),
                Boolean.class
        );

        response.addCookie(delete);
    }
}
