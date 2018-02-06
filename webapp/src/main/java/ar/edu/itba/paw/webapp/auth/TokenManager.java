package ar.edu.itba.paw.webapp.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Base64;
import java.util.UUID;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;


@Component
public class TokenManager {

    private String key = Base64.getEncoder().encodeToString("2ff9014773457469f20774db50b6fbaa10ea6060".getBytes());

    @Autowired
    private UserDetailsService userDetailsService;

    public String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public UserDetails parseToken(final String token) {
        try {
            final String username = Jwts.parser().setSigningKey(key)
                    .parseClaimsJws(token).getBody().getSubject();
            return userDetailsService.loadUserByUsername(username);
        } catch (SignatureException e) {
            return null;
        }
    }
}
