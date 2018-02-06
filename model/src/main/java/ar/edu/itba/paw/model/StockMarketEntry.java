package ar.edu.itba.paw.model;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "stockmarket")
@Entity
public class StockMarketEntry {
    @Transient
    private ResourceType resourceType;

    @Id
    @Column(name = "resourcetype")
    private int _resourcetype;

    private BigDecimal amount;

    @PostLoad
    private void postLoad() {
        resourceType = ResourceType.fromId(_resourcetype);
    }

    public StockMarketEntry(){}

    public StockMarketEntry(@NotNull ResourceType resourceType, BigDecimal amount) {
        this.resourceType = resourceType;
        this.amount = amount;
        _resourcetype = resourceType.getId();
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
