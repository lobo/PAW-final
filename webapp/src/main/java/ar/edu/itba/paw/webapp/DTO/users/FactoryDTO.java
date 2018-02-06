package ar.edu.itba.paw.webapp.DTO.users;

import ar.edu.itba.paw.model.Factory;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

/**
 * Created by juanfra on 08/08/17.
 */
@XmlRootElement
public class FactoryDTO {

    public static String url = "users/%s/factories/%s";

    @XmlElement(name = "id")
    private Integer id;

    @XmlElement(name = "type")
    private String type;

    @XmlElement(name = "amount")
    private double amount;

    @XmlElement(name = "level")
    private int level;

    @XmlElement(name = "upgradeURL")
    private URI upgradeURL;

    @XmlElement(name = "buyLimits_url")
    private URI buyLimitsURL;

    @XmlElement(name = "recipe_url")
    private URI recipeURL;

    public FactoryDTO(){}

    public FactoryDTO(Factory factory, URI baseUri) {
        id = factory.getType().getId();
        type = factory.getType().getNameCode();
        amount = factory.getAmount().doubleValue();
        level = factory.getLevel();
        upgradeURL = baseUri.resolve(String.format(UpgradeDTO.url, factory.getUserid(), factory.getType().getId()));
        buyLimitsURL = baseUri.resolve(String.format(BuyLimitsDTO.url, factory.getUserid(), factory.getType().getId()));
        recipeURL = baseUri.resolve(String.format(FactoryRecipeDTO.url, factory.getUserid(), factory.getType().getId()));

    }
}
