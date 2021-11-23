/*
 * Copyright (c) 2016-2021 Deephaven Data Labs and Patent Pending
 */

package io.deephaven.engine.time.calendar;

import io.deephaven.engine.time.DateTime;
import io.deephaven.engine.time.DateTimeUtil;

import java.io.Serializable;

/**
 * A period of business time during a business day.
 */
public class BusinessPeriod implements Serializable {
    private static final long serialVersionUID = 8196837269495115196L;
    private final DateTime startTime;
    private final DateTime endTime;

    @SuppressWarnings("ConstantConditions")
    BusinessPeriod(final DateTime startTime, final DateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;

        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Null argument: startTime=" + startTime + " endTime=" + endTime);
        }

        if (startTime.getNanos() > endTime.getNanos()) {
            throw new IllegalArgumentException("Start is after end: startTime=" + startTime + " endTime=" + endTime);
        }
    }

    /**
     * Returns the start of the period.
     *
     * @return the start of the period
     */
    public DateTime getStartTime() {
        return startTime;
    }

    /**
     * Returns the end of the period.
     *
     * @return the end of the period
     */
    public DateTime getEndTime() {
        return endTime;
    }

    /**
     * Returns the length of the period in nanoseconds.
     *
     * @return length of the period in nanoseconds
     */
    public long getLength() {
        return DateTimeUtil.minus(endTime, startTime);
    }

    /**
     * Determines if the specified time is within the business period.
     *
     * @param time time.
     * @return true if the time is in this period; otherwise, false.
     */
    public boolean contains(final DateTime time) {
        return time != null && (startTime.getNanos() <= time.getNanos() && time.getNanos() <= endTime.getNanos());
    }
}
