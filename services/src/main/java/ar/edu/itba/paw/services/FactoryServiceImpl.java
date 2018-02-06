package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.FactoryService;
import ar.edu.itba.paw.model.FactoryType;
import ar.edu.itba.paw.model.Upgrade;
import org.springframework.stereotype.Service;

@Service
public class FactoryServiceImpl implements FactoryService {

    public Upgrade getUpgrade(FactoryType type, int level){
        return Upgrade.getUpgrade(type,level);
    }
    public FactoryType getFactoryById(int id) {
        return FactoryType.fromId(id);
    }
}
