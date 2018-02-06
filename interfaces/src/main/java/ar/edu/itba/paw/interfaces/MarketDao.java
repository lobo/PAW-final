package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.ResourceType;
import ar.edu.itba.paw.model.StockMarketEntry;

import java.math.BigDecimal;
import java.util.Map;

public interface MarketDao {

    boolean registerPurchase(StockMarketEntry stockMarketEntry);
    Map<ResourceType,BigDecimal> getPopularities();
}
