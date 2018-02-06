package ar.edu.itba.paw.webapp.DTO.factories;

import ar.edu.itba.paw.model.FactoryType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@XmlRootElement
public class AllBaseFactories {

    @XmlElement(name = "factories")
    private List<BaseFactoryDTO> factories;

    public AllBaseFactories(){}

    public AllBaseFactories(URI baseURI) {
        factories = Stream.of(FactoryType.values()).map(f -> new BaseFactoryDTO(f, baseURI)).collect(Collectors.toList());
    }
}
