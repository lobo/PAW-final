package ar.edu.itba.paw.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Optional;


public enum ResourceType {
    CIRCUITS(12, "CIRCUITS", 50),
    CARDBOARD(11, "CARDBOARD", 1),
    COPPER_CABLE(10, "COPPER_CABLE", 5),
    COPPER(9, "COPPER", 1),
    METAL_SCRAP(8, "METAL_SCRAP", 11),
    RUBBER(7, "RUBBER", 4),
    TIRES(6, "TIRES", 8),
    IRON(5, "IRON", 5),
    PEOPLE(4, "PEOPLE", 9),
    MONEY(3, "MONEY", 10),
    GOLD(2, "GOLD", 20),
    PLASTIC(1, "PLASTIC", 40),
    POWER(0, "POWER", 80);

    private int id;
    private String nameCode;
    private BigDecimal price;
    private BigDecimal popularity;

    ResourceType(int id, String name, double price) {
        this.id = id;
        this.nameCode = name;
        this.price = BigDecimal.valueOf(price);
        this.popularity = BigDecimal.ONE;
    }

    public void setPopularity(BigDecimal popularity) {
        this.popularity = popularity;
    }

    public BigDecimal getPrice() {
        return popularity.multiply(price);
    }

    public BigDecimal getBasePrice() {
        return price.setScale(0, RoundingMode.CEILING);
    }

    public String getNameCode() {
        return nameCode;
    }

    public int getId() {
        return id;
    }

    public static BigDecimal initialMoney() {
        return BigDecimal.valueOf(13000);
    }

    public static ResourceType fromId(int id){
        return Arrays.stream(ResourceType.values()).filter((r) -> r.getId() ==id).findAny().orElse(null);
    }
    public static Optional<ResourceType> fromName(String nameCode){
        return Arrays.stream(ResourceType.values()).filter((r) -> r.nameCode.equals(nameCode)).findAny();
    }
}
