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
public class BaseRecipe extends ResourcePackage {
    private final static Validator<BigDecimal> VALIDATOR = (d) -> d.compareTo(ZERO)!=0;
    private final static Creator<BaseRecipe> CREATOR = (pb) -> new BaseRecipe(pb.getResources());

    private BaseRecipe(Map<ResourceType, BigDecimal> map) {
        resources = super.generate(map,VALIDATOR);
        formatter = (d) -> formatValue(d,false) + "/s";
    }

    public static PackageBuilder<BaseRecipe> packageBuilder() {
        return new PackageBuilder<>(VALIDATOR,CREATOR);
    }

    public Recipe calculateRecipe(BigDecimal inputReduction, BigDecimal outputMultiplier, long level){
        PackageBuilder<Recipe> builder = Recipe.packageBuilder();
        resources.forEach(
                (r,d) -> builder.putItem(r,
                        d.compareTo(ZERO)>0 ?
                        d.multiply(outputMultiplier) :
                        d.multiply(inputReduction)
                )
        );
        return builder.buildPackage();
    }

    public FactoriesProduction calculateProduction(BigDecimal amount, BigDecimal inputReduction, BigDecimal outputMultiplier, long level){
        PackageBuilder<FactoriesProduction> builder = FactoriesProduction.packageBuilder();
        resources.forEach(
                (r,d) -> builder.putItem(r,
                        d.compareTo(ZERO)>0 ?
                                d.multiply(outputMultiplier).multiply(amount) :
                                d.multiply(inputReduction).multiply(amount)
                )
        );
        return builder.buildPackage();
    }

    public Map<ResourceType,String> getFormattedInputs(){
        return super.getFormattedInputs();
    }

    public Map<ResourceType,String> getFormattedOutputs(){
        return super.getFormattedOutputs();
    }

    public Map<ResourceType, BigDecimal> getInputs() {
        return super.getInputs();
    }

    public Map<ResourceType, BigDecimal> getOutputs(){
        return super.getOutputs();
    }

}
