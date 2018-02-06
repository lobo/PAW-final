package ar.edu.itba.paw.model;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * WARNING:
 * DO NOT modify the variables ids by any means in any way shape or form
 *
 * This test validates that the database ID are being maintained through the releases
 * If a FactoryType is added there should be established here as FACTORYNAME with the given ID
 * If a FactoryType Name is changed there should be changed in the static variables as well
 * It is the only way to check that the correlation with the DB is intact
 */
public class FactoryTypeTest {
    public static int CIRCUIT_MAKER_BASE        = 7;    //in DB
    public static int BOILER_BASE               = 6;    //in DB
    public static int CABLE_MAKER_BASE          = 5;    //in DB
    public static int RUBBER_SHREDDER_BASE      = 4;    //in DB
    public static int METAL_SEPARATOR_BASE      = 3;    //in DB
    public static int JUNK_COLLECTOR_BASE       = 2;    //in DB
    public static int PEOPLE_RECRUITING_BASE    = 1;    //in DB
    public static int STOCK_INVESTMENT_BASE     = 0;    //in DB

    @Test
    public void noRepeatedIds() throws Exception{
        Set<Integer> ResSet = new HashSet<>();
        Set<Integer> FieldsSet = new HashSet<>();

        for (FactoryType f:FactoryType.values()) {
            int DBid = this.getClass().getField(f.name()).getInt(null);

            assertTrue(ResSet.add(f.getId()));
            assertTrue(FieldsSet.add(DBid));
        }
    }

    @Test
    public void matchIdTest() throws Exception{

        assertEquals(
                FactoryType.values().length,
                this.getClass().getFields().length
        );
        for(FactoryType f: FactoryType.values()){
            int id= f.getId();
            int DBid = this.getClass().getField(f.name()).getInt(null);
            assertEquals(id,DBid);
        }
    }

    @Test
    public void fromId() throws Exception{
        Field[] fields = this.getClass().getFields();

        for(Field f: fields) {
            FactoryType fac = FactoryType.fromId(f.getInt(null));
            assertNotNull(fac);
            assertEquals(fac.name(),f.getName());
        }
    }
}
