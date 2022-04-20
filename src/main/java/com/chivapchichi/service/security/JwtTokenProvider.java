package com.chivapchichi.service.security;

import com.chivapchichi.exception.JwtAuthenticationException;
import com.chivapchichi.model.Users;
import com.chivapchichi.service.GetUriService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenProvider extends GetUriService {

    private final RestTemplate restTemplate;

    @Value("${jwt.header}")
    private String authorizationHeader;

    public JwtTokenProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Authentication getAuthentication(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(authorizationHeader, token);
        try {
            ResponseEntity<Users> users = restTemplate.exchange(getUri("/myinfo/get-user-info"), HttpMethod.GET, new HttpEntity(headers), Users.class);
            UserDetails userDetails = SecurityUser.fromUsers(users.getBody());
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (HttpClientErrorException.Forbidden e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid", e.getStatusCode());
        }
    }

    public String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            Optional<Cookie> cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(authorizationHeader)).findAny();
            if (cookie.isPresent()) {
                return cookie.get().getValue();
            }
        }
        return request.getHeader(authorizationHeader);
    }

    public void deleteCookie(HttpServletResponse response) {
        Cookie delete = new Cookie(authorizationHeader, null);
        delete.setMaxAge(0);
        response.addCookie(delete);
    }
}
