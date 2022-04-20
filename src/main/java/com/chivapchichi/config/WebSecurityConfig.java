package com.chivapchichi.config;

import com.chivapchichi.service.security.JwtConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfigurer jwtConfigurer;

    @Autowired
    public WebSecurityConfig(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/tournament/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/tournament/**").permitAll()
                //.antMatchers(HttpMethod.POST, "/api/**").hasRole("USER")
                //.antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers("/admin-panel/**").hasRole("ADMIN")
                //.antMatchers("/admin/api/**").hasRole("ADMIN")
                .antMatchers("/registration").permitAll()
                //.antMatchers("/login-api/auth/login").permitAll()
                //.antMatchers("/login-api/auth/logout").hasAnyRole("ADMIN", "USER")
                .antMatchers("/login").permitAll()
                .antMatchers("/makelogout").hasAnyRole("ADMIN", "USER")
                //.antMatchers("/registration/api").permitAll()
                .antMatchers("/main.js").permitAll()
                .antMatchers("/select.js").permitAll()
                .antMatchers("/style.css").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(jwtConfigurer);
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
