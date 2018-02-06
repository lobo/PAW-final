package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MarketDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.packages.Implementations.Storage;
import ar.edu.itba.paw.model.packages.Paginating;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jws.soap.SOAPBinding;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ar.edu.itba.paw.model.FactoryType.CABLE_MAKER_BASE;
import static ar.edu.itba.paw.model.FactoryType.PEOPLE_RECRUITING_BASE;
import static ar.edu.itba.paw.model.FactoryType.STOCK_INVESTMENT_BASE;
import static ar.edu.itba.paw.services.BigDecimalAssert.assertBigDecimalEquals;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    // For reset purposes
//    @Autowired
//    ar.edu.itba.paw.services.MockUserDao mockUserDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MarketDao marketDao;

    @Autowired
    UserDao userDao;



    private String username = "Daniel";
    private String password = "l0b0--";
    private String img = "1.img";
    private int id = 0;

    private Wealth w = Wealth.createWealth(id);

    private BigDecimal initialMoneyProduction = STOCK_INVESTMENT_BASE.getBaseRecipe().getValue(ResourceType.MONEY);
    private BigDecimal initialPeopleProduction = PEOPLE_RECRUITING_BASE.getBaseRecipe().getValue(ResourceType.PEOPLE);
    private BigDecimal initialMoney = ResourceType.initialMoney();

    @Before
    public void setup(){
        Mockito.reset(userDao);

        Mockito.when(passwordEncoder.encode(password)).thenReturn(password);
        Mockito.when(marketDao.registerPurchase(Matchers.any(StockMarketEntry.class))).thenReturn(true);

        //Setup userdao mock
        Mockito.when(userDao.create(username,password,img)).thenReturn(new User(id,username,password,img,BigDecimal.valueOf(id),null));
        Mockito.when(userDao.getUserWealth(id)).thenReturn(w);
        Mockito.when(userDao.findById(id)).thenReturn(new User(id,username,password,img,BigDecimal.valueOf(id),null));

    }

    @Test
    public void testCreateUser(){
        mockCreateUser(id,username);

        User user = userService.create(username,password,img);
        assertNotNull(user);
        assertEquals(user.getUsername(),username);
        assertEquals(user.getPassword(),password);
        assertEquals(user.getProfileImage(),img);
    }

    @Test
    public void testGetUserById(){
        mockCreateUser(id,username);
        User user = userService.create(username,password,img);
        User u1 = userService.findById(user.getId());
        assertEquals(user,u1);
    }

    @Test
    public void testGetUserByUsername(){
        mockCreateUser(id,username);
        Mockito.when(userDao.findByUsername(username)).thenReturn(new User(id,username,password,img,BigDecimal.valueOf(id),null));

        User user = userService.create(username,password,img);
        User u1 = userService.findByUsername(user.getUsername());
        assertEquals(user,u1);
    }




    public void testGlobalRanking(){
        User user = userService.create(username,password,img);
        User user2 = userService.create(username + "2",password,img);
        User user3 = userService.create(username + "3",password,img);

        assertTrue(userService.purchaseFactory(user2.getId(), PEOPLE_RECRUITING_BASE,1));

        assertTrue(userService.purchaseFactory(user3.getId(), PEOPLE_RECRUITING_BASE,1));
        assertTrue(userService.purchaseFactory(user3.getId(), PEOPLE_RECRUITING_BASE,1));

        int rank1 = userService.getGlobalRanking(user.getId());
        int rank2 = userService.getGlobalRanking(user2.getId());
        int rank3 = userService.getGlobalRanking(user3.getId());
        assertEquals(rank3,1);
        assertEquals(rank2,2);
        assertEquals(rank1,3);
    }


    @Test
    public void testGetUserWealth() {
        Mockito.when(userDao.getUserWealth(id)).thenReturn(Wealth.createWealth(id));
        Wealth wealth = userService.getUserWealth(id);

        assertNotNull(wealth);
        Map<ResourceType,BigDecimal> storageMap = wealth.getStorage().rawMap();
        Map<ResourceType,BigDecimal> productionsMap = wealth.getProductions().rawMap();
        assertFalse(storageMap.isEmpty());
        assertFalse(productionsMap.isEmpty());


        storageMap.forEach(
                (r,d)-> {
                    if(r==ResourceType.MONEY) {
                        assertBigDecimalEquals(d,initialMoney,0D);
                    } else {
                        assertBigDecimalEquals(d,ZERO,0D);
                    }
                }
        );


       productionsMap.forEach(
                (r,d) -> {
                            assertBigDecimalEquals(d,ZERO,0D);
                }
        );
    }

    @Test
    public void testGetUserFactoriesGood(){
        Mockito.when(userDao.getUserWealth(id)).thenReturn(Wealth.createWealth(id));
        Collection<Factory> mockFactories = Stream.of(FactoryType.values()).filter(factoryType -> factoryType != FactoryType.CABLE_MAKER_BASE)
                .map(factoryType -> {
                    switch (factoryType){
                        case PEOPLE_RECRUITING_BASE:
                        case STOCK_INVESTMENT_BASE:
                            return new Factory(id,factoryType,ONE,ONE,ONE,ONE,1);
                    }
                    return factoryType.defaultFactory(id);
                }).collect(Collectors.toList());
        Mockito.when(userDao.getUserFactories(id)).thenReturn(mockFactories);

        Collection<Factory> factories = userService.getUserFactories(id);

        assertEquals(FactoryType.values().length, factories.size());
        assertTrue(factories.containsAll(mockFactories));
        assertTrue(factories.contains(FactoryType.CABLE_MAKER_BASE.defaultFactory(id)));
    }

    @Test
    public void testPurchaseFactorySuccess(){
        Mockito.when(userDao.getUserWealth(id)).thenReturn(Wealth.createWealth(id));

        Factory origFactory = FactoryType.PEOPLE_RECRUITING_BASE.defaultFactory(id);
        Factory resultFactory = new Factory(id,FactoryType.PEOPLE_RECRUITING_BASE,BigDecimal.valueOf(2),ONE,ONE,ONE,1);

        Wealth resultWealth = Wealth.createWealth(id).purchaseResult(origFactory,1);

        User origUser = new User(id,username,password,img);
        User resultUser = new User(id,username,password,img,resultWealth.calculateScore(),null);

        Collection<Factory> mockFactories = new ArrayList<>();
        mockFactories.add(origFactory);

        Mockito.when(userDao.getUserFactories(id)).thenReturn(mockFactories);
        Mockito.when(userDao.update(origFactory)).thenReturn(resultFactory);
        Mockito.when(userDao.update(origUser)).thenReturn(resultUser);
        Mockito.when(userDao.update(resultWealth)).thenReturn(resultWealth);

        assertTrue(userService.purchaseFactory(id, PEOPLE_RECRUITING_BASE,1));

//        Mockito.verify(userDao,Mockito.times(1)).update(resultWealth);
//        Mockito.verify(userDao,Mockito.times(1)).update(resultFactory);
//        Mockito.verify(userDao,Mockito.times(1)).update(resultUser);
    }

    @Test
    public void testPurchaseFactoryFail(){
        Mockito.when(userDao.getUserWealth(id)).thenReturn(Wealth.createWealth(id));
        Collection<Factory> mockFactories = Stream.of(FactoryType.values())
                .map(factoryType -> {
                    switch (factoryType){
                        case PEOPLE_RECRUITING_BASE:
                        case STOCK_INVESTMENT_BASE:
                            return new Factory(id,factoryType,ONE,ONE,ONE,ONE,1);
                    }
                    return factoryType.defaultFactory(id);
                }).collect(Collectors.toList());

        Mockito.when(userDao.getUserFactories(id)).thenReturn(mockFactories);
//        Mockito.verify(userDao,Mockito.times(0)).update(Matchers.any(Wealth.class));
//        Mockito.verify(userDao,Mockito.times(0)).update(Matchers.any(Factory.class));
//        Mockito.verify(userDao,Mockito.times(0)).update(Matchers.any(User.class));

        assertFalse(userService.purchaseFactory(id, CABLE_MAKER_BASE,1));
    }



    // This test doesnt test anything we dont test in other tests
    public void purchaseMultipleFactoriesUpdateWealthTest(){
        User user = userService.create(username,password,img);
        assertTrue(userService.purchaseFactory(user.getId(), PEOPLE_RECRUITING_BASE,1));
        assertTrue(userService.purchaseFactory(user.getId(), PEOPLE_RECRUITING_BASE,1));
        assertTrue(userService.purchaseFactory(user.getId(), PEOPLE_RECRUITING_BASE,1));
        BigDecimal factory1Cost = new Factory(user.getId(), PEOPLE_RECRUITING_BASE,ONE,ONE,ONE,ONE,0)
                .getCost().getValue(ResourceType.MONEY);
        BigDecimal factory2Cost = new Factory(user.getId(), PEOPLE_RECRUITING_BASE,BigDecimal.valueOf(2),ONE,ONE,ONE,0)
                .getCost().getValue(ResourceType.MONEY);
        BigDecimal factory3Cost = new Factory(user.getId(), PEOPLE_RECRUITING_BASE,BigDecimal.valueOf(3),ONE,ONE,ONE,0)
                .getCost().getValue(ResourceType.MONEY);
        Wealth wealth = userService.getUserWealth(user.getId());
        assertBigDecimalEquals(wealth.getStorage().getValue(ResourceType.MONEY),
                initialMoney.subtract(factory1Cost.add(factory2Cost).add(factory3Cost)),
                0D);


    }

    @Test
    public void testPurchaseUpgradeSuccess(){
        Mockito.when(userDao.getUserWealth(id)).thenReturn(Wealth.createWealth(id));

        Factory origFactory = new Factory(id,FactoryType.PEOPLE_RECRUITING_BASE,ONE,ONE,ONE,ONE,1);
        Factory resultFactory = new Factory(id,FactoryType.PEOPLE_RECRUITING_BASE,ONE,ONE,ONE,ONE,2);

        Wealth resultWealth = Wealth.createWealth(id).upgradeResult(origFactory);

        User origUser = new User(id,username,password,img);
        User resultUser = new User(id,username,password,img,resultWealth.calculateScore(),null);

        Collection<Factory> mockFactories = new ArrayList<>();
        mockFactories.add(origFactory);

        Mockito.when(userDao.getUserFactories(id)).thenReturn(mockFactories);
        Mockito.when(userDao.update(origFactory)).thenReturn(resultFactory);
        Mockito.when(userDao.update(origUser)).thenReturn(resultUser);
        Mockito.when(userDao.update(resultWealth)).thenReturn(resultWealth);

        assertTrue(userService.purchaseUpgrade(id, PEOPLE_RECRUITING_BASE));

//        Mockito.verify(userDao,Mockito.times(1)).update(resultWealth);
//        Mockito.verify(userDao,Mockito.times(1)).update(resultFactory);
//        Mockito.verify(userDao,Mockito.times(1)).update(resultUser);
    }

    @Test
    public void testPurchaseUpgradeFail(){
        Mockito.when(userDao.getUserWealth(id)).thenReturn(Wealth.createWealth(id));

        Factory origFactory = new Factory(id,FactoryType.PEOPLE_RECRUITING_BASE,ONE,ONE,ONE,ONE,1);
        Factory resultFactory = new Factory(id,FactoryType.PEOPLE_RECRUITING_BASE,ONE,ONE,ONE,ONE,2);

        Wealth resultWealth = Wealth.createWealth(id).upgradeResult(origFactory);

        User origUser = new User(id,username,password,img);
        User resultUser = new User(id,username,password,img,resultWealth.calculateScore(),null);

        Collection<Factory> mockFactories = new ArrayList<>();
        mockFactories.add(origFactory);

        Mockito.when(userDao.getUserFactories(id)).thenReturn(mockFactories);
        Mockito.when(userDao.update(origFactory)).thenReturn(resultFactory);
        Mockito.when(userDao.update(origUser)).thenReturn(resultUser);
        Mockito.when(userDao.update(resultWealth)).thenReturn(resultWealth);

        assertFalse(userService.purchaseUpgrade(id, CABLE_MAKER_BASE));

//        Mockito.verify(userDao,Mockito.times(0)).update(resultWealth);
//        Mockito.verify(userDao,Mockito.times(0)).update(resultFactory);
//        Mockito.verify(userDao,Mockito.times(0)).update(resultUser);
    }

    @Test
    public void testPurchaseResourceSuccess() {
        Mockito.when(userDao.getUserWealth(id)).thenReturn(Wealth.createWealth(id));

        BigDecimal amount = ONE;
        Wealth resultWealth = Wealth.createWealth(id).addResource(ResourceType.CARDBOARD,amount);

        User origUser = new User(id,username,password,img);
        User resultUser = new User(id,username,password,img,resultWealth.calculateScore(),null);

        Mockito.when(userDao.update(origUser)).thenReturn(resultUser);
        Mockito.when(userDao.update(resultWealth)).thenReturn(resultWealth);

        assertTrue(userService.purchaseResourceType(id, ResourceType.CARDBOARD,amount.doubleValue()));

//        Mockito.verify(userDao,Mockito.times(1)).update(resultWealth);
//        Mockito.verify(userDao,Mockito.times(1)).update(resultUser);
    }

    @Test
    public void testBuyResourceFail() {

        Double amount = 20000D;
        Wealth origWealth = Wealth.createWealth(id);

        User origUser = new User(id,username,password,img);

        Mockito.when(userDao.getUserWealth(id)).thenReturn(origWealth);
        Mockito.when(userDao.update(origUser)).thenReturn(origUser);
        Mockito.when(userDao.update(origWealth)).thenReturn(origWealth);

        assertFalse(userService.purchaseResourceType(id, ResourceType.CARDBOARD,amount));

//        Mockito.verify(userDao,Mockito.times(0)).update(origWealth);
//        Mockito.verify(userDao,Mockito.times(0)).update(origUser);
    }

    @Test
    public void testSellResourceSuccess() {

        BigDecimal amount = ONE;
        Wealth origWealth = Wealth.createWealth(id).addResource(ResourceType.CARDBOARD,amount);
        Wealth resultWealth = origWealth.addResource(ResourceType.CARDBOARD,amount.negate());

        User origUser = new User(id,username,password,img);
        User resultUser = new User(id,username,password,img,resultWealth.calculateScore(),null);

        Mockito.when(userDao.getUserWealth(id)).thenReturn(origWealth);
        Mockito.when(userDao.update(origUser)).thenReturn(resultUser);
        Mockito.when(userDao.update(resultWealth)).thenReturn(resultWealth);

        assertTrue(userService.sellResourceType(id, ResourceType.CARDBOARD,amount.doubleValue()));

//        Mockito.verify(userDao,Mockito.times(1)).update(resultWealth);
//        Mockito.verify(userDao,Mockito.times(1)).update(resultUser);
    }

    @Test
    public void testSellResourceFail() {

        BigDecimal amount = BigDecimal.valueOf(2);
        Wealth origWealth = Wealth.createWealth(id).addResource(ResourceType.CARDBOARD,amount.subtract(ONE));

        User origUser = new User(id,username,password,img);

        Mockito.when(userDao.getUserWealth(id)).thenReturn(origWealth);
        Mockito.when(userDao.update(origUser)).thenReturn(origUser);
        Mockito.when(userDao.update(origWealth)).thenReturn(origWealth);

        assertFalse(userService.sellResourceType(id, ResourceType.CARDBOARD,amount.doubleValue()));

//        Mockito.verify(userDao,Mockito.times(0)).update(origWealth);
//        Mockito.verify(userDao,Mockito.times(0)).update(origUser);
    }

    @Test
    public void testPaginatorFirstPage() {
        int totalUsers = 11;
        int perPage = 5;
        int firstPage = 1;
        int pages = (int)Math.ceil(totalUsers/(double)perPage);
        List<User> list = new ArrayList<>();
        for(int i = 0;i<perPage;i++){
            list.add(new User(i+1,username+i,password,img,BigDecimal.valueOf(500+i),null));
        }
        Mockito.when(userDao.globalUsers(firstPage,perPage)).thenReturn(new Paginating<User>(firstPage,perPage,totalUsers,pages,list));
        Paginating<User> users = userService.globalUsers(firstPage,perPage);
        assertEquals(users.getPage(),firstPage);
        assertEquals(users.getItemsPerPage(),perPage);
        assertEquals(users.getTotalItems(),totalUsers);
        assertEquals(users.getTotalPages(),pages);

        assertEquals(users.getItems().size(),perPage);
        Set<User> uniqueUsers = new HashSet<>();
        users.getItems().forEach((u)->assertTrue(uniqueUsers.add(u)));
        BigDecimal score = BigDecimal.valueOf(500D+perPage);
        for(User u : users.getItems()){
            assertTrue(u.getScore().compareTo(score)<0);
            score = u.getScore();
        }
    }

    private void mockCreateUser(int id, String usr){
        Mockito.when(userDao.create(usr,password,img)).thenReturn(new User(id,usr,password,img,BigDecimal.valueOf(id),null));
        Mockito.when(userDao.getUserWealth(id)).thenReturn(w);
        Mockito.when(userDao.findById(id)).thenReturn(new User(id,usr,password,img,BigDecimal.valueOf(id),null));

    }
}
