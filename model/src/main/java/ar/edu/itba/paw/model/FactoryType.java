package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.packages.Implementations.BaseCost;
import ar.edu.itba.paw.model.packages.Implementations.BaseRecipe;
import ar.edu.itba.paw.model.packages.PackageBuilder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;


public enum FactoryType {
    STOCK_INVESTMENT_BASE(0,"STOCK_INVESTOR"){
        public BaseCost getBaseCost(){
            PackageBuilder<BaseCost> costBuilder = BaseCost.packageBuilder();
            costBuilder.putItem(ResourceType.MONEY,BigDecimal.valueOf(1000));
            return costBuilder.buildPackage();
        }
        public BaseRecipe getBaseRecipe() {
            PackageBuilder<BaseRecipe> recipeBuilder = BaseRecipe.packageBuilder();
            recipeBuilder.putItem(ResourceType.MONEY,ONE);
            return recipeBuilder.buildPackage();
        }},

    PEOPLE_RECRUITING_BASE(1,"PEOPLE_RECRUITER"){
        public BaseCost getBaseCost(){
            PackageBuilder<BaseCost> costBuilder = BaseCost.packageBuilder();
            costBuilder.putItem(ResourceType.MONEY,BigDecimal.valueOf(200));
            return costBuilder.buildPackage();
        }
        public BaseRecipe getBaseRecipe() {
            PackageBuilder<BaseRecipe> recipeBuilder = BaseRecipe.packageBuilder();
            recipeBuilder.putItem(ResourceType.PEOPLE,BigDecimal.valueOf(0.3));
            return recipeBuilder.buildPackage();
        }},

    JUNK_COLLECTOR_BASE(2,"JUNK_COLLECTOR"){
        public BaseCost getBaseCost(){
            PackageBuilder<BaseCost> costBuilder = BaseCost.packageBuilder();
            costBuilder.putItem(ResourceType.PEOPLE,BigDecimal.valueOf(5));
            return costBuilder.buildPackage();
        }
        public BaseRecipe getBaseRecipe() {
            PackageBuilder<BaseRecipe> recipeBuilder = BaseRecipe.packageBuilder();
            recipeBuilder.putItem(ResourceType.TIRES,BigDecimal.valueOf(0.2));
            recipeBuilder.putItem(ResourceType.METAL_SCRAP,BigDecimal.valueOf(0.6));
            recipeBuilder.putItem(ResourceType.CARDBOARD,BigDecimal.valueOf(2));
            return recipeBuilder.buildPackage();
        }},

    METAL_SEPARATOR_BASE(3,"METAL_SEPARATOR"){
        public BaseCost getBaseCost(){
            PackageBuilder<BaseCost> costBuilder = BaseCost.packageBuilder();
            costBuilder.putItem(ResourceType.PEOPLE,BigDecimal.valueOf(2));
            return costBuilder.buildPackage();
        }
        public BaseRecipe getBaseRecipe() {
            PackageBuilder<BaseRecipe> recipeBuilder = BaseRecipe.packageBuilder();
            recipeBuilder.putItem(ResourceType.METAL_SCRAP,BigDecimal.valueOf(-3));
            recipeBuilder.putItem(ResourceType.IRON,BigDecimal.valueOf(0.6D));
            recipeBuilder.putItem(ResourceType.COPPER,BigDecimal.valueOf(0.4D));
            return recipeBuilder.buildPackage();
        }},

    RUBBER_SHREDDER_BASE(4,"RUBBER_SHREDDER"){
        public BaseCost getBaseCost(){
            PackageBuilder<BaseCost> costBuilder = BaseCost.packageBuilder();
            costBuilder.putItem(ResourceType.PEOPLE,BigDecimal.valueOf(2));
            costBuilder.putItem(ResourceType.IRON,BigDecimal.valueOf(50));
            return costBuilder.buildPackage();
        }
        public BaseRecipe getBaseRecipe() {
            PackageBuilder<BaseRecipe> recipeBuilder = BaseRecipe.packageBuilder();
            recipeBuilder.putItem(ResourceType.TIRES,BigDecimal.valueOf(-2));
            recipeBuilder.putItem(ResourceType.RUBBER,BigDecimal.valueOf(0.8D));
            return recipeBuilder.buildPackage();
        }},

