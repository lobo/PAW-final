package ar.edu.itba.paw.webapp.DTO.market;

import ar.edu.itba.paw.model.ResourceType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@XmlRootElement
public class MarketDTO {
    static String url = "market";

    @XmlElement(name = "market")
    private List<MarketEntryDTO> market = new ArrayList<>();

    public MarketDTO() {
        Stream.of(ResourceType.values()).forEach( (res) -> market.add(new MarketEntryDTO(res, res.getPrice())));
    }
}
