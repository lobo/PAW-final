package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.packages.BuyLimits;
import ar.edu.itba.paw.model.packages.Implementations.Productions;
import ar.edu.itba.paw.model.packages.Implementations.Storage;
import ar.edu.itba.paw.model.packages.Paginating;

import java.util.Collection;
import java.util.Map;


public interface UserService {

    User findById(long id);
    User findByUsername(String username);
    Collection<User> findByKeyword(String search);

    Integer getGlobalRanking(long userId);

    User create(String username, String Password, String img);
    User resetPassword(User user, String newPassword);

    Wealth getUserWealth(long id);
    Productions getUserProductions(long id);
    Storage getUserStorage(long id);
    Wealth calculateUserWealth(long userid);


    boolean purchaseFactory(long userid, FactoryType type,long amount);
    Map<FactoryType, Long> canPurchaseFactory(long userid);
    Collection<BuyLimits> getPurchaseableFactory(long userid);

    boolean purchaseUpgrade(long userid, FactoryType type);
    boolean purchaseResourceType(long userid, ResourceType resourceType, double amount);
    boolean sellResourceType(long userid, ResourceType resourceType, double amount);

    Collection<Factory> getUserFactories(long id);

    String getProfileImage(long userid);
    Paginating<User> globalUsers(int pag, int userPerPage);

    }
