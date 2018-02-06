package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.MarketDao;
import ar.edu.itba.paw.model.ResourceType;
import ar.edu.itba.paw.model.StockMarketEntry;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

/**
 * Created by juanfra on 17/06/17.
 */
@Repository
public class MarketHibernateDao implements MarketDao {

    @PersistenceContext
    private EntityManager em;

    private StockMarketEntry findByResource(ResourceType type){
        final TypedQuery<StockMarketEntry> query = em.createQuery( "from StockMarketEntry as s where s._resourcetype = :resource" ,
                StockMarketEntry.class);
        query.setParameter("resource",type.getId());
        List<StockMarketEntry> list = query.getResultList();
        return list.isEmpty()?null:list.get(0);    }

    @Override
    public boolean registerPurchase(StockMarketEntry stockMarketEntry) {
        StockMarketEntry sme = findByResource(stockMarketEntry.getResourceType());
        if(sme == null){
            em.persist(stockMarketEntry);
        } else {
            sme.setAmount(sme.getAmount().add(stockMarketEntry.getAmount()));
            em.merge(sme);
        }
        return true;
    }

    @Override
    public Map<ResourceType, BigDecimal> getPopularities() {
        final TypedQuery<StockMarketEntry> query = em.createQuery( "from StockMarketEntry as s" ,
                StockMarketEntry.class);

        List<StockMarketEntry> entries = query.getResultList();
        Map<ResourceType, BigDecimal> popularities = new HashMap<>();
        if(entries.isEmpty()) return Collections.emptyMap();

        BigDecimal minRes = entries.stream()
                .map(StockMarketEntry::getAmount)
                .reduce(BigDecimal::min)
                .orElse(null);

        if(minRes==null)
            //TODO log this
            return Collections.EMPTY_MAP;

        BigDecimal min = minRes.compareTo(BigDecimal.ZERO)<0?minRes.negate():ONE;

        BigDecimal total = ZERO;
        for(ResourceType r: ResourceType.values()){
            total = total.add(min);
        }

        total = total.add(min);

        BigDecimal finalTotal = total;
        entries.forEach( (e) -> {
                    BigDecimal value = e.getAmount().add(min);
                    BigDecimal popularity = popularityCalculator(
                                    value,
                                    BigDecimal.valueOf(ResourceType.values().length),
                                    finalTotal
                    );

                    if(popularity.compareTo(BigDecimal.valueOf(0.5))<0) popularity=BigDecimal.valueOf(0.5);
                    popularities.put(e.getResourceType(),popularity);
                }
        );
        Stream.of(ResourceType.values()).filter((r)->!popularities.containsKey(r)).forEach((r)->popularities.put(r,min));
        return popularities;
    }

    public BigDecimal popularityCalculator(BigDecimal purchases, BigDecimal totalAmount, BigDecimal totalSum) {
        return popularityExponential(purchases.multiply(totalAmount).divide(totalSum, RoundingMode.HALF_EVEN).doubleValue());
    }

    private BigDecimal popularityExponential(double x) {
        double slope = 0.15;
        BigDecimal max = BigDecimal.valueOf(3);
        return BigDecimal.valueOf( (1+1/max.doubleValue()) - Math.exp(-(x-1) * slope) ).multiply(max);
    }

}
