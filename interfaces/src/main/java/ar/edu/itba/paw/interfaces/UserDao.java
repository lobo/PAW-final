package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Factory;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Wealth;
import ar.edu.itba.paw.model.packages.Implementations.Productions;
import ar.edu.itba.paw.model.packages.Implementations.Storage;
import ar.edu.itba.paw.model.packages.Paginating;

import java.util.Collection;


public interface UserDao {

    User findById(long id);
    User findByUsername(String username);
    Collection<User> findByKeyword(String search);
    Integer getGlobalRanking(long userId);

    /**
     * Create a new user.
     *
     * @param username The name of the user.
     * @return The created user.
     */
    User create(String username, String Password, String profileImage);
    Factory create(Factory factory);
    Wealth create(Wealth wealth);

    String getProfileImage(long userid);
    Productions getUserProductions(long userid);
    Storage getUserStorage(long userid);

    User update(User u);
    User resetPassword(User u, String newPassword);

    Collection<Factory> getUserFactories(long userid);
    Factory update(Factory f);

    Wealth getUserWealth(long userid);
    Wealth update(Wealth w);

    Paginating<User> globalUsers(int pag, int userPerPage);

}
