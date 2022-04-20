package com.chivapchichi.service.api.admin;

import com.chivapchichi.model.Members;
import com.chivapchichi.model.Users;
import com.chivapchichi.service.GetUriService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserMemService extends GetUriService {

    private final RestTemplate restTemplate;
    private final String auth;

    public UserMemService(RestTemplate restTemplate, @Value("${jwt.header}") String auth) {
        this.restTemplate = restTemplate;
        this.auth = auth;
        /*HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);

        restTemplate.setRequestFactory(requestFactory);*/
    }

    public Users[] getUsers(HttpServletRequest request) {
        return restTemplate.exchange(
                getUri("/admin/api/users/get-users"),
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeader(request)),
                Users[].class
        ).getBody();
    }

    public ResponseEntity<Users> getUserByUserName(String username, HttpServletRequest request) {
        return restTemplate.exchange(
                getUri("/admin/api/users/user/" + username),
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeader(request)),
                Users.class
        );
    }

    public ResponseEntity<Members> getMemberByUserName(String username, HttpServletRequest request) {
        return restTemplate.exchange(
                getUri("/admin/api/users/member/" + username),
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeader(request)),
                Members.class
        );
    }

    public ResponseEntity<Map> deleteUserMember(String username, HttpServletRequest request) {
        return restTemplate.exchange(
                getUri("/admin/api/users/delete-user/" + username),
                HttpMethod.DELETE,
                new HttpEntity<>(getAuthHeader(request)),
                Map.class
        );
    }

    public void updateUserMember(Users users, Members members, HttpServletRequest request) {
        HttpHeaders headers = getAuthHeader(request);
        restTemplate.exchange(
                getUri("/admin/api/users/user/" + users.getUserName()),
                HttpMethod.PATCH,
                new HttpEntity<>(users, headers),
                Users.class
        );
        restTemplate.exchange(
                getUri("/admin/api/users/member/" + users.getUserName()),
                HttpMethod.PATCH,
                new HttpEntity<>(members, headers),
                Members.class
        );
    }

    private HttpHeaders getAuthHeader(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Cookie cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(auth)).findAny().get();
        headers.set(auth, cookie.getValue());
        return headers;
    }
}
