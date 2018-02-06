package ar.edu.itba.paw.model.packages.Implementations;

import ar.edu.itba.paw.model.ResourceType;
import ar.edu.itba.paw.model.packages.Creator;
import ar.edu.itba.paw.model.packages.PackageBuilder;
import ar.edu.itba.paw.model.packages.ResourcePackage;
import ar.edu.itba.paw.model.packages.Validator;

import java.math.BigDecimal;
import java.util.Map;

import static java.math.BigDecimal.ZERO;

/**
 * Created by juanfra on 08/04/17.
 */
public class FactoriesProduction extends ResourcePackage {

    private final static Validator<BigDecimal> VALIDATOR = (d) -> d.compareTo(ZERO)!=0;
    private final static Creator<FactoriesProduction> CREATOR = (pb) -> new FactoriesProduction(pb.getResources());

    private FactoriesProduction(Map<ResourceType, BigDecimal> map) {
        resources = super.generate(map,VALIDATOR);
        formatter = (d) -> formatValue(d,false) + "/s";
    }

    public static PackageBuilder<FactoriesProduction> packageBuilder() {
        return new PackageBuilder<>(VALIDATOR,CREATOR);
    }

    public Map<ResourceType,String> getFormattedInputs(){
        return super.getFormattedInputs();
    }

    public Map<ResourceType, BigDecimal> getInputs() {
        return super.getInputs();
    }

    public Map<ResourceType, BigDecimal> getOutputs(){
        return super.getOutputs();
    }

    public Map<ResourceType,String> getFormattedOutputs(){
        return super.getFormattedOutputs();
    }
}
