package ar.edu.itba.paw.model.packages.Implementations;

import ar.edu.itba.paw.model.ResourceType;
import ar.edu.itba.paw.model.packages.Creator;
import ar.edu.itba.paw.model.packages.PackageBuilder;
import ar.edu.itba.paw.model.packages.ResourcePackage;
import ar.edu.itba.paw.model.packages.Validator;

import java.math.BigDecimal;
import java.util.Map;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

/**
 * Created by juanfra on 08/04/17.
 */
public class FactoryCost extends ResourcePackage {

    private final static Validator<BigDecimal> VALIDATOR = (d) -> d.signum()>=0 && d.remainder(ONE).compareTo(ZERO)==0;
    private final static Creator<FactoryCost> CREATOR = (pb) -> new FactoryCost(pb.getResources());

    private FactoryCost(Map<ResourceType, BigDecimal> map) {
        resources = super.generate(map,VALIDATOR);
        formatter = (d) -> formatValue(d,true);
    }

    public static PackageBuilder<FactoryCost> packageBuilder() {
        return new PackageBuilder<>(VALIDATOR,CREATOR);
    }

    public Map<ResourceType, BigDecimal> getCost() {
        return resources;
    }
}
