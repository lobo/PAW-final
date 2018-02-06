package ar.edu.itba.paw.model.packages.Implementations;
import ar.edu.itba.paw.model.ResourceType;
import ar.edu.itba.paw.model.packages.Creator;
import ar.edu.itba.paw.model.packages.PackageBuilder;
import ar.edu.itba.paw.model.packages.ResourcePackage;
import ar.edu.itba.paw.model.packages.Validator;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by juanfra on 08/04/17.
 */
public class Productions extends ResourcePackage {
    private final static Validator<BigDecimal> VALIDATOR = (d) -> d.signum()>=0;
    private final static Creator<Productions> CREATOR = (pb) -> new Productions(pb.getResources());

    private Productions(Map<ResourceType, BigDecimal> map) {
        resources = super.generate(map, VALIDATOR);
        formatter = (d) -> formatValue(d, false) + "/s";
    }

    public static PackageBuilder<Productions> packageBuilder() {
        return new PackageBuilder<>(VALIDATOR, CREATOR);
    }

    public Map<ResourceType, BigDecimal> getProductions() {
        return resources;
    }
}
