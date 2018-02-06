package ar.edu.itba.paw.webapp.DTO.users;

import ar.edu.itba.paw.model.FactoryType;
import ar.edu.itba.paw.model.ResourceType;
import ar.edu.itba.paw.model.packages.BuyLimits;
import ar.edu.itba.paw.webapp.DTO.map.ResourceEntryDTO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@XmlRootElement
public class BuyLimitsDTO {
    public static String url = "/users/%s/factories/%s/buyLimits";

    @XmlElement(name = "user_id")
    private long userID;

    @XmlElement(name = "factory_id")
    private int factoryID;

    @XmlElement(name = "max")
    private long max;

    @XmlElement(name = "cost_max")
    private List<ResourceEntryDTO> costMax;

    @XmlElement(name = "cost_next_max")
    private List<ResourceEntryDTO> costNextMax;

    @XmlElement(name = "cost_1")
    private List<ResourceEntryDTO> cost1;

    @XmlElement(name = "cost_10")
    private List<ResourceEntryDTO> cost10;

    @XmlElement(name = "cost_100")
    private List<ResourceEntryDTO> cost100;

    public BuyLimitsDTO(BuyLimits buyLimits, long userID, int factoryID) {
        this.userID = userID;
        this.factoryID = factoryID;
        FactoryType factoryType = FactoryType.fromId(factoryID);
        if(factoryType!=null) {
            max = buyLimits.getMaxFactories();
            if(max>0) costMax = ResourceEntryDTO.fillCollection(buyLimits.getCostMax().rawMap(), multiplyProductions(factoryType.getBaseRecipe().getInputs(), max));
            costNextMax = ResourceEntryDTO.fillCollection(buyLimits.getCostNextMax().rawMap(),  multiplyProductions(factoryType.getBaseRecipe().getInputs(), max+1));
            cost1 = ResourceEntryDTO.fillCollection(buyLimits.getCost1().rawMap(), factoryType.getBaseRecipe().getInputs());
            cost10 = ResourceEntryDTO.fillCollection(buyLimits.getCost10().rawMap(), multiplyProductions(factoryType.getBaseRecipe().getInputs(), 10));
            cost100 = ResourceEntryDTO.fillCollection(buyLimits.getCost100().rawMap(), multiplyProductions(factoryType.getBaseRecipe().getInputs(), 100));
        } else {
            throw new IllegalArgumentException("No factory with the ID " + factoryID);
        }

    }

    private Map<ResourceType, BigDecimal> multiplyProductions(Map<ResourceType, BigDecimal> map, long factor) {
        for (ResourceType k: map.keySet()) {
            map.put(k, map.get(k).multiply(BigDecimal.valueOf(factor)));
        }
        return map;
    }

    public BuyLimitsDTO(){}

}
