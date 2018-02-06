package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.Exception.ValidatorException;
import ar.edu.itba.paw.model.packages.Creator;
import ar.edu.itba.paw.model.packages.PackageBuilder;
import ar.edu.itba.paw.model.packages.ResourcePackage;
import ar.edu.itba.paw.model.packages.Validator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.management.openmbean.KeyAlreadyExistsException;

import java.math.BigDecimal;

import static ar.edu.itba.paw.model.BigDecimalAssert.assertBigDecimalEquals;
import static org.junit.Assert.assertEquals;

public class PackageBuilderTest {

    private Validator<BigDecimal> VALIDATOR = (d) -> d.compareTo(BigDecimal.valueOf(5))>=0 && d.compareTo(BigDecimal.valueOf(15))<0;
    private Creator<TestPackage> CREATOR = TestPackage::new;

    private static ResourceType people = ResourceType.PEOPLE;
    private static ResourceType cardboard = ResourceType.CARDBOARD;
    private PackageBuilder<TestPackage> pb;


    private class TestPackage extends ResourcePackage {
        TestPackage(PackageBuilder<TestPackage> pb) {
            resources = generate(pb.getResources(),VALIDATOR);
        }
    }

    @Before
    public void setUp() {
        pb = new PackageBuilder<>(VALIDATOR,CREATOR);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test(expected = ValidatorException.class)
    public void testValidatorPutLess() {
        pb.putItem(people, BigDecimal.valueOf(2D));
    }

    @Test
    public void testValidatorAddMore() {
        pb.putItem(people, BigDecimal.valueOf(5D));
        thrown.expect(ValidatorException.class);
        pb.addItem(people,  BigDecimal.valueOf(10));
    }

    @Test
    public void testValidatorAddLess() {
        pb.putItem(people,  BigDecimal.valueOf(5));
        thrown.expect(ValidatorException.class);
        pb.addItem(people,  BigDecimal.valueOf(-10));
    }

    @Test
    public void testCreator() {
        pb.putItem(people, BigDecimal.valueOf(5));
        pb.addItem(people, BigDecimal.valueOf(3));

        TestPackage testPackage = pb.buildPackage();

        assertEquals(testPackage.getResources().size(),1);
        testPackage.rawMap().forEach(
                (r,d) -> {
                    assertEquals(r,people);
                    assertBigDecimalEquals(d,BigDecimal.valueOf(8),0);
                }
        );
    }

    @Test
    public void testRepeatedResources(){

        pb.putItem(people,BigDecimal.valueOf(5));
        pb.putItem(cardboard,BigDecimal.valueOf(6));
        thrown.expect(KeyAlreadyExistsException.class);
        pb.putItem(cardboard,BigDecimal.valueOf(6));
    }

    @Test
    public void testMultipleResources(){

        Double delta = 0D;

        pb.putItem(people,BigDecimal.valueOf(5));
        pb.putItem(cardboard,BigDecimal.valueOf(6));
        pb.addItem(people,BigDecimal.valueOf(2.5D));
        pb.addItem(cardboard,BigDecimal.valueOf(3.6D));

        TestPackage testPackage = pb.buildPackage();
        assertEquals(testPackage.getResources().size(),2);
        assertBigDecimalEquals(testPackage.getValue(people),BigDecimal.valueOf(7.5),delta);
        assertBigDecimalEquals(testPackage.getValue(cardboard),BigDecimal.valueOf(9.6),delta);
    }

}
