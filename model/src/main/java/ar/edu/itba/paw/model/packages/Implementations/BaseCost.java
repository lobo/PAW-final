package ar.edu.itba.paw.model.packages.Implementations;

import ar.edu.itba.paw.model.ResourceType;
import ar.edu.itba.paw.model.packages.Creator;
import ar.edu.itba.paw.model.packages.PackageBuilder;
import ar.edu.itba.paw.model.packages.ResourcePackage;
import ar.edu.itba.paw.model.packages.Validator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

/**
 * Created by juanfra on 10/04/17.
 */
public class BaseCost extends ResourcePackage {
    private final static Validator<BigDecimal> VALIDATOR = (d) -> d.signum()>=0 && d.remainder(ONE).compareTo(ZERO)==0;
    private final static Creator<BaseCost> CREATOR = (pb) -> new BaseCost(pb.getResources());

    private BaseCost(Map<ResourceType, BigDecimal> map) {
        resources = super.generate(map,VALIDATOR);
        formatter = (d) -> formatValue(d,true);
    }

    public static PackageBuilder<BaseCost> packageBuilder() {
        return new PackageBuilder<>(VALIDATOR,CREATOR);
    }

    public FactoryCost calculateCost(BigDecimal currentAmount, BigDecimal costReduction, long amountToBuy) {
        PackageBuilder<FactoryCost> builder = FactoryCost.packageBuilder();
        resources.forEach(
                (r,d) -> builder.putItem(r,
                        d.multiply(gDiff(currentAmount, currentAmount.add(BigDecimal.valueOf(amountToBuy))))
                        .multiply(costReduction).setScale(0,RoundingMode.HALF_UP)
                )
        );
        return builder.buildPackage();
    }

    private BigDecimal gDiff(BigDecimal start, BigDecimal end)
    {
        return baseCostOfFactories(end).subtract(baseCostOfFactories(start));
    }

    public static BigDecimal baseCostOfFactories(BigDecimal n) {
        return n.multiply(n.add(ONE)).divide(ONE.add(ONE), RoundingMode.HALF_DOWN);
    }
}
