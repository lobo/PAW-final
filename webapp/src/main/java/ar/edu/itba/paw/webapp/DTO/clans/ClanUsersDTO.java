package ar.edu.itba.paw.webapp.DTO.clans;

import ar.edu.itba.paw.model.clan.Clan;
import ar.edu.itba.paw.webapp.DTO.users.UserDTO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement
public class ClanUsersDTO {

    public static String url = "clans/%s/users";

    @XmlElement(name = "clan_id")
    private int clanID;

    @XmlElement(name = "members")
    private int members;

    @XmlElement(name = "users")
    private List<UserDTO> users;

    ClanUsersDTO(){}

    public ClanUsersDTO(Clan clan, URI baseUri) {
        clanID = clan.getId();
        members = clan.getUsers().size();
        users = clan.getUsers().stream().map(u -> new UserDTO(u, baseUri)).collect(Collectors.toList());
    }

    public List<UserDTO> getUsers() {
        return users;
    }
}
