package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.packages.Implementations.*;
import ar.edu.itba.paw.model.packages.PackageBuilder;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by juanfra on 03/04/17.
 */
@Entity
@Table(name = "users")
public class Wealth {

    @Id
    @Column(name = "userid")
    private long userid;

    @Transient
    private Storage storage;

    @Transient
    private Productions productions;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "userid")
    private Set<UserWealth> wealths = new HashSet<>();

    @PostLoad
    private void postLoad(){
        PackageBuilder<Storage> sb = Storage.packageBuilder();
        PackageBuilder<Productions> pb = Productions.packageBuilder();
        wealths.forEach((w) -> {
            ResourceType r = w.getResourceType();
            Calendar lu = Calendar.getInstance();
            lu.setTimeInMillis(w.getLastupdated());
            sb.putItemWithDate(r,w.getStorage(),lu);
            pb.putItem(r,w.getProduction());

        });

        productions = pb.buildPackage();
        storage = sb.buildPackage();
    }

    Wealth(){}

    public Wealth(long userid, @NotNull Storage storage, @NotNull Productions productions) {
        if ( !storage.getResources().equals(productions.getResources()) ) {
            throw new IllegalArgumentException("Storage and Productions must have same resources");
        }
        this.userid = userid;
        this.storage = storage;
        this.productions = productions;

        wealths = new HashSet<>();
        if(productions.getResources().size() != ResourceType.values().length){
            PackageBuilder<Productions> pb = Productions.packageBuilder();
            PackageBuilder<Storage> sb = Storage.packageBuilder();
            pb.putItems(productions);
            sb.putItems(storage);
            Calendar now = Calendar.getInstance();
            storage.getLastUpdated().forEach(sb::setLastUpdated);
            Stream.of(ResourceType.values()).filter((r)->!pb.getResources().containsKey(r))
                    .forEach((r)->{
                        pb.putItem(r,BigDecimal.ZERO);
                        sb.putItemWithDate(r,BigDecimal.ZERO,now);
                    });

            this.productions = pb.buildPackage();
            this.storage = sb.buildPackage();
        }

        for(ResourceType r: ResourceType.values()){
            UserWealth uw = new UserWealth();
            uw.setId(userid,r.getId());
            uw.setProduction(this.productions.getValue(r));
            uw.setStorage(this.storage.getValue(r));
            uw.setLastupdated(this.storage.getLastUpdated(r).getTimeInMillis());
            wealths.add(uw);
        }
    }

    public long getUserid() {
        return userid;
    }

    public Storage getStorage() {
        return storage.getUpdatedStorage(productions);
    }

    public Productions getProductions() {
        return productions;
    }

    public Wealth purchaseResult(@NotNull  Factory f,long amountToBuy) {
        if (!f.isBuyable(this,amountToBuy)) {
            return null;
        }

        Storage calculatedStorage = getStorage();
        Recipe recipe = f.getRecipe();
        FactoryCost cost = f.getCost(amountToBuy);
        PackageBuilder<Storage> storageBuilder = Storage.packageBuilder();
        PackageBuilder<Productions> productionsBuilder = Productions.packageBuilder();


        for (ResourceType r: ResourceType.values() ) {
            storageBuilder.putItemWithDate(r,calculatedStorage.getValue(r),calculatedStorage.getLastUpdated(r));
            productionsBuilder.putItem(r,productions.getValue(r));

            if (cost.contains(r)) {
                storageBuilder.addItem(r, cost.getValue(r).negate());
            }

            if (recipe.contains(r)){
                productionsBuilder.addItem(r,recipe.getValue(r).multiply(BigDecimal.valueOf(amountToBuy)));
            }
        }

        return new Wealth(userid,storageBuilder.buildPackage(),productionsBuilder.buildPackage());
    }

    public Wealth upgradeResult(@NotNull Factory f) {

        if(!f.isUpgreadable(this)) {
            return null;
        }

        Upgrade u = f.getNextUpgrade();
        FactoriesProduction factoriesProduction = f.getFactoriesProduction();

        Storage calculatedStorage = getStorage();
        PackageBuilder<Storage> storageBuilder = Storage.packageBuilder();
        PackageBuilder<Productions> productionsBuilder = Productions.packageBuilder();

        calculatedStorage.rawMap().forEach(storageBuilder::putItem);
        calculatedStorage.getLastUpdated().forEach(storageBuilder::setLastUpdated);
        storageBuilder.addItem(ResourceType.MONEY,BigDecimal.valueOf(-u.getCost()));

        productions.rawMap().forEach(productionsBuilder::putItem);
        Map<ResourceType, BigDecimal> factoryInputs = f.getFactoriesProduction().getInputs();
        Map<ResourceType, BigDecimal> factoryOutputs = f.getFactoriesProduction().getOutputs();


        if (u.getInputReduction().compareTo(BigDecimal.ONE) != 0) {
            factoriesProduction.getInputs().forEach(
                    (r, d) -> {
                        productionsBuilder.addItem(r,d);
                        productionsBuilder.addItem(r, factoryInputs.get(r).multiply(u.getInputReduction()).negate());
                    }
            );
        }

        if (u.getOutputMultiplier().compareTo(BigDecimal.ONE) != 0) {
            factoriesProduction.getOutputs().forEach(
                    (r, d) -> {
                        productionsBuilder.addItem(r,  factoryOutputs.get(r).multiply(u.getOutputMultiplier()));
                        productionsBuilder.addItem(r, d.negate());
                    }
            );
        }

        return new Wealth(userid,storageBuilder.buildPackage(),productionsBuilder.buildPackage());
    }

    public Wealth calculateProductions(@NotNull  Collection<Factory> factories) {
        PackageBuilder<Productions> productionsBuilder = Productions.packageBuilder();
        Arrays.stream(ResourceType.values()).forEach((r) -> productionsBuilder.putItem(r,BigDecimal.ZERO));
        factories.stream()
                .filter(f -> f.getAmount().signum()>0)
                .map(Factory::getFactoriesProduction)
                .map(FactoriesProduction::getOutputs)
                .forEach((m) -> m.forEach(productionsBuilder::addItem));

        factories.stream()
                .filter(f -> f.getAmount().signum()>0)
                .map(Factory::getFactoriesProduction)
                .map(FactoriesProduction::getInputs)
                .forEach((m) -> m.forEach(
                        (r,d) -> productionsBuilder.addItem(r,d.negate())));

        return new Wealth(userid,storage,productionsBuilder.buildPackage());
    }

    public Wealth addResource(ResourceType resource, BigDecimal amount) {
        Storage calculatedStorage = getStorage();
        PackageBuilder<Storage> storageBuilder = Storage.packageBuilder();

        calculatedStorage.rawMap().forEach(storageBuilder::putItem);
        calculatedStorage.getLastUpdated().forEach(storageBuilder::setLastUpdated);
        storageBuilder.addItem(resource,amount);
        return new Wealth(userid,storageBuilder.buildPackage(),productions);
    }

    public BigDecimal calculateScore() {
        return productions.rawMap().entrySet().stream()
                .map((e) -> e.getValue().multiply(e.getKey().getBasePrice()))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public static Wealth createWealth(long userId) {
        PackageBuilder<Storage> storageBuilder = Storage.packageBuilder();
        PackageBuilder<Productions> productionsBuilder = Productions.packageBuilder();
        Calendar now = Calendar.getInstance();

        Arrays.stream(ResourceType.values()).forEach((r) ->  {
            storageBuilder.putItemWithDate(r,BigDecimal.ZERO,now);
            productionsBuilder.putItem(r,BigDecimal.ZERO);
        });
        storageBuilder.addItem(ResourceType.MONEY,ResourceType.initialMoney());
        return new Wealth(userId,storageBuilder.buildPackage(),productionsBuilder.buildPackage());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wealth wealth = (Wealth) o;
        return userid == wealth.userid;

    }

    @Override
    public int hashCode() {
        return (int) (userid ^ (userid >>> 32));
    }
}
