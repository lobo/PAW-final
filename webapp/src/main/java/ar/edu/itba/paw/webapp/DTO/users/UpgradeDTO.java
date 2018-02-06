package ar.edu.itba.paw.webapp.DTO.users;

import ar.edu.itba.paw.model.Upgrade;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UpgradeDTO {
    static String url = "users/%s/factories/%s/upgrade";

    @XmlElement(name = "type")
    private Integer type;

    @XmlElement(name = "factory_type_id")
    private Integer factoryTypeID;

    @XmlElement(name = "factory_type")
    private String factoryType;

    @XmlElement(name = "level")
    private long level;

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "cost")
    private double cost;

    UpgradeDTO(){}

    public UpgradeDTO(Upgrade upgrade) {
        factoryType = upgrade.getFactoryId().getNameCode();
        factoryTypeID = upgrade.getFactoryId().getId();
        level = upgrade.getLevel();
        description = upgrade.getDescription();
        cost = upgrade.getCost();
        type = upgrade.getType().ordinal();
    }
}
