package ar.edu.itba.paw.model;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *  WARNING:
 *  DO NOT modify the variables ids by any means in any way shape or form
 *
 * This test validates that the database ID are being maintained through the releases
 * If a Resource is added there should be established here as RESOURCENAME with the given ID
 * If a Resource Name is changed there should be changed in the static variables as well
 * It is the only way to check that the correlation with the DB is intact
 */
public class ResourceTypeTest {

    public static int CIRCUITS        = 12;   //in DB
    public static int CARDBOARD        = 11;   //in DB
    public static int COPPER_CABLE     = 10;   //in DB
    public static int COPPER           = 9;    //in DB
    public static int METAL_SCRAP      = 8;    //in DB
    public static int RUBBER           = 7;    //in DB
    public static int TIRES            = 6;    //in DB
    public static int IRON             = 5;    //in DB
    public static int PEOPLE           = 4;    //in DB
    public static int MONEY            = 3;    //in DB
    public static int GOLD             = 2;    //in DB
    public static int PLASTIC          = 1;    //in DB
    public static int POWER            = 0;    //in DB

    @Test
    public void noRepeatedIds() throws Exception{
        Set<Integer> ResSet = new HashSet<>();
        Set<Integer> FieldsSet = new HashSet<>();

        for (ResourceType r:ResourceType.values()) {
            int DBid = this.getClass().getField(r.name()).getInt(null);

            assertTrue(ResSet.add(r.getId()));
            assertTrue(FieldsSet.add(DBid));
        }
    }

    @Test
    public void matchIdTest() throws Exception{

        assertEquals(
                ResourceType.values().length,
                this.getClass().getFields().length
                );
        for(ResourceType r: ResourceType.values()){
            int id= r.getId();
            int DBid = this.getClass().getField(r.name()).getInt(null);
            assertEquals(id,DBid);
        }
    }

    @Test
    public void fromId() throws Exception{
        Field[] fields = this.getClass().getFields();

        for(Field f: fields) {
            ResourceType res = ResourceType.fromId(f.getInt(null));
            assertEquals(res.name(),f.getName());
        }
    }
}
