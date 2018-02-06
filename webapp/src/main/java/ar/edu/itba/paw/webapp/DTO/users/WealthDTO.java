package ar.edu.itba.paw.webapp.DTO.users;

import ar.edu.itba.paw.model.ResourceType;
import ar.edu.itba.paw.model.Wealth;
import ar.edu.itba.paw.model.packages.Implementations.Productions;
import ar.edu.itba.paw.model.packages.Implementations.Storage;
import ar.edu.itba.paw.webapp.DTO.map.ResourceEntryDTO;

import javax.annotation.Resource;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by juanfra on 08/08/17.
 */
@XmlType(name = "Wealth")
@XmlRootElement
public class WealthDTO {

    static String url = "users/%s/wealth";

    @XmlElement(name = "user_id")
    private long userID;

    @XmlElement(name = "resources")
    private List<ResourceEntryDTO> resources = new ArrayList<>();


    public WealthDTO(){}

    public WealthDTO(Wealth wealth, long userID) {
        this.userID = userID;
        resources = ResourceEntryDTO.fillCollection(wealth.getStorage().rawMap(), wealth.getProductions().rawMap());
    }

}
