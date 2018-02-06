package ar.edu.itba.paw.model.packages;

import ar.edu.itba.paw.model.ResourceType;
import ar.edu.itba.paw.model.Exception.ValidatorException;
import ar.edu.itba.paw.model.packages.Implementations.Storage;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by juanfra on 08/04/17.
 */
public class PackageBuilder<T extends ResourcePackage> {

    private Map<ResourceType, BigDecimal> resources;
    private Map<ResourceType, Calendar> lastUpdated;
    private Validator<BigDecimal> validator;
    private Creator<T> creator;

    public PackageBuilder(Validator<BigDecimal> validator, Creator<T> creator) {
        resources = new HashMap<>();
        lastUpdated = new HashMap<>();
        this.validator = validator;
        this.creator = creator;
    }

    public PackageBuilder<T> putItem(ResourceType resource, BigDecimal amount){
        if(!validator.validates(amount)) {
            throw new ValidatorException("Validator invalid " + amount);
        }else if(resources.containsKey(resource)) {
            throw new KeyAlreadyExistsException("Key already put " + resource);
        } else {
            resources.put(resource,amount);
            return this;
        }
    }

    public PackageBuilder<T> addItem(ResourceType resource, BigDecimal amount) {
        BigDecimal toAdd;
        if(resources.containsKey(resource)) {
            toAdd = resources.get(resource).add(amount);
        } else {
            toAdd = amount;
        }

        if(!validator.validates(toAdd)) {
            throw new ValidatorException("Validator invalid " + amount);
        }else {
            resources.put(resource, toAdd);
            return this;
        }
    }

    public PackageBuilder<T> putItems(ResourcePackage resources) {
        resources.rawMap().forEach(this::putItem);
        return this;
    }

    public PackageBuilder<T> setDates(Storage storage) {
        storage.getLastUpdated().forEach(this::setLastUpdated);
        return this;
    }

    public PackageBuilder<T> addItems(ResourcePackage resources) {
        resources.rawMap().forEach(this::addItem);
        return this;
    }

    public PackageBuilder<T> setLastUpdated(ResourceType resource, Calendar time){
        if(!lastUpdated.containsKey(resource) && resources.containsKey(resource)) {
            lastUpdated.put(resource,time);
            return this;
        } else {
            throw new RuntimeException("Resource not set yet");
        }
    }

    public PackageBuilder<T> putItemWithDate(ResourceType resource, BigDecimal amount, Calendar time){
        return this.putItem(resource,amount).setLastUpdated(resource,time);
    }

    public Map<ResourceType, BigDecimal> getResources() {
        return resources;
    }

    public T buildPackage(){
        return creator.create(this);
    }

    public Map<ResourceType,Calendar> lastUpdated() {
        return lastUpdated;
    }
}
