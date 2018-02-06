package ar.edu.itba.paw.webapp.DTO.factories;

import ar.edu.itba.paw.model.FactoryType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement
public class BaseFactoryDTO {
    @XmlElement(name = "id")
    int id;

    @XmlElement(name = "type")
    String type;

    @XmlElement(name = "recipe_url")
    URI recipe_url;

    public BaseFactoryDTO(){}

    public BaseFactoryDTO(FactoryType factoryType, URI baseUri) {
        this.id = factoryType.getId();
        this.type = factoryType.getNameCode();
        this.recipe_url = baseUri.resolve(String.format(BaseFactoryRecipeDTO.url, id));
    }
}
