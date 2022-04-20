package com.chivapchichi.service.api.admin;

import com.chivapchichi.model.Record;
import com.chivapchichi.service.GetUriService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Service
public class RecordService extends GetUriService {

    private final RestTemplate restTemplate;

    private final String auth;

    public RecordService(RestTemplate restTemplate, @Value("${jwt.header}") String auth) {
        this.restTemplate = restTemplate;
        this.auth = auth;
    }

    public Record[] getAll() {
        return restTemplate.getForObject(getUri("/api/record/get-records"), Record[].class);
    }

    public Record[] getByTournament(Integer id) {
        return restTemplate.getForObject(getUri("/api/record/get-records/by-tournament/" + id), Record[].class);
    }

    public Record getRecordById(Integer id) {
        return restTemplate.getForObject(getUri("/api/record/get-record/" + id), Record.class);
    }

    public void deleteById(Integer id, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Cookie cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(auth)).findAny().get();
        headers.set(auth, cookie.getValue());

        restTemplate.exchange(
                getUri("/admin/api/record/delete-record/" + id),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );
    }
}
