package ar.edu.itba.paw.webapp.DTO.clans;

import ar.edu.itba.paw.model.clan.Clan;
import ar.edu.itba.paw.model.clan.ClanBattle;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.net.URI;

/**
 * Created by juanfra on 22/08/17.
 */
@XmlRootElement
@XmlType(name = "Clan")
public class ClanDTO {

    public static String url = "clans/%s";

    @XmlElement(name = "id")
    private int id;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "score")
    private double score;

    @XmlElement(name = "users_url")
    private URI usersURL;

    @XmlElement(name = "wins")
    private int wins = 0;

    @XmlElement(name = "battles")
    private int battles = 0;

    @XmlElement(name = "image")
    private String image;

    @XmlElement(name = "battle_url")
    private URI battleURL;

    @XmlElement(name = "clan_rank_url")
    private URI clanRankURL;


    public ClanDTO(){}

    public ClanDTO(Clan clan, ClanBattle clanBattle, URI baseUri){
        this.id = clan.getId();
        this.name = clan.getName();
        this.score = clan.getScore().doubleValue();
        this.wins = clan.getWins();
        this.battles = clan.getBattles();
        this.image = baseUri.resolve("resources/group_icons/" + clan.getImage()).toString().replace("/v1/","/");        this.usersURL = baseUri.resolve(String.format(ClanUsersDTO.url, clan.getId()));
        this.clanRankURL = baseUri.resolve(String.format(ClanRankDTO.url, clan.getId()));
        if(clanBattle != null) {
            this.battleURL = baseUri.resolve(String.format(ClanBattleDTO.url, clan.getId()));
        }
    }
}
