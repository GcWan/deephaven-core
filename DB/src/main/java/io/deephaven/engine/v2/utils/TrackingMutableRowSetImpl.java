/*
 * Copyright (c) 2016-2021 Deephaven Data Labs and Patent Pending
 */

package io.deephaven.engine.v2.utils;

import io.deephaven.engine.v2.sources.LogicalClock;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInput;

public class TrackingMutableRowSetImpl extends GroupingRowSetHelper {

    private transient OrderedLongSet prevInnerSet;
    /**
     * Protects prevImpl. Only updated in checkPrev() and initializePreviousValue() (this later supposed to be used only
     * right after the constructor, in special cases).
     */
    private transient volatile long changeTimeStep;


    public TrackingMutableRowSetImpl() {
        this(OrderedLongSet.EMPTY);
    }

    public TrackingMutableRowSetImpl(final OrderedLongSet impl) {
        super(impl);
        this.prevInnerSet = OrderedLongSet.EMPTY;
        changeTimeStep = -1;
    }

    @Override
    public void preMutationHook() {
        checkAndGetPrev();
    }

    private OrderedLongSet checkAndGetPrev() {
        if (LogicalClock.DEFAULT.currentStep() == changeTimeStep) {
            return prevInnerSet;
        }
        synchronized (this) {
            final long currentClockStep = LogicalClock.DEFAULT.currentStep();
            if (currentClockStep == changeTimeStep) {
                return prevInnerSet;
            }
            prevInnerSet.ixRelease();
            prevInnerSet = getInnerSet().ixCowRef();
            changeTimeStep = currentClockStep;
            return prevInnerSet;
        }
    }

    @Override
    public TrackingMutableRowSet convertToTracking() {
        throw new UnsupportedOperationException("Already tracking! You must clone() before convertToTracking()");
    }

    @Override
    public void close() {
        prevInnerSet.ixRelease();
        prevInnerSet = null; // Force NPE on use after tracking
        changeTimeStep = -1;
        super.close();
    }

    @Override
    public void initializePreviousValue() {
        prevInnerSet.ixRelease();
        prevInnerSet = OrderedLongSet.EMPTY;
        changeTimeStep = -1;
    }

    @Override
    public long sizePrev() {
        return checkAndGetPrev().ixCardinality();
    }

    @Override
    public MutableRowSet getPrevRowSet() {
        return new MutableRowSetImpl(checkAndGetPrev().ixCowRef());
    }

    @Override
    public long getPrev(final long pos) {
        if (pos < 0) {
            return -1;
        }
        return checkAndGetPrev().ixGet(pos);
    }

    @Override
    public long findPrev(long rowKey) {
        return checkAndGetPrev().ixFind(rowKey);
    }

    @Override
    public long firstRowKeyPrev() {
        return checkAndGetPrev().ixFirstKey();
    }

    @Override
    public long lastRowKeyPrev() {
        return checkAndGetPrev().ixLastKey();
    }

    @Override
    public void readExternal(@NotNull final ObjectInput in) throws IOException {
        super.readExternal(in);
        initializePreviousValue();
    }
}
