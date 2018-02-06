package ar.edu.itba.paw.webapp.DTO.map;

import ar.edu.itba.paw.model.ResourceType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@XmlRootElement
public class ResourceEntryDTO {
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "storage")
    private Double storage;
    @XmlElement(name = "production")
    private Double production;

    ResourceEntryDTO(ResourceType resourceType, Double storage, Double production) {
        this.id = resourceType.getId();
        this.name = resourceType.getNameCode();
        this.storage = storage;
        this.production = production;
    }

    public ResourceEntryDTO(){}

    public static List<ResourceEntryDTO> fillCollection(
            Map<ResourceType, BigDecimal> storage,
            Map<ResourceType, BigDecimal> productions) {

        List<ResourceEntryDTO> resources = new ArrayList<>();
        Stream.of(ResourceType.values()).forEach((r) -> {
            Double resStorage = null;
            Double resProduction = null;
            if (storage != null && storage.get(r)!=null) resStorage = storage.get(r).doubleValue();
            if (productions != null && productions.get(r)!=null) resProduction = productions.get(r).doubleValue();
            if (resStorage != null || resProduction != null) {
                resources.add(new ResourceEntryDTO(r, resStorage, resProduction));
            }
        });

        return resources;
    }
}
