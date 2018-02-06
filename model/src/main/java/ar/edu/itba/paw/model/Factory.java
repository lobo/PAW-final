package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.packages.BuyLimits;
import ar.edu.itba.paw.model.packages.Implementations.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import static java.math.BigDecimal.ROUND_FLOOR;
import static java.math.RoundingMode.DOWN;
import static java.math.RoundingMode.HALF_DOWN;
import static java.math.RoundingMode.HALF_EVEN;

@Entity
@Table(name = "factories")
@IdClass(Factory.FactoryID.class)
public class Factory implements Comparable<Factory> {

    @Id
    @Column(nullable = false)
    private long userid;

    @Id
    @Column(name = "type", nullable = false)
    private int _type;

    @Transient
    private FactoryType type;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "inputreduction")
    private BigDecimal inputReduction;

    @Column(name = "outputmultiplier")
    private BigDecimal outputMultiplier;

    @Column(name = "costreduction")
    private BigDecimal costReduction;

    @Column(name = "level")
    private int level;

    @PostLoad
    private void postLoad(){
        type = FactoryType.fromId(_type);
    }

    public Factory(){}

    static class FactoryID implements Serializable {
        @Column(nullable = false)
        private long userid;
        @Column(name = "type", nullable = false)
        private int _type;
        FactoryID(){};
        FactoryID(long userid,int _type){
            this.userid = userid;
            this._type = _type;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FactoryID that = (FactoryID) o;

            if (getUserid() != that.getUserid()) return false;
            return getType() == that.getType()  ;

        }
        @Override
        public int hashCode() {
            int result = (int) (getUserid() ^ (getUserid() >>> 32));
            result = 31 * result + getType();
            return result;
        }

        public long getUserid() {
            return userid;
        }

        public void setUserid(long userid) {
            this.userid = userid;
        }

        public int getType() {
            return _type;
        }

        public void setType(int type) {
            this._type = type;
        }
    }

    public Factory(long userid, @NotNull FactoryType type, BigDecimal amount, BigDecimal inputReduction,
                   BigDecimal outputMultiplier, BigDecimal costReduction, int level) {
        this.userid = userid;
        this.type = type;
        this.amount = amount;
        this.inputReduction = inputReduction;
        this.outputMultiplier = outputMultiplier;
        this.costReduction = costReduction;
        this.level = level;
        this._type = type.getId();
    }

    public long getUserid() {
        return userid;
    }

    public FactoryType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getInputReduction() {
        return inputReduction;
    }

    public BigDecimal getOutputMultiplier() {
        return outputMultiplier;
    }

    public BigDecimal getCostReduction() {
        return costReduction;
    }

    public Recipe getRecipe() {
        return  type.getBaseRecipe()
                .calculateRecipe(inputReduction,outputMultiplier,level);
    }

    public FactoryCost getCost() {
        return getCost(1);
    }
    public FactoryCost getCost(long amountToBuy) {
        return type.getBaseCost().calculateCost(amount,costReduction,amountToBuy);
    }

    public FactoriesProduction getFactoriesProduction() {
        return type.getBaseRecipe().calculateProduction(amount,inputReduction,outputMultiplier,level);
    }

    public String getImage(){
        return getType().getId() + ".jpg";
    }

    public Factory purchaseResult(long amountToBuy) {
        return new Factory(userid,type,
                amount.add(BigDecimal.valueOf(amountToBuy)),
                inputReduction,outputMultiplier,costReduction,level);
    }

    public Factory upgradeResult(){
        Upgrade newUpgrade = getNextUpgrade();

        return new Factory(userid,type,amount,
                            newUpgrade.getInputReduction(),
                            newUpgrade.getOutputMultiplier(),
                            newUpgrade.getCostReduction(),
                            level + 1
        );
    }


    public boolean isBuyable(Wealth w, long amountToBuy) {
        if(amountToBuy==0)
            return false;

        FactoryCost cost = getCost(amountToBuy);
        Storage storage = w.getStorage();

        for (ResourceType r: cost.getResources()) {
            if(cost.getValue(r).compareTo(storage.getValue(r))>0){
                return false;
            }
        }

        Map<ResourceType,BigDecimal> need = getRecipe().getInputs();
        Productions productions = w.getProductions();

        for (ResourceType r: need.keySet()) {
            if(need.get(r).multiply(BigDecimal.valueOf(amountToBuy)).compareTo(productions.getValue(r)) > 0){
                return false;
            }
        }

        return true;
    }

    /**
     * Given an amount it checks how many factories can be bought for that resource
     */
    public BigDecimal maxFactoriesLimitedByStorage(ResourceType resource, BigDecimal price, Wealth wealth) {
        BigDecimal resourcesAvailable = wealth.getStorage().getValue(resource);
        BigDecimal a = BigDecimal.ONE;
        BigDecimal b = BigDecimal.ONE;
        BigDecimal c = BigDecimal.valueOf(-2)
                .multiply(
                        resourcesAvailable.divide(costReduction.multiply(price),ROUND_FLOOR)
                        .add(BaseCost.baseCostOfFactories(amount))
                );

        Double delta = b.multiply(b).subtract(a.multiply(c).multiply(BigDecimal.valueOf(4))).doubleValue();

        if(delta < 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal sol1 = b.negate()
                .add(BigDecimal.valueOf(Math.sqrt(delta)))
                .divide(a.multiply(BigDecimal.valueOf(2)), ROUND_FLOOR);

        BigDecimal sol2 = b.negate()
                .subtract(BigDecimal.valueOf(Math.sqrt(delta)))
                .divide(a.multiply(BigDecimal.valueOf(2)), ROUND_FLOOR);

        BigDecimal realSol = sol1.max(sol2).subtract(amount).setScale(0,ROUND_FLOOR);
        return realSol;
    }

    public BigDecimal maxFactoriesLimitedByProduction(ResourceType resource, BigDecimal productionNeeded, Wealth wealth) {
        BigDecimal currentProduction = wealth.getProductions().rawMap().get(resource);
        BigDecimal realSol = currentProduction.divide(productionNeeded,ROUND_FLOOR).setScale(0,ROUND_FLOOR);
        if(isBuyable(wealth, realSol.longValue() + 1)) {
            throw new IllegalStateException("IMPOSIBLE");
        }
        return realSol;
    }

    public BuyLimits getLimits(Wealth w){
        return new BuyLimits(w, this);
    }

    public Upgrade getNextUpgrade() {
        return Upgrade.getUpgrade(getType(),getLevel() + 1);
    }

    public int compareTo(Factory f) {
        return this.getType().getId() - f.getType().getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Factory factory = (Factory) o;

        if (userid != factory.userid) return false;
        return type == factory.type;

    }

    @Override
    public int hashCode() {
        int result = (int) (userid ^ (userid >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public int getLevel() {
        return level;
    }

    public boolean isUpgreadable(Wealth w) {
        if(getAmount().signum()>0) {
            return getNextUpgrade().isBuyable(w);
        }

        return false;
    }

}
