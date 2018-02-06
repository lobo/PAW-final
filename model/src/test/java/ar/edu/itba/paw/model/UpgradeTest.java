package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.packages.UpgradeType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Julian Benitez on 5/17/2017.
 */
public class UpgradeTest {

    private static FactoryType noInputFactory = FactoryType.STOCK_INVESTMENT_BASE;
    private static FactoryType inputFactory = FactoryType.CIRCUIT_MAKER_BASE;

    private static int firstLevel = 1;
    private static int secondLevel = 2;
    private static int thirdLevel = 3;

    private static int noInputLoop = 2;
    private static int inputLoop = 3;


    @Test
    public void firstUpgradeNotInputs() {
        Upgrade upgrade = Upgrade.getUpgrade(noInputFactory,firstLevel);
        assertEquals(upgrade.getType(), UpgradeType.OUTPUT_INCREASE);
    }

    @Test
    public void secondUpgradeNotInputs() {
        Upgrade upgrade = Upgrade.getUpgrade(noInputFactory,secondLevel);
        assertEquals(upgrade.getType(), UpgradeType.COST_REDUCTION);
    }

    @Test
    public void thirdUpgradeNotInputs() {
        Upgrade upgrade = Upgrade.getUpgrade(noInputFactory,thirdLevel);
        assertEquals(upgrade.getType(), UpgradeType.OUTPUT_INCREASE);
    }

    @Test
    public void firstUpgradeInputs() {
        Upgrade upgrade = Upgrade.getUpgrade(inputFactory,firstLevel);
        assertEquals(upgrade.getType(), UpgradeType.INPUT_REDUCTION);
    }

    @Test
    public void secondUpgradeInputs() {
        Upgrade upgrade = Upgrade.getUpgrade(inputFactory,secondLevel);
        assertEquals(upgrade.getType(), UpgradeType.OUTPUT_INCREASE);
    }

    @Test
    public void thirdUpgradeInputs() {
        Upgrade upgrade = Upgrade.getUpgrade(inputFactory,thirdLevel);
        assertEquals(upgrade.getType(), UpgradeType.COST_REDUCTION);
    }

    @Test
    public void veryLargeUpgrade() {
        int veryLargeLevel = 1000;
        Upgrade upgrade = Upgrade.getUpgrade(inputFactory,veryLargeLevel);
        assertNotEquals(upgrade.getType(), UpgradeType.INPUT_REDUCTION);
        Upgrade upgrade2 = Upgrade.getUpgrade(inputFactory,veryLargeLevel + 1);
        assertNotEquals(upgrade2.getType(), UpgradeType.INPUT_REDUCTION);
        Upgrade upgrade3 = Upgrade.getUpgrade(inputFactory,veryLargeLevel + 2);
        assertNotEquals(upgrade3.getType(), UpgradeType.INPUT_REDUCTION);

    }
    
    @Test
    public void noInputLoop() {
        int level = 10;
        Upgrade first = Upgrade.getUpgrade(noInputFactory,level);
        Upgrade second = Upgrade.getUpgrade(noInputFactory,level + noInputLoop);
        assertEquals(first.getType(),second.getType());

    }

    @Test
    public void inputLoop() {
        int level = 10;
        Upgrade first = Upgrade.getUpgrade(inputFactory,level);
        Upgrade second = Upgrade.getUpgrade(inputFactory,level + inputLoop);
        assertEquals(first.getType(),second.getType());

    }
}
