package ar.edu.itba.paw.webapp.DTO.market;

import ar.edu.itba.paw.model.ResourceType;

import javax.annotation.Resource;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement
public class MarketEntryDTO {

    @XmlElement(name = "id")
    private int id;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "price")
    private BigDecimal price;

    public MarketEntryDTO(){}

    public MarketEntryDTO(ResourceType resourceType, BigDecimal price) {
        this.id = resourceType.getId();
        this.name = resourceType.getNameCode();
        this.price = price;
    }
}
