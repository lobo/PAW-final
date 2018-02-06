package ar.edu.itba.paw.webapp.DTO.factories;

import ar.edu.itba.paw.model.FactoryType;
import ar.edu.itba.paw.model.ResourceType;
import ar.edu.itba.paw.webapp.DTO.map.ResourceEntryDTO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@XmlRootElement
public class BaseFactoryRecipeDTO {

    public static String url = "factories/%s/recipe";

    @XmlElement(name = "factory_id")
    int factoryId;

    @XmlElement(name = "factory_type")
    String type;

    @XmlElement(name = "recipe")
    List<ResourceEntryDTO> recipe;

    public BaseFactoryRecipeDTO(){}
    public BaseFactoryRecipeDTO(FactoryType factoryType) {
        factoryId = factoryType.getId();
        type = factoryType.getNameCode();
        recipe = ResourceEntryDTO.fillCollection(factoryType.getBaseCost().rawMap(), factoryType.getBaseRecipe().rawMap());
    }

}
