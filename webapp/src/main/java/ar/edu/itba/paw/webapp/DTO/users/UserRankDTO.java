package ar.edu.itba.paw.webapp.DTO.users;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement
public class UserRankDTO {

    public static String url = "users/%s/rank";

    @XmlElement(name = "user_id")
    private int id;

    @XmlElement(name = "user_url")
    private URI userURL;

    @XmlElement(name = "rank")
    private int rank;

    public UserRankDTO(){}

    public UserRankDTO(int id, int rank, URI baseUri) {
        this.id = id;
        this.userURL = baseUri.resolve(String.format(UserDTO.url, id));
        this.rank = rank;
    }
}

