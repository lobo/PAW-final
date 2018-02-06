package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.FactoryType;
import ar.edu.itba.paw.model.Upgrade;

public interface FactoryService {
    Upgrade getUpgrade(FactoryType type,int level);
    FactoryType getFactoryById(int id);
}
