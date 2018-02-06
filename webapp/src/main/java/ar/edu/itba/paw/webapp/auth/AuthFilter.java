package ar.edu.itba.paw.webapp.auth;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.GenericFilterBean;


public class AuthFilter extends GenericFilterBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);


    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Authentication auth = tokenAuthenticationService.getAuthentication(httpRequest);
        SecurityContextHolder.getContext().setAuthentication(auth);

        if(auth.isAuthenticated())
            filterChain.doFilter(request, response);
        else {
            response.setCharacterEncoding("UTF-8");
            httpResponse.getWriter().write("ERROR AUTHENTICATING");

            LOGGER.info("ACCESS_DENIED.");
        }
    }
}
