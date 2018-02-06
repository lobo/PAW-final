package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.packages.Implementations.Productions;
import ar.edu.itba.paw.model.packages.Implementations.Storage;
import ar.edu.itba.paw.model.packages.PackageBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import static ar.edu.itba.paw.persistence.BigDecimalAssert.assertBigDecimalEquals;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class UserDaoTest {
    private static final String PASSWORD = "Password";
    private static final String USERNAME = "Username";
    private static final String IMAGE = "asda.png";
    @Autowired
    private DataSource ds;
    @Autowired
    private UserHibernateDao userDao;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    @Test
    public void testCreateUSer() {
        final User user = userDao.create(USERNAME, PASSWORD,IMAGE);
        assertNotNull(user);
        assertEquals(USERNAME, user.getUsername());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(1,user.getId());
    }

    @Test
    public void testUserId() {
        final String username2 = USERNAME + "2";
        final String username3 = USERNAME + "3";

        final User user1 = userDao.create(USERNAME, PASSWORD,IMAGE);
        final User user2 = userDao.create(username2, PASSWORD,IMAGE);
        final User user3 = userDao.create(username3, PASSWORD,IMAGE);

        assertNotNull(user1);
        assertNotNull(user2);
        assertNotNull(user3);
        assertEquals(1,user1.getId());
        assertEquals(2,user2.getId());
        assertEquals(3,user3.getId());

    }

    @Test
    public void testCreateNullWealth(){
        final User user = userDao.create(USERNAME, PASSWORD,IMAGE);
        Wealth nullWealth = userDao.getUserWealth(user.getId());
        assertNull(nullWealth);

    }

    @Test
    public void testCreateEmptyWealth() {
        final User user = userDao.create(USERNAME, PASSWORD,IMAGE);
        Wealth wealth = new Wealth(user.getId(), Storage.packageBuilder().buildPackage(), Productions.packageBuilder().buildPackage());
        userDao.create(wealth);
        Wealth receivedWealth = userDao.getUserWealth(user.getId());
        assertEquals(ResourceType.values().length,receivedWealth.getStorage().getResources().size());
        receivedWealth.getProductions().rawMap().forEach((r,d) -> assertBigDecimalEquals(ZERO,d,0D));
        receivedWealth.getStorage().rawMap().forEach((r,d) -> assertBigDecimalEquals(ZERO,d,0D));
    }

    @Test
    public void testCreateFilledWealth() {
        final User user = userDao.create(USERNAME, PASSWORD,IMAGE);
        PackageBuilder<Storage> sb = Storage.packageBuilder();
        PackageBuilder<Productions> pb = Productions.packageBuilder();
        sb.putItemWithDate(ResourceType.PEOPLE,ONE, Calendar.getInstance());
        pb.putItem(ResourceType.PEOPLE,ONE);

        Wealth wealth = new Wealth(user.getId(), sb.buildPackage(), pb.buildPackage());
        userDao.create(wealth);
        Wealth receivedWealth = userDao.getUserWealth(user.getId());
        assertEquals(wealth,receivedWealth);

    }

    @Test
    public void testUpdateFilledWealth() {
        final User user = userDao.create(USERNAME, PASSWORD,IMAGE);
        PackageBuilder<Storage> sb = Storage.packageBuilder();
        PackageBuilder<Productions> pb = Productions.packageBuilder();
        sb.putItemWithDate(ResourceType.PEOPLE,ONE, Calendar.getInstance());
        pb.putItem(ResourceType.PEOPLE,ONE);

        Wealth wealth = new Wealth(user.getId(), sb.buildPackage(), pb.buildPackage());
        userDao.create(wealth);

        sb.addItem(ResourceType.PEOPLE,ONE);
        pb.addItem(ResourceType.PEOPLE,ONE);
        Wealth newWealth = new Wealth(user.getId(), sb.buildPackage(), pb.buildPackage());
        userDao.update(newWealth);

        Wealth receivedWealth = userDao.getUserWealth(user.getId());
        assertEquals(receivedWealth,newWealth);

    }

    @Test
    public void testCreateEmptyFactories(){
        final User user = userDao.create(USERNAME, PASSWORD,IMAGE);
        Collection<Factory> factories = userDao.getUserFactories(user.getId());
        assertTrue(factories.isEmpty());

    }
    @Test
    public void testCreateFactories() {
        final User user = userDao.create(USERNAME, PASSWORD,IMAGE);
        Factory factory1 = FactoryType.BOILER_BASE.defaultFactory(user.getId());
        Factory factory2 = FactoryType.STOCK_INVESTMENT_BASE.defaultFactory(user.getId());
        Factory factory3 = FactoryType.CABLE_MAKER_BASE.defaultFactory(user.getId());

        userDao.create(factory1);
        userDao.create(factory2);
        userDao.create(factory3);

        Collection<Factory> factories = userDao.getUserFactories(user.getId());
        assertTrue(factories.contains(factory1));
        assertTrue(factories.contains(factory2));
        assertTrue(factories.contains(factory3));

    }

    @Test
    public void testUpdateFactory() {
        final User user = userDao.create(USERNAME, PASSWORD,IMAGE);
        Factory factory1 = FactoryType.BOILER_BASE.defaultFactory(user.getId());
        Factory factory2 = FactoryType.CIRCUIT_MAKER_BASE.defaultFactory(user.getId());

        userDao.create(factory1);
        userDao.create(factory2);

        Factory newFactory = factory1.upgradeResult();
        userDao.update(newFactory);

        Collection<Factory> factories = userDao.getUserFactories(user.getId());
        assertTrue(factories.contains(newFactory));
        assertTrue(factories.contains(factory2));

    }
    @Test
    public void testFindByKeywordAll(){
        User user = userDao.create(USERNAME,PASSWORD,IMAGE);
        User user2 = userDao.create(USERNAME+"2",PASSWORD,IMAGE);

        Collection<User> users = userDao.findByKeyword(USERNAME);
        assertEquals(users.size(),2);
        assertTrue(users.contains(user));
        assertTrue(users.contains(user2));
    }

    @Test
    public void testFindByKeywordSome(){
        User user = userDao.create(USERNAME,PASSWORD,IMAGE);
        User user2 = userDao.create("test",PASSWORD,IMAGE);

        Collection<User> users = userDao.findByKeyword("est");
        assertEquals(users.size(),1);
        assertFalse(users.contains(user));
        assertTrue(users.contains(user2));
    }


    public void testGlobalRanking(){
        User user = userDao.create(USERNAME,PASSWORD,IMAGE);
        User user2 = userDao.create(USERNAME + "2",PASSWORD,IMAGE);
        User user3 = userDao.create(USERNAME + "3",PASSWORD,IMAGE);


        userDao.update(new Factory(1,FactoryType.PEOPLE_RECRUITING_BASE,BigDecimal.valueOf(3),ONE,ONE,ONE,1));
        userDao.update(new Factory(2,FactoryType.PEOPLE_RECRUITING_BASE,BigDecimal.valueOf(2),ONE,ONE,ONE,1));

        int rank1 = userDao.getGlobalRanking(user.getId());
        int rank2 = userDao.getGlobalRanking(user2.getId());
        int rank3 = userDao.getGlobalRanking(user3.getId());
        assertEquals(rank3,1);
        assertEquals(rank2,2);
        assertEquals(rank1,3);

    }
}
