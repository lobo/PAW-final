package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MarketDao;
import ar.edu.itba.paw.interfaces.MarketService;
import ar.edu.itba.paw.model.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by juanfra on 17/05/17.
 */
@Service
@Transactional
public class MarketServiceImpl implements MarketService {
    private static final long refreshTime = 2*60*1000;

    @Autowired
    MarketDao marketDao;

    @Scheduled(fixedDelay=refreshTime)
    public void updatePrices(){
        Map<ResourceType,BigDecimal> popularities = marketDao.getPopularities();
        popularities.forEach(ResourceType::setPopularity);
    }
}
