package com.chivapchichi.service.api.admin;

import com.chivapchichi.model.Tournament;
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
import java.util.Arrays;
import java.util.Map;

@Service
public class TournamentAdminService extends GetUriService {

    private final RestTemplate restTemplate;

    private final String auth;

    public TournamentAdminService(RestTemplate restTemplate, @Value("${jwt.header}") String auth) {
        this.restTemplate = restTemplate;
        this.auth = auth;
    }

    public ResponseEntity<Tournament> saveTournament(Tournament tournament, HttpServletRequest request) {
        return restTemplate.exchange(
                getUri("/admin/api/tournament/add-tournament"),
                HttpMethod.POST,
                new HttpEntity<>(tournament, getAuthHeader(request)),
                Tournament.class
        );
    }

    public Tournament[] getAllTournaments() {
        return restTemplate.getForObject(getUri("/api/tournament/get-all-tournaments"), Tournament[].class);
    }

    public ResponseEntity<Map> deleteTournament(Integer id, HttpServletRequest request) {
        return restTemplate.exchange(
                getUri("/admin/api/tournament/delete-tournament/" + id),
                HttpMethod.DELETE,
                new HttpEntity<>(getAuthHeader(request)),
                Map.class
        );
    }

    public Tournament getTournamentById(Integer id) {
        return restTemplate.getForObject(getUri("/api/tournament/get-tournament/" + id), Tournament.class);
    }

    private HttpHeaders getAuthHeader(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Cookie cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(auth)).findAny().get();
        headers.set(auth, cookie.getValue());
        return headers;
    }
}
