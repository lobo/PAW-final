package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.ClanDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Factory;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Wealth;
import ar.edu.itba.paw.model.packages.Implementations.Productions;
import ar.edu.itba.paw.model.packages.Implementations.Storage;
import ar.edu.itba.paw.model.packages.Paginating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Repository
public class UserHibernateDao implements UserDao{
    @PersistenceContext
    private EntityManager em;

    @Autowired
    ClanDao clanDao;

    @Override
    public User findByUsername (final String username) {
        final TypedQuery<User> query = em.createQuery( "from User as u where lower(u.username) = lower(:username)" , User.class);
        query.setParameter( "username" , username);
        final List<User> list = query.getResultList();
        return list.isEmpty() ? null : list.get( 0 );
    }

    @Override
    public Collection<User> findByKeyword(String search) {
        StringBuilder s = new StringBuilder(search.toLowerCase()).append("%").insert(0,"%");
        final TypedQuery<User> query = em.createQuery( "from User as u where lower(u.username) like lower(:username)" , User.class);
        query.setParameter( "username" , s.toString());
        final List<User> list = query.getResultList();
        return list;
    }

    @Override
    public Integer getGlobalRanking(long userId) {
        Query query = em.createNativeQuery("SELECT row_number FROM " +
                                                    "(SELECT ROW_NUMBER() OVER(ORDER BY score DESC),* FROM users) as u " +
                                                    "WHERE userid = :userid");

        query.setParameter("userid",userId);
        Number n = (Number)query.getSingleResult();
        return n!=null? n.intValue() : null;
    }

    @Override
    public User findById ( long id){
         return em.find(User.class, id);
     }

    @Override
    public User create(String username, String password, String profileImage) {
        final User user = new User(username, password,profileImage, BigDecimal.ZERO,null);
        em.persist(user);
        em.flush();
        return user;
    }

    @Override
    public Factory create(Factory factory) {
        em.persist(factory);
        User u = findById(factory.getUserid());
        u.getFactories().add(factory);
        update(u);
        em.flush();
        return factory;
    }

    @Override
    public Wealth create(Wealth w) {
        if(w.getStorage().rawMap().isEmpty() || w.getProductions().rawMap().isEmpty()) {
            return null;
        }
        User u = findById(w.getUserid());
        u.setWealth(w);
        update(u);
        em.flush();
        return w;
    }

    @Override
    public String getProfileImage(long userid) {
        return findById(userid).getProfileImage();
    }

    @Override
    public Productions getUserProductions(long userid) {
        return getUserWealth(userid).getProductions();
    }

    @Override
    public Storage getUserStorage(long userid) {
        return getUserWealth(userid).getStorage();
    }

    @Override
    public User resetPassword(User u,String newPassword){
        u.setPassword(newPassword);
        return em.merge(u);
    }

    @Override
    public User update(User u) {
        if(u.getFactories().isEmpty()){
            u.setFactories(getUserFactories(u.getId()));
        }
        if(u.getClanId() != null){
            clanDao.getClanById(u.getClanId());
        }
        return em.merge(u);
    }

    @Override
    public Collection<Factory> getUserFactories(long userid) {
        return findById(userid).getFactories();
    }

    @Override
    public Factory update(Factory factory) {
        em.merge(factory);
        return factory;
    }

    @Override
    public Wealth getUserWealth(long userid) {
        return findById(userid).getWealth();
    }

    @Override
    public Wealth update(Wealth w) {
        if(w.getStorage().rawMap().isEmpty() || w.getProductions().rawMap().isEmpty()) {
            return null;
        }
        User u = findById(w.getUserid());
        u.setWealth(w);
        em.merge(w);
        return w;
    }

    @Override
    public Paginating<User> globalUsers(int page, int userPerPage) {
        if(page<=0 || userPerPage<=0) {
            throw new IllegalArgumentException("Page and maxPage must be an positive integer");
        }

        int min = (page -1) * userPerPage;

        final TypedQuery<User> query = em.createQuery( "from User as u order by u.score desc" , User.class);
        query.setFirstResult(min);
        query.setMaxResults(userPerPage);
        List<User> users = query.getResultList();


        Number amount = (Number)em.createQuery("SELECT COUNT(*) FROM User",Number.class).getSingleResult();

        if(users.isEmpty()) {
            return null;
        } else {
            int totalUsers = amount.intValue();
            int totalPages = (int)Math.ceil(totalUsers/((double)userPerPage));
            return new Paginating<>(page,userPerPage,amount.intValue(),totalPages,users);
        }
    }

}