    CABLE_MAKER_BASE(5,"CABLE_MAKER"){
        public BaseCost getBaseCost(){
            PackageBuilder<BaseCost> costBuilder = BaseCost.packageBuilder();
            costBuilder.putItem(ResourceType.PEOPLE,BigDecimal.valueOf(5D));
            costBuilder.putItem(ResourceType.IRON,BigDecimal.valueOf(250D));
            return costBuilder.buildPackage();
        }
        public BaseRecipe getBaseRecipe() {
            PackageBuilder<BaseRecipe> recipeBuilder = BaseRecipe.packageBuilder();
            recipeBuilder.putItem(ResourceType.COPPER,BigDecimal.valueOf(-3D));
            recipeBuilder.putItem(ResourceType.RUBBER,BigDecimal.valueOf(-5D));
            recipeBuilder.putItem(ResourceType.COPPER_CABLE,BigDecimal.valueOf(0.8D));
            return recipeBuilder.buildPackage();
        }},

    BOILER_BASE(6,"BOILER"){
        public BaseCost getBaseCost(){
            PackageBuilder<BaseCost> costBuilder = BaseCost.packageBuilder();
            costBuilder.putItem(ResourceType.PEOPLE,BigDecimal.valueOf(20));
            costBuilder.putItem(ResourceType.IRON,BigDecimal.valueOf(500));
            costBuilder.putItem(ResourceType.COPPER_CABLE,BigDecimal.valueOf(200));
            return costBuilder.buildPackage();
        }
        public BaseRecipe getBaseRecipe() {
            PackageBuilder<BaseRecipe> recipeBuilder = BaseRecipe.packageBuilder();
            recipeBuilder.putItem(ResourceType.CARDBOARD,BigDecimal.valueOf(-10));
            recipeBuilder.putItem(ResourceType.POWER,ONE);
            return recipeBuilder.buildPackage();
        }},

    CIRCUIT_MAKER_BASE(7,"CIRCUIT_MAKER"){
        public BaseCost getBaseCost(){
            PackageBuilder<BaseCost> costBuilder = BaseCost.packageBuilder();
            costBuilder.putItem(ResourceType.PEOPLE,BigDecimal.valueOf(250));
            costBuilder.putItem(ResourceType.IRON,BigDecimal.valueOf(1000));
            costBuilder.putItem(ResourceType.COPPER_CABLE,BigDecimal.valueOf(500));
            return costBuilder.buildPackage();
        }
        public BaseRecipe getBaseRecipe() {
            PackageBuilder<BaseRecipe> recipeBuilder = BaseRecipe.packageBuilder();
            recipeBuilder.putItem(ResourceType.POWER,BigDecimal.valueOf(-100));
            recipeBuilder.putItem(ResourceType.COPPER_CABLE,BigDecimal.valueOf(-5));
            recipeBuilder.putItem(ResourceType.CIRCUITS,BigDecimal.valueOf(2));
            return recipeBuilder.buildPackage();
        }},
    ;

    private int id;
    private String nameCode;
    FactoryType(int i,String nameCode){
        id=i;
        this.nameCode = nameCode;
    }

    public abstract BaseCost getBaseCost();
    public abstract BaseRecipe getBaseRecipe();

    public Factory defaultFactory(long userId) {
        return new Factory(userId,this,
                ZERO,
                ONE,ONE,ONE,
                0);
    }

    public String getNameCode(){
        return nameCode;
    }
    public int getId() {
        return id;
    }

    public static FactoryType fromId(int id){
        for (FactoryType f: FactoryType.values()){
            if(f.getId() == id){
                return f;
            }
        }
        return null;
    }

    public static Optional<FactoryType> fromName(String nameCode){
        return Arrays.stream(FactoryType.values()).filter((r) -> r.nameCode.equals(nameCode)).findAny();
    }

}
