package io.deephaven.engine.time.calendar;

import io.deephaven.base.testing.BaseArrayTestCase;
import io.deephaven.engine.time.DateTime;
import io.deephaven.engine.time.DateTimeUtil;
import junit.framework.TestCase;

public class TestBusinessPeriod extends BaseArrayTestCase {

    public void testBusinessPeriod() {
        final DateTime open1 = DateTimeUtil.convertDateTime("2017-03-11T10:00:00.000000000 NY");
        final DateTime close1 = DateTimeUtil.convertDateTime("2017-03-11T11:00:00.000000000 NY");

        try {
            new BusinessPeriod(null, close1);
            TestCase.fail("Expected an exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("null"));
        }

        try {
            new BusinessPeriod(close1, null);
            TestCase.fail("Expected an exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("null"));
        }

        try {
            new BusinessPeriod(close1, open1);
            TestCase.fail("Expected an exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("after"));
        }

        BusinessPeriod period = new BusinessPeriod(open1, close1);
        assertEquals(open1, period.getStartTime());
        assertEquals(close1, period.getEndTime());
        assertEquals(DateTimeUtil.HOUR, period.getLength());

        assertTrue(period.contains(open1));
        assertTrue(period.contains(new DateTime(open1.getNanos() + DateTimeUtil.MINUTE)));
        assertFalse(period.contains(new DateTime(open1.getNanos() - DateTimeUtil.MINUTE)));
        assertTrue(period.contains(close1));
        assertTrue(period.contains(new DateTime(close1.getNanos() - DateTimeUtil.MINUTE)));
        assertFalse(period.contains(new DateTime(close1.getNanos() + DateTimeUtil.MINUTE)));
    }
}
