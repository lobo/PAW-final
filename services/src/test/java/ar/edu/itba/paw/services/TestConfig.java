package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.ClanDao;
import ar.edu.itba.paw.interfaces.MarketDao;
import ar.edu.itba.paw.interfaces.UserDao;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@ComponentScan({ "ar.edu.itba.paw.services" })
@Configuration
public class TestConfig {

    /**
     * DataSource for the Testing of the Persistence Module. It uses a new HSQLDB dataBase
     * stored temporarily in memory
     */
    @Bean
    public UserDao mockUserDao() {
        return Mockito.mock(UserDao.class);
//        return new ar.edu.itba.paw.services.MockUserDao();
    }

    @Bean
    public PasswordEncoder mockPasswordEncoder(){
        return Mockito.mock(PasswordEncoder.class);
    }

    @Bean
    public MarketDao mockMarketDao(){
        return Mockito.mock(MarketDao.class);
    }

    @Bean
    public ClanDao mockClanDao(){
        return Mockito.mock(ClanDao.class);
    }
}