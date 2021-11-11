package io.deephaven.engine.v2.utils;

import io.deephaven.util.datastructures.LongRangeIterator;
import io.deephaven.engine.v2.utils.rsp.RspBitmap;
import io.deephaven.engine.v2.utils.singlerange.SingleRange;
import io.deephaven.engine.v2.utils.sortedranges.SortedRanges;

import java.util.PrimitiveIterator;
import java.util.function.LongConsumer;

abstract class AbstractOrderedLongSetBuilderRandom implements OrderedLongSet.BuilderRandom {

    protected static final IndexCounts indexCounts = new IndexCounts("randomIndexBuilder");

    protected SortedRanges pendingSr = null;
    private long pendingRangeStart = -1;
    private long pendingRangeEnd = -1;

    protected abstract OrderedLongSet.BuilderRandom innerBuilder();

    protected abstract void setupInnerBuilderForRange(final long start, final long end);

    protected abstract void setupInnerBuilderEmpty();

    protected abstract void setInnerBuilderNull();

    protected boolean flushPendingRange() {
        if (pendingRangeStart == -1) {
            return false;
        }
        if (innerBuilder() != null) {
            innerBuilder().addRange(pendingRangeStart, pendingRangeEnd);
        } else {
            if (pendingSr == null) {
                pendingSr = SortedRanges.makeSingleRange(pendingRangeStart, pendingRangeEnd);
            } else {
                return tryFlushToPendingSr();
            }
        }
        pendingRangeStart = pendingRangeEnd = -1;
        return true;
    }

    private boolean tryFlushToPendingSr() {
        final SortedRanges ans = pendingSr.addRangeUnsafe(pendingRangeStart, pendingRangeEnd);
        if (ans != null) {
            pendingSr = ans;
            pendingRangeStart = pendingRangeEnd = -1;
            return true;
        }
        flushPendingSrToInnerBuilder();
        innerBuilder().addRange(pendingRangeStart, pendingRangeEnd);
        pendingRangeStart = pendingRangeEnd = -1;
        return false;
    }

    protected void flushPendingSrToInnerBuilder() {
        flushSrToInnerBuilder(pendingSr);
        pendingSr = null;
    }

    private void flushSrToInnerBuilder(final SortedRanges sr) {
        setupInnerBuilderEmpty();
        sr.forEachLongRange((final long start, final long end) -> {
            innerBuilder().appendRange(start, end);
            return true;
        });
    }

    private boolean tryMergeToPendingRange(final long firstKey, final long lastKey) {
        if (pendingRangeStart == -1) {
            pendingRangeStart = firstKey;
            pendingRangeEnd = lastKey;
            return true;
        }
        if (pendingRangeEnd < firstKey - 1 || lastKey < pendingRangeStart - 1) {
            return false;
        }
        pendingRangeStart = Math.min(pendingRangeStart, firstKey);
        pendingRangeEnd = Math.max(pendingRangeEnd, lastKey);
        return true;
    }

    private void newKey(final long key) {
        newRangeSafe(key, key);
    }

    private void newRange(final long firstKey, final long lastKey) {
        if (firstKey > lastKey) {
            throw new IllegalArgumentException("Illegal range start=" + firstKey + " > end=" + lastKey + ".");
        }
        newRangeSafe(firstKey, lastKey);
    }

    private void newRangeSafe(final long firstKey, final long lastKey) {
        if (tryMergeToPendingRange(firstKey, lastKey)) {
            return;
        }
        flushPendingRange();
        pendingRangeStart = firstKey;
        pendingRangeEnd = lastKey;
    }

    @Override
    public OrderedLongSet getTreeIndexImpl() {
        final OrderedLongSet ans;
        if (innerBuilder() == null && pendingSr == null) {
            if (pendingRangeStart == -1) {
                indexCounts.emptyCount.sample(1);
                ans = OrderedLongSet.EMPTY;
            } else {
                final SingleRange sr = SingleRange.make(pendingRangeStart, pendingRangeEnd);
                indexCounts.sampleSingleRange(sr);
                ans = sr;
                pendingRangeStart = -1;
            }
        } else {
            flushPendingRange();
            if (innerBuilder() == null) {
                pendingSr = pendingSr.tryCompactUnsafe(4);
                indexCounts.sampleSortedRanges(pendingSr);
                ans = pendingSr;
                pendingSr = null;
            } else {
                // No counts since they are gathered by inner builder.
                ans = innerBuilder().getTreeIndexImpl();
                setInnerBuilderNull();
            }
        }
        return ans;
    }

    @Override
    public void addKey(final long rowKey) {
        newKey(rowKey);
    }

    public void addKeys(final PrimitiveIterator.OfLong it) {
        final LongConsumer c = this::addKey;
        it.forEachRemaining(c);
    }

    @Override
    public void addRange(long firstRowKey, long lastRowKey) {
        newRange(firstRowKey, lastRowKey);
    }

    public void addRanges(final LongRangeIterator it) {
        it.forEachLongRange((final long start, final long end) -> {
            addRange(start, end);
            return true;
        });
    }

    public void addRowSet(final RowSet rowSet) {
        flushPendingRange();
        if (rowSet instanceof MutableRowSetImpl) {
            MutableRowSetImpl.addToBuilderFromImpl(this, (MutableRowSetImpl) rowSet);
            return;
        }
        rowSet.forEachRowKeyRange((final long start, final long end) -> {
            addRange(start, end);
            return true;
        });
    }

    @Override
    public void add(final SortedRanges other, final boolean acquire) {
        flushPendingRange();
        if (pendingSr != null) {
            final OrderedLongSet tix = pendingSr.insertImpl(other, false);
            if (tix instanceof SortedRanges) {
                pendingSr = (SortedRanges) tix;
                return;
            }
            pendingSr = null;
            if (innerBuilder() == null) {
                setupInnerBuilderEmpty();
            }
            innerBuilder().add((RspBitmap) tix, true);
            return;
        }
        if (innerBuilder() == null) {
            setupInnerBuilderEmpty();
        }
        innerBuilder().add(other, acquire);
    }

    @Override
    public void add(final RspBitmap other, final boolean acquire) {
        flushPendingRange();
        if (pendingSr == null && innerBuilder() == null) {
            setupInnerBuilderEmpty();
            innerBuilder().add(other, acquire);
            return;
        }
        if (pendingSr != null) {
            flushPendingSrToInnerBuilder();
        }
        innerBuilder().add(other, acquire);
    }
}
