package com.chivapchichi.service.api;

import com.chivapchichi.model.Record;
import com.chivapchichi.model.Tournament;
import com.chivapchichi.service.GetUriService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class TournamentRegService extends GetUriService {

    private final RestTemplate restTemplate;

    private final String auth;

    public TournamentRegService(RestTemplate restTemplate, @Value("${jwt.header}") String auth) {
        this.restTemplate = restTemplate;
        this.auth = auth;
    }

    public void homepage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentPrincipal = authentication.getName();
            model.addAttribute("principal", currentPrincipal);
            Tournament[] tournamentRecords = restTemplate.getForObject(
                    getUri("/api/tournament/get-by-username/" + currentPrincipal),
                    Tournament[].class);
            model.addAttribute("tournamentsRecords", tournamentRecords);
            model.addAttribute("registered", true);
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                model.addAttribute("admin", true);
            }
        } else {
            model.addAttribute("registered", false);
        }
        Tournament[] tournaments = restTemplate.getForObject(getUri("/api/tournament/get-all-tournaments"), Tournament[].class);
        model.addAttribute("tournament", tournaments);
        model.addAttribute("division", getDivisions());
    }

    public void tournaments(Model model) {
        homepage(model);
        String tournamentsUri = getUri("/api/tournament/get-dates");
        Calendar[] tournaments = restTemplate.getForObject(tournamentsUri, Calendar[].class);
        List<Calendar[]> dates = new ArrayList<>(tournaments.length / 3 + 1);
        for (int i = 0; i * 3 < tournaments.length; i++) {
            dates.add(getSlice(tournaments, i * 3, i * 3 + (tournaments.length / 3 > i ? 3 : tournaments.length % 3)));
        }
        model.addAttribute("dates", dates);
    }

    private Calendar[] getSlice(Calendar[] date, int start, int end) {
        Calendar[] slice = new Calendar[end - start];
        System.arraycopy(date, start, slice, 0, slice.length);
        return slice;
    }

    public void tournamentsWithDivision(Model model, String division) {
        tournaments(model);
        model.addAttribute("selectedDivision", division);
    }

    public void formRegistration(Integer id, Model model, HttpServletRequest request) {
        homepage(model);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentPrincipal = authentication.getName();
            String info = getUri("/api/record/get-records/by-tournament/" + id);
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("username", currentPrincipal);
            HttpHeaders header = getHeadersWithAuth(request);
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                if (restTemplate.exchange(info, HttpMethod.POST, new HttpEntity<>(userInfo, header), Record.class).getBody() != null) {
                    model.addAttribute("unregistration", "Удалить запись");
                } else {
                    model.addAttribute("registration", "Записаться");
                }
            }
        }
        Tournament tournament = restTemplate.getForObject(getUri("/api/tournament/get-tournament/" + id), Tournament.class);
        model.addAttribute("selectedTournament", tournament);

        model.addAttribute(
                "recordMain",
                restTemplate.getForObject(
                        getUri("/api/record/get-records/by-tournament/" + id + "/main-team"),
                        Record[].class
                )
        );

        model.addAttribute(
                "recordReserve",
                restTemplate.getForObject(
                        getUri("/api/record/get-records/by-tournament/" + id + "/reserve"),
                        Record[].class
                )
        );
    }

    public ResponseEntity<Map> registerOnTournament(Integer id, HttpServletRequest request) {
        return makereg(id, request, "register");
    }

    public ResponseEntity<Map> unregisterOnTournament(Integer id, HttpServletRequest request) {
        return makereg(id, request, "unregister");
    }

    public String[] getDivisions() {
        return restTemplate.getForObject(getUri("/api/tournament/get-divisions"), String[].class);
    }

    private Cookie getAuthCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(auth)).findAny().get();
    }

    private HttpHeaders getHeadersWithAuth(HttpServletRequest request) {
        HttpHeaders header = new HttpHeaders();
        Cookie token = getAuthCookie(request);
        header.set(auth, token.getValue());
        return header;
    }

    private ResponseEntity<Map> makereg(Integer id, HttpServletRequest request, String reg) {
        return restTemplate.exchange(
                getUri("/api/tournament/" + reg + '/' + id),
                HttpMethod.POST,
                new HttpEntity<>(null, getHeadersWithAuth(request)),
                Map.class
        );
    }

    @Bean
    public static RestTemplate getRestTemplate(HttpComponentsClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public static HttpComponentsClientHttpRequestFactory getHttpFactory() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(50000);
        requestFactory.setReadTimeout(50000);
        return requestFactory;
    }
}
