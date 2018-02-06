package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.clan.Clan;
import ar.edu.itba.paw.model.clan.ClanBattle;
import ar.edu.itba.paw.model.packages.Paginating;

import java.util.Collection;

/**
 * Created by juanfra on 17/05/17.
 */
public interface ClanService {
    Clan createClan(String name);
    Clan getClanByName(String name);
    Clan getClanById(int clanId);
    Collection<String> findByKeyword(String search);
    Clan addUserToClan(int clanId, long userId);

    boolean deleteFromClan(long userId);
    Paginating<Clan> globalClans(int page, int clansPerPage);
    Integer getGlobalRanking(int clanid);

    ClanBattle getClanBattle(int clanid);
}
