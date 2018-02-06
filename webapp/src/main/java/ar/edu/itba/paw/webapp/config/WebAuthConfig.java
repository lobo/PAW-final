package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.ClickerQuestUserDetailsService;
import ar.edu.itba.paw.webapp.auth.LoginHandler;
import ar.edu.itba.paw.webapp.auth.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Base64;

@Configuration
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth"})
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private ClickerQuestUserDetailsService userDetailsService;
    @Autowired
    private LoginHandler loginHandler;
    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http.userDetailsService(userDetailsService).sessionManagement()
                .and()
                .csrf().disable().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/accounts/login").anonymous()
                .antMatchers(HttpMethod.POST, "/api/v1/accounts/create").anonymous()
                .antMatchers(HttpMethod.POST).authenticated()
                .antMatchers(HttpMethod.DELETE).authenticated()
                .antMatchers("/api/v1/**").permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().usernameParameter("username").passwordParameter("password").loginProcessingUrl("/api/v1/accounts/login")
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .successHandler(loginHandler)
                .and()
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
    @Override
    public void configure(final WebSecurity web) throws Exception{
        web.ignoring().antMatchers("api/resources/**","/404","/403");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(authProvider());
    }


    @Bean
    public PasswordEncoder passwordencoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordencoder());
        return auth;
    }

    @Bean
    public String tokenSigningKey() {
        return Base64.getEncoder().encodeToString("2ff9014773457469f20774db50b6fbaa10ea6060".getBytes());
    }
}