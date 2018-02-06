package ar.edu.itba.paw.webapp.auth;


import java.util.Arrays;
import java.util.Collection;

import ar.edu.itba.paw.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class UserAuthentication implements Authentication {

    private String token;
    private UserDetails details;
    private boolean authenticated;


    public UserAuthentication(UserDetails details) {
        this.details = details;
        authenticated = true;
    }

    @Override
    public String getName() {
        return details.getUsername();
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return details.getUsername();
    }

    @Override
    public Object getCredentials(){ return details.getPassword(); }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return details.getAuthorities();
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
