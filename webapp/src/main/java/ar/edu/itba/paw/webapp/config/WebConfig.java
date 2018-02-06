package ar.edu.itba.paw.webapp.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@EnableWebMvc
@ComponentScan({ "ar.edu.itba.paw.webapp.controller, ar.edu.itba.paw.services, ar.edu.itba.paw.persistence","ar.edu.itba.paw.persistence" })
@Configuration
@EnableTransactionManagement

@Import(value = { WebAuthConfig.class })
@EnableScheduling
public class WebConfig {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);

    @Autowired
    private Environment environment;
    // --------- WEBAPP
    /**
     * ViewResolver for the Webapp. JSP files located in WEB-INF/jsp/ will composed
     * the different views of our Webapp
     */
    @Bean
    public ViewResolver viewResolver() {

        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;

    }

    // ---------- PERSISTENCE

    @Value("classpath:schema.sql")
    private Resource schemaSql;

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory (DataSource datasource) {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan( "ar.edu.itba.paw.model" );
        factoryBean.setDataSource(datasource);
        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        final Properties properties = new Properties();
        properties.setProperty( "hibernate.hbm2ddl.auto" , "update" );
        properties.setProperty( "hibernate.dialect" , "org.hibernate.dialect.PostgreSQL92Dialect" );
        if(environment.acceptsProfiles("!production")) {
            properties.setProperty("hibernate.show_sql", "true");
            properties.setProperty("format_sql", "true");
        }

        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf){
        return new JpaTransactionManager(emf);
    }


    /**
     * DataSource which indicates how the application should access the database. Sets up
     * the port, address, database name, user and password for it, as well as the driver.
     */
    @Bean
    @Profile("production")
    public DataSource productionDataSource() {

        final SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(org.postgresql.Driver.class);
        ds.setUrl("jdbc:postgresql://10.16.1.110/paw-2017a-4");
        ds.setUsername("paw-2017a-4");
        ds.setPassword("ooc4Choo");
        return ds;

    }
    /**
     * DataSource which indicates how the application should access the database. Sets up
     * the port, address, database name, user and password for it, as well as the driver.
     */
    @Bean
    @Profile("default")
    public DataSource devDataSource() {

        final SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(org.postgresql.Driver.class);
        ds.setUrl("jdbc:postgresql://localhost/clickerQuest");
        ds.setUsername("root");
        ds.setPassword("root");
        return ds;

    }

    @Bean
    public ExposedResourceBundleMessageSource messageSource() {
        final ExposedResourceBundleMessageSource messageSource = new ExposedResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        ReloadableResourceBundleMessageSource ads ;
        return messageSource;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }


}
