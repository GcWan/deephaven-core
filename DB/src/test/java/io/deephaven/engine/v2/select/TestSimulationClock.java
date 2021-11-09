package io.deephaven.engine.v2.select;

import io.deephaven.engine.tables.live.UpdateGraphProcessor;
import io.deephaven.engine.tables.utils.DateTime;
import io.deephaven.engine.tables.utils.DateTimeUtils;
import io.deephaven.engine.v2.RefreshingTableTestCase;

/**
 * Quick unit test for {@link SimulationClock}.
 */
public class TestSimulationClock extends RefreshingTableTestCase {

    public void testSignal() {
        final DateTime start = DateTime.now();
        final SimulationClock clock = new SimulationClock(start, DateTimeUtils.plus(start, 1), 1);
        clock.start();
        for (int ci = 0; ci < 2; ++ci) {
            UpdateGraphProcessor.DEFAULT.runWithinUnitTestCycle(clock::advance);
        }
        clock.awaitDoneUninterruptibly();
    }
}
