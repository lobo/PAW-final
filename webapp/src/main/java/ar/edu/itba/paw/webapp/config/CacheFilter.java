package ar.edu.itba.paw.webapp.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CacheFilter extends OncePerRequestFilter {

    //This is to solve ResolveHandler not adding Headers to the response.
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)  throws ServletException, IOException {

        if(!request.getRequestURI().matches(".*/api/.*")) {
            response.addHeader("Cache-Control", "max-age=31556926, public");
        }
        filterChain.doFilter(request, response);
    }
}
