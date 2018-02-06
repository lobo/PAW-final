package ar.edu.itba.paw.webapp.DTO.search;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement
public class SearchResultDTO {


    @XmlElement(name = "type")
    private String type;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "id")
    private Long id;

    @XmlElement(name = "url")
    private URI url;


    public SearchResultDTO(){}

    public SearchResultDTO(SearchResultType type, URI resourceURL, String name, Long id) {
        this.type = type.name;
        this.name = name;
        this.id = id;
        url=resourceURL;
    }

}
