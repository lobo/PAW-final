package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.MarketDao;
import ar.edu.itba.paw.model.ResourceType;
import ar.edu.itba.paw.model.StockMarketEntry;
import ar.edu.itba.paw.model.User;
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
import java.util.Map;

import static ar.edu.itba.paw.persistence.BigDecimalAssert.assertBigDecimalEquals;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class MarketJdbcDaoTest {
    private static final String CLAN_NAME = "AwesomeClan";
    private static User USER = new User(0,"pepe","pass","as.jpg");
    private static User USER2 = new User(0,"pepe2","pass","as.jpg");

    @Autowired
    private DataSource ds;
    @Autowired
    private MarketDao marketDao;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplate.execute("TRUNCATE TABLE stockmarket RESTART IDENTITY AND COMMIT NO CHECK");
    }

    @Test
    public void testRegisterNewPurchase() {
        assertTrue(marketDao.registerPurchase(new StockMarketEntry(ResourceType.MONEY, BigDecimal.valueOf(20))));
    }

    @Test
    public void testRegisterMultiplePurchases() {
        marketDao.registerPurchase(new StockMarketEntry(ResourceType.MONEY,BigDecimal.valueOf(20)));
        assertTrue(marketDao.registerPurchase(new StockMarketEntry(ResourceType.MONEY,BigDecimal.valueOf(20))));
    }

    @Test
    public void testPopularity(){
        marketDao.registerPurchase(new StockMarketEntry(ResourceType.MONEY,BigDecimal.valueOf(20)));
        Map<ResourceType,BigDecimal> popularities = marketDao.getPopularities();
        assertTrue(popularities.containsKey(ResourceType.MONEY));
    }

    @Test
    public void testMultiplePopularity(){
        marketDao.registerPurchase(new StockMarketEntry(ResourceType.MONEY,BigDecimal.valueOf(20)));
        marketDao.registerPurchase(new StockMarketEntry(ResourceType.MONEY,BigDecimal.valueOf(20)));
        marketDao.registerPurchase(new StockMarketEntry(ResourceType.CARDBOARD,BigDecimal.valueOf(20)));
        Map<ResourceType,BigDecimal> popularities = marketDao.getPopularities();
        assertTrue(popularities.containsKey(ResourceType.MONEY));
        assertTrue(popularities.containsKey(ResourceType.CARDBOARD));
        assertTrue(popularities.get(ResourceType.MONEY).compareTo(popularities.get(ResourceType.CARDBOARD))>0);

    }

    @Test
    public void testPurchaseAndSell(){
        marketDao.registerPurchase(new StockMarketEntry(ResourceType.MONEY,BigDecimal.valueOf(-20)));
        marketDao.registerPurchase(new StockMarketEntry(ResourceType.CARDBOARD,BigDecimal.valueOf(20)));
        Map<ResourceType,BigDecimal> popularities = marketDao.getPopularities();
        assertTrue(popularities.containsKey(ResourceType.MONEY));
        assertTrue(popularities.containsKey(ResourceType.CARDBOARD));
        assertTrue(popularities.get(ResourceType.MONEY).compareTo(popularities.get(ResourceType.CARDBOARD))<0);
        assertNotEquals(popularities.get(ResourceType.MONEY).doubleValue(),0D,0D);

    }

    @Test
    public void testEmptyPopularities(){
        marketDao.getPopularities().forEach(
                (r,d) -> assertBigDecimalEquals(ONE,d,0)
        );
    }




}
