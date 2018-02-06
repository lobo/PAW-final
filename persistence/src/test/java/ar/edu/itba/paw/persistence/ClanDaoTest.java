package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.ClanDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.clan.Clan;
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

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class ClanDaoTest {
    private static final String CLAN_NAME = "AwesomeClan";
    private static User USER = new User(1,"pepe","pass","as.jpg");
    private static User USER2 = new User(1,"pepe2","pass","as.jpg");

    @Autowired
    private DataSource ds;
    @Autowired
    private ClanDao clanDao;
    @Autowired
    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
        USER = userDao.create(USER.getUsername(),USER.getPassword(),USER.getProfileImage());
        USER2 = userDao.create(USER2.getUsername(),USER2.getPassword(),USER2.getProfileImage());
    }

    @Test
    public void testCreateClan() {
        Clan clan =clanDao.createClan(CLAN_NAME);
        assertNotNull(clan);
        assertEquals(1,clan.getId());
        assertTrue(clan.getUsers().isEmpty());
    }

    @Test
    public void testGetClanById() {
        clanDao.createClan(CLAN_NAME);
        Clan clan = clanDao.getClanById(1);
        assertNotNull(clan);
        assertEquals(CLAN_NAME,clan.getName());
        assertTrue(clan.getUsers().isEmpty());
    }

    @Test
    public void testGetClanByName() {
        clanDao.createClan(CLAN_NAME);
        Clan clan = clanDao.getClanByName(CLAN_NAME);
        assertNotNull(clan);
        assertEquals(1,clan.getId());
        assertTrue(clan.getUsers().isEmpty());
    }

    @Test
    public void testUpdateClan() {
        Clan original = clanDao.createClan(CLAN_NAME);
        clanDao.addToClan(original.getId(),USER.getId());
        Clan clan = clanDao.getClanByName(CLAN_NAME);
        assertEquals(clan.getUsers().size(),1);
        User clanUser = clan.getUser(USER.getId());
        assertNotNull(clanUser);
        assertEquals(clanUser.getClanId().intValue(),original.getId());
    }

    @Test
    public void testDeleteFromClan() {
        Clan clan = clanDao.createClan(CLAN_NAME);
        clanDao.addToClan(clan.getId(),USER.getId());
        clanDao.addToClan(clan.getId(),USER2.getId());
        clanDao.removeFromClan(USER.getId());

        Clan newCLan = clanDao.getClanById(clan.getId());
        assertEquals(1,newCLan.getId());
        assertEquals(1,newCLan.getUsers().size());
    }


    @Test
    public void testDeleteClan() {
        Clan clan = clanDao.createClan(CLAN_NAME);
        clanDao.addToClan(clan.getId(),USER.getId());
        clanDao.removeFromClan(USER.getId());

        assertEquals(1,clan.getId());
        assertNull(clanDao.getClanByName(CLAN_NAME));
    }

}
