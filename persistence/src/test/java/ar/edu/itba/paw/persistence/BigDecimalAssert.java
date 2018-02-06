package ar.edu.itba.paw.persistence;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertTrue;

public class BigDecimalAssert {
    static public void assertBigDecimalEquals(BigDecimal d1, BigDecimal d2, double precision) {
        assertTrue(d1.subtract(d2).doubleValue()<=precision);
    }
}
