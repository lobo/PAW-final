package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MarketDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.packages.BuyLimits;
import ar.edu.itba.paw.model.packages.Implementations.Productions;
import ar.edu.itba.paw.model.packages.Implementations.Storage;
import ar.edu.itba.paw.model.packages.PackageBuilder;
import ar.edu.itba.paw.model.packages.Paginating;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    MarketDao marketDao;

    public User findById(long userid) {
        return userDao.findById(userid);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public Collection<User> findByKeyword(String search) {
        if(!search.matches("^[a-zA-Z0-9]+$")) return Collections.emptyList();
        return userDao.findByKeyword(search);
    }


    @Override
    public Wealth getUserWealth(long userid) {
        Wealth w = userDao.getUserWealth(userid);
        Map<ResourceType,BigDecimal> storageRaw = w.getStorage().rawMap();

        if(storageRaw.size() < ResourceType.values().length) {
            PackageBuilder<Storage> storageB = Storage.packageBuilder();
            PackageBuilder<Productions> productionsB = Productions.packageBuilder();
            Calendar now = Calendar.getInstance();

            Stream.of(ResourceType.values()).filter(
                    resType -> storageRaw.keySet().stream().noneMatch(r -> r == resType))
                    .forEach(   resType -> {
                        storageB.putItem(resType,BigDecimal.ZERO);
                        productionsB.putItem(resType,BigDecimal.ZERO);
                    });

            w.getStorage().rawMap().forEach(storageB::putItem);
            w.getProductions().rawMap().forEach(productionsB::putItem);
            Stream.of(ResourceType.values()).forEach((r) -> storageB.setLastUpdated(r,now));
            w = new Wealth(userid,storageB.buildPackage(),productionsB.buildPackage());
            updateWealth(w);
        }
        return w;
    }

    public Wealth calculateUserWealth(long userid){
        User u = findById(userid);
        Wealth oldWealth = getUserWealth(userid);
        Wealth newWealth = oldWealth.calculateProductions(getUserFactories(userid));
        u = userDao.update(new User(userid,u.getUsername(),u.getPassword(),
                    u.getProfileImage(),newWealth.calculateScore(),u.getClanId()));

        updateWealth(newWealth);
        return newWealth;
    }

    public User create(String username, String password, String img) {
        User user = userDao.create(username,passwordEncoder.encode(password),img);
        if(user != null){
            userDao.create(Wealth.createWealth(user.getId()));
            for (FactoryType type: FactoryType.values()){
                Factory factory = type.defaultFactory(user.getId());
                create(factory.getType(),user.getId());
            }
            purchaseFactory(user.getId(),FactoryType.PEOPLE_RECRUITING_BASE,1);
            purchaseFactory(user.getId(),FactoryType.STOCK_INVESTMENT_BASE,1);

            return userDao.findById(user.getId());
        }
        return null;
    }

    public User resetPassword(User user, String newPassword){
        return userDao.resetPassword(user, newPassword);
    }

    @Override
    public Productions getUserProductions(long id) {
        return userDao.getUserProductions(id);
    }

    @Override
    public Storage getUserStorage(long id) {
        return userDao.getUserStorage(id);
    }

    @Override
    public boolean purchaseFactory(long userid,@NotNull FactoryType type, long amountToBuy) {
        Wealth w = getUserWealth(userid);
        Optional<Factory> maybeFactory = userDao.getUserFactories(userid).stream()
                .filter( (f) -> f.getType() == type).findAny();

        Factory f;

        if(maybeFactory.isPresent()) {
            f = maybeFactory.get();
        } else {
            return false;
        }

        if( f.isBuyable(w,amountToBuy)) {
            Wealth wealth = w.purchaseResult(f,amountToBuy);
            Factory factory = f.purchaseResult(amountToBuy);

            wealth = updateWealth(wealth);
            factory = userDao.update(factory);

            return factory != null && wealth != null;
        }
        return false;
    }


    @Override
    public Collection<BuyLimits> getPurchaseableFactory(long userid) {
        Wealth w = getUserWealth(userid);
        Collection<Factory> factories = userDao.getUserFactories(userid);
        return factories.stream().map(f->f.getLimits(w)).collect(Collectors.toList());
    }

    @Override
    public Map<FactoryType,Long> canPurchaseFactory(long userid) {
        Wealth w = getUserWealth(userid);
        Map<FactoryType,Long> maxBuyable = new HashMap<>();
        Collection<Factory> factories = userDao.getUserFactories(userid);
        for (Factory f: factories){
            long max=0;
            BuyLimits bl = f.getLimits(w);
            if(f.isBuyable(w,100)) max = 100;
            else if(f.isBuyable(w,50)) max = 50;
            else if(f.isBuyable(w,25)) max = 25;
            else if(f.isBuyable(w,10)) max = 10;
            else if(f.isBuyable(w,5)) max = 5;
            else if(f.isBuyable(w,1)) max = 1;
            maxBuyable.put(f.getType(),max);
        }

        return maxBuyable;
    }

    @Override
    public Collection<Factory> getUserFactories(long userid) {
        final Collection<Factory> factories = userDao.getUserFactories(userid);
        if(factories.size() < FactoryType.values().length){
            List<Factory> lostFactories = Stream.of(FactoryType.values())
                    .filter((t) -> factories.stream().noneMatch((f) -> f.getType() == t))
                    .map(
                            (t) -> create(t,userid)
                    )
                    .collect(Collectors.toList());

            lostFactories.addAll(factories);
            return lostFactories;
        }
        return factories;
    }

    private Factory create(FactoryType factoryType, long userId){
        final Factory f = factoryType.defaultFactory(userId);
        userDao.create(f);
        return f;
    }

    @Override
    public String getProfileImage(long userid) {
        return userDao.getProfileImage(userid);
    }

    @Override
    public boolean purchaseUpgrade(long userid, FactoryType type) {
        Collection<Factory> factories = getUserFactories(userid);
        Factory factory = factories.stream()
                .filter(
                        (f) -> f.getType().getId() == type.getId()
                ).findAny().orElse(null);

        if(factory==null) {
            LOGGER.error("There was an error making the purchaseUpgrade: the factory is null");
            throw new RuntimeException("Factory not found");
        }

        Wealth w = getUserWealth(userid);

        if(factory.isUpgreadable(w)) {
            Factory newFactory = factory.upgradeResult();
            factories.remove(factory);
            factories.add(newFactory);
            Wealth newWealth = w.calculateProductions(factories);
            newWealth = newWealth.addResource(ResourceType.MONEY,BigDecimal.valueOf(-factory.getNextUpgrade().getCost()));
            userDao.update(newFactory);
            updateWealth(newWealth);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean sellResourceType(long userid, ResourceType resourceType, double amount) {
        if(resourceType == ResourceType.MONEY) {
            return false;
        }

        Wealth wealth = getUserWealth(userid);
        BigDecimal bigAmount = BigDecimal.valueOf(amount);
        BigDecimal cost = (resourceType.getPrice()).multiply(bigAmount).setScale(0,BigDecimal.ROUND_FLOOR);
        if( wealth.getStorage().getValue(resourceType).compareTo(bigAmount) <  0 ) {
            return false;
        }

        PackageBuilder<Storage> wbuilder = Storage.packageBuilder();

        wealth.getStorage().rawMap().forEach(wbuilder::putItem);
        wealth.getStorage().getLastUpdated().forEach(wbuilder::setLastUpdated);

        wbuilder.addItem(resourceType,BigDecimal.valueOf(-amount));
        wbuilder.addItem(ResourceType.MONEY,cost);

        Wealth newWealth = new Wealth(userid,wbuilder.buildPackage(),wealth.getProductions());
        updateWealth(newWealth);
        marketDao.registerPurchase(new StockMarketEntry(resourceType,BigDecimal.valueOf(-amount)));

        return true;
    }

    @Override
    public boolean purchaseResourceType(long userid, ResourceType resourceType,double amount){
        if(resourceType == ResourceType.MONEY) {
            return false;
        }
        Wealth wealth = getUserWealth(userid);
        BigDecimal bigAmount = BigDecimal.valueOf(amount);
        BigDecimal cost = (resourceType.getPrice()).multiply(bigAmount).setScale(0, BigDecimal.ROUND_CEILING);
        if( wealth.getStorage().getValue(ResourceType.MONEY).compareTo(cost) <  0 ) {
            return false;
        }

        PackageBuilder<Storage> wbuilder = Storage.packageBuilder();

        wealth.getStorage().rawMap().forEach(wbuilder::putItem);
        wealth.getStorage().getLastUpdated().forEach(wbuilder::setLastUpdated);

        wbuilder.addItem(ResourceType.MONEY,cost.negate());
        wbuilder.addItem(resourceType,BigDecimal.valueOf(amount));

        Wealth newWealth = new Wealth(userid,wbuilder.buildPackage(),wealth.getProductions());
        updateWealth(newWealth);
        marketDao.registerPurchase(new StockMarketEntry(resourceType,BigDecimal.valueOf(amount)));

        return true;
    }



    private Wealth updateWealth(Wealth wealth){
        long userId = wealth.getUserid();
        User oldUser = findById(userId);
        User newUser = new User(oldUser.getId(),
                                oldUser.getUsername(),
                                oldUser.getPassword(),
                                oldUser.getProfileImage(),
                                wealth.calculateScore(),
                                oldUser.getClanId());
        if(userDao.update(newUser) != null) {
            return userDao.update(wealth);
        } else {
            return null;
        }
    }

    @Override
    public Integer getGlobalRanking(long userId) {
        return userDao.getGlobalRanking(userId);
    }

    @Override
    public Paginating<User> globalUsers(int pag, int userPerPage) {
        Paginating<User> users = userDao.globalUsers(pag,userPerPage);
        if(users==null) {
            return null;
        }
        users.sort((u1,u2)->u1.getScore().compareTo(u2.getScore())<0?1:-1);
        return users;
    }


}
