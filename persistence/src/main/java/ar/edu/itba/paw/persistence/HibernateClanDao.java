package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.ClanDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.clan.Clan;
import ar.edu.itba.paw.model.clan.ClanBattle;
import ar.edu.itba.paw.model.clan.ClanBuilder;
import ar.edu.itba.paw.model.packages.Paginating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by juanfra on 14/06/17.
 */
@Repository
@EnableTransactionManagement(proxyTargetClass = true)
public class HibernateClanDao implements ClanDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateClanDao.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserDao userdao;

    @Override
    public Clan createClan(String name) {
        Clan clan = ClanBuilder.construct(0,name).buildClan();
        em.persist(clan);
        return clan;
    }

    @Override
    public Clan getClanByName(String name) {
        final TypedQuery<Clan> query = em.createQuery( "from Clan as c where c.name = :name" , Clan.class);
        query.setParameter( "name" , name);
        final List<Clan> list = query.getResultList();
        return list.size()!=1 ? null : list.get( 0 );
    }

    @Override
    public Clan getClanById(int clanId) {
        final TypedQuery<Clan> query = em.createQuery( "from Clan as c where c.id = :clanid" , Clan.class);
        query.setParameter( "clanid" , clanId);
        final List<Clan> list = query.getResultList();
        return list.size()!=1 ? null : list.get( 0 );
    }

    @Override
    public Collection<String> findByKeyword(String search) {
        StringBuilder s = new StringBuilder(search.toLowerCase()).append("%").insert(0,"%");
        final TypedQuery<Clan> query = em.createQuery( "from Clan as c where lower(c.name) like lower(:name)" , Clan.class);
        query.setParameter( "name" , s.toString());
        final List<Clan> list = query.getResultList();
        return list.stream().map(Clan::getName).collect(Collectors.toList());
    }

    @Override
    public boolean addToClan(int clanId, long userId) {
        Clan clan = getClanById(clanId);
        User u = userdao.findById(userId);
        if(clan == null || u.getClanId()!=null){
            return false;
        }

        u.setClanId(clanId);
        clan.getUsers().add(u);
        return true;

    }

    @Override
    public boolean removeFromClan(long userId) {
        User u = userdao.findById(userId);
        if(u.getClanId()==null) {
            return false;
        }

        Clan clan = getClanById(u.getClanId());
        if(clan == null){
            return false;
        }

        u.setClanId(null);
        Iterator<User> it = clan.getUsers().iterator();
        while (it.hasNext()){
            if(it.next().getId() == u.getId()){
                it.remove();
                break;
            }
        }

        if(clan.getUsers().isEmpty()){
            em.remove(clan);
        }

        return true;
    }

    @Override
    public Paginating<Clan> globalClan(int page, int clansPerPage) {
        if(page<=0 || clansPerPage<=0) {
            throw new IllegalArgumentException("Page and maxPage must be an positive integer");
        }
        Number amount = (Number) em.createQuery("SELECT COUNT(*) FROM Clan", Number.class).getSingleResult();
        int min = (page - 1) * clansPerPage;

            final TypedQuery<Clan> query = em.createQuery("from Clan as c order by c.score desc", Clan.class);
            query.setFirstResult(min);
            query.setMaxResults(clansPerPage);

            List<Clan> clans = query.getResultList();


            if (!clans.isEmpty()) {
                int totalClans = amount.intValue();
                int totalPages = (int) Math.ceil(totalClans / ((double) clansPerPage));
                return new Paginating<>(page, clansPerPage, amount.intValue(), totalPages, clans);
            } else {
                return null;
            }

    }

    @Override
    public Integer getGlobalRanking(int clanid) {
        Query query = em.createNativeQuery("SELECT row_number FROM " +
                "(SELECT ROW_NUMBER() OVER(ORDER BY score DESC),* FROM clans) as c " +
                "WHERE c.clanid = :clanid");

        query.setParameter("clanid",clanid);
        Number n = (Number)query.getResultList().stream().findFirst().orElse(null);
        return n==null?null:n.intValue();
    }

    @Override
    public ClanBattle getClanBattle(int clanid) {
        Query query = em.createQuery("from ClanBattle as c where c.clan.id = :clanid",ClanBattle.class);
        query.setParameter("clanid",clanid);
        List<ClanBattle> clanBattle = query.getResultList();
        return clanBattle.size()!=1?null:clanBattle.get(0);
    }

    @Transactional
    public void calculateNextBattles(){
        calculateWins();
        em.flush();
        Query query = em.createNativeQuery("truncate TABLE clansbattle");
        query.executeUpdate();
        em.flush();
        Paginating<Clan> pagClan = globalClan(1,2);
        if(pagClan==null){
            return;
        }
        for(int i=1;i<=pagClan.getTotalPages();i++) {
            Paginating<Clan> clan = globalClan(i,2);
            List<Clan> clans = clan.getItems();
            if(clans.size()<2) {
                return;
            }

            Clan clan1 = clans.get(0);
            Clan clan2 = clans.get(1);
            ClanBattle clanBattle = new ClanBattle(clan1,clan2,clan1.getClanScore());
            em.persist(clanBattle);
            ClanBattle clanBattleV = new ClanBattle(clan2,clan1,clan2.getClanScore());
            em.persist(clanBattleV);
        }
    }

    private void calculateWins() {
        Query query = em.createQuery("from ClanBattle as c order by c.initialScore desc", ClanBattle.class);
        List<ClanBattle> cbs = query.getResultList();
        Iterator<ClanBattle> it = cbs.iterator();
        while (it.hasNext()) {
            ClanBattle cb1 = it.next();
            if (cb1.getVersus() == null) {
                cb1.getClan().addWin();
                cb1.getClan().addBattle();
                em.merge(cb1.getClan());
                em.remove(cb1);
            } else {
                if (!it.hasNext()) {
                    LOGGER.error("Versus clan not found");
                    return;
                }
                ClanBattle cb2 = it.next();
                if (cb1.getVersus().getId() != cb2.getClan().getId()) {
                    LOGGER.error("Versus clans id does not match");
                    return;
                }

                Clan c1 = cb1.getClan();
                Clan c2 = cb2.getClan();
                c1.addBattle();
                c2.addBattle();
                if (c1.getScore().subtract(cb1.getInitialScore()).compareTo(c2.getScore().subtract(cb2.getInitialScore()))>0) {
                    c1.addWin();
                } else if (c1.getScore().subtract(cb1.getInitialScore()).compareTo(c2.getScore().subtract(cb2.getInitialScore()))<0) {
                    c2.addWin();
                }
                em.merge(c1);
                em.merge(c2);
                em.remove(cb1);
                em.remove(cb2);

            }
        }
    }

}
