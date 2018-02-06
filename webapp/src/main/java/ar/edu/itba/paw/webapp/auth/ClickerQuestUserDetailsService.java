package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Component
public class ClickerQuestUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService us;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername (final String username) throws UsernameNotFoundException {
        User user = us.findByUsername(username);
        if (user == null ) {
            throw new UsernameNotFoundException( "No user by the name " + username);
        }

        if(!user.getPassword().startsWith("$")) {
            us.resetPassword(user, passwordEncoder.encode(user.getPassword()));
        }

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.asList(  new SimpleGrantedAuthority( "ROLE_USER" ));
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

}