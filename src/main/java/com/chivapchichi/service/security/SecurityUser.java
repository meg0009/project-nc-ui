package com.chivapchichi.service.security;

import com.chivapchichi.model.Users;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class SecurityUser implements UserDetails {

    private final String userName;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean isActive = true;

    public SecurityUser(String userName, String password, List<SimpleGrantedAuthority> authorities) {
        this.userName = userName;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromUsers(Users users) {
        Set<SimpleGrantedAuthority> auth = new HashSet<>();
        auth.add(new SimpleGrantedAuthority(users.getRole()));

        return new User(users.getUserName()
                , users.getPassword()
                , true, true, true, true
                , auth);
    }
}
