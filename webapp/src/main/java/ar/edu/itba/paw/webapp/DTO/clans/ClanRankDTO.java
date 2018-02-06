package ar.edu.itba.paw.webapp.DTO.clans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement
public class ClanRankDTO {

    public static String url = "clans/%s/rank";

    @XmlElement(name = "clan_id")
    private int id;

    @XmlElement(name = "clan_url")
    private URI clanURL;

    @XmlElement(name = "rank")
    private int rank;

    public ClanRankDTO(){}

    public ClanRankDTO(int id, int rank, URI baseUri) {
        this.id = id;
        this.clanURL = baseUri.resolve(String.format(ClanDTO.url, id));
        this.rank = rank;
    }
}
