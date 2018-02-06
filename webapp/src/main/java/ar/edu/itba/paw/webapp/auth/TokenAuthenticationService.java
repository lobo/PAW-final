package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Julian Benitez on 11/14/2017.
 */

@Service
public class TokenAuthenticationService  {

    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String SCHEMA = "Bearer ";

    @Autowired
    private TokenManager tokenManager;

    public void addAuthentication(final HttpServletResponse response, final Authentication authentication) {
        response.addHeader(AUTH_HEADER_NAME, SCHEMA + tokenManager.createToken(authentication.getName()));
        response.addHeader("Access-Control-Expose-Headers", AUTH_HEADER_NAME);
    }

    public Authentication getAuthentication(final HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER_NAME);

        if(token == null) { return null; }

        token = token.replace(SCHEMA,"");

        if (token != null && Jwts.parser().isSigned(token)) {
            final UserDetails details = tokenManager.parseToken(token);

            if (details != null)
                return new UserAuthentication(details);
        }

        return null;
    }
}
