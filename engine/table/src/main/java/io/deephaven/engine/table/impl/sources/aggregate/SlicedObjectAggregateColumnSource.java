/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.engine.table.impl.sources.aggregate;

import io.deephaven.base.ClampUtil;
import io.deephaven.chunk.LongChunk;
import io.deephaven.chunk.ObjectChunk;
import io.deephaven.chunk.WritableChunk;
import io.deephaven.chunk.WritableObjectChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.RowSequence;
import io.deephaven.engine.rowset.RowSet;
import io.deephaven.engine.rowset.chunkattributes.OrderedRowKeys;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.impl.vector.ObjectVectorColumnWrapper;
import io.deephaven.vector.ObjectVector;
import io.deephaven.vector.ObjectVectorDirect;
import org.jetbrains.annotations.NotNull;

import static io.deephaven.util.QueryConstants.NULL_LONG;

/**
 * {@link ColumnSource} implementation for aggregation result short columns.
 */
public final class SlicedObjectAggregateColumnSource<COMPONENT_TYPE>
        extends BaseAggregateSlicedColumnSource<ObjectVector<COMPONENT_TYPE>, COMPONENT_TYPE> {

    public SlicedObjectAggregateColumnSource(
            @NotNull final ColumnSource<COMPONENT_TYPE> aggregatedSource,
            @NotNull final ColumnSource<? extends RowSet> groupRowSetSource,
            @NotNull final ColumnSource<Long> startSource,
            @NotNull final ColumnSource<Long> endSource) {
        // noinspection unchecked,rawtypes
        super((Class<ObjectVector<COMPONENT_TYPE>>) (Class) ObjectVector.class,
                aggregatedSource, groupRowSetSource, startSource, endSource);
    }

    public SlicedObjectAggregateColumnSource(
            @NotNull final ColumnSource<COMPONENT_TYPE> aggregatedSource,
            @NotNull final ColumnSource<? extends RowSet> groupRowSetSource,
            final long startOffset,
            final long endOffset) {
        // noinspection unchecked,rawtypes
        super((Class<ObjectVector<COMPONENT_TYPE>>) (Class) ObjectVector.class,
                aggregatedSource, groupRowSetSource, startOffset, endOffset);
    }

    private ObjectVector<COMPONENT_TYPE> makeVector(final RowSet rowSetSlice) {
        return rowSetSlice.isEmpty()
                ? ObjectVectorDirect.empty()
                : new ObjectVectorColumnWrapper<>(aggregatedSource, rowSetSlice);
    }

    private ObjectVector<COMPONENT_TYPE> makePrevVector(final RowSet rowSetSlice) {
        return rowSetSlice.isEmpty()
                ? ObjectVectorDirect.empty()
                : new ObjectVectorColumnWrapper<>(aggregatedSourcePrev, rowSetSlice);
    }

    @Override
    public ObjectVector<COMPONENT_TYPE> get(final long rowKey) {
        if (rowKey == RowSequence.NULL_ROW_KEY) {
            return null;
        }

        final long startPos = startSource != null ? startSource.getLong(rowKey) : startOffset;
        final long endPos = endSource != null ? endSource.getLong(rowKey) : endOffset;

        if (startPos == NULL_LONG || endPos == NULL_LONG) {
            return null;
        }

        final RowSet bucketRowSet = groupRowSetSource.get(rowKey);
        final long rowPos = bucketRowSet.find(rowKey);

        final long size = bucketRowSet.size();
        final long start = ClampUtil.clampLong(0, size, rowPos + startPos);
        final long end = ClampUtil.clampLong(0, size, rowPos + endPos);

        // Determine the slice of the groupRowSetSource from start to end.
        final RowSet rowSetSlice = bucketRowSet.subSetByPositionRange(start, end);
        return makeVector(rowSetSlice);
    }

    @Override
    public ObjectVector<COMPONENT_TYPE> getPrev(final long rowKey) {
        if (rowKey == RowSequence.NULL_ROW_KEY) {
            return null;
        }

        final long startPos = startSource != null ? startSource.getPrevLong(rowKey) : startOffset;
        final long endPos = endSource != null ? endSource.getPrevLong(rowKey) : endOffset;

        if (startPos == NULL_LONG || endPos == NULL_LONG) {
            return null;
        }

        final RowSet bucketRowSet = getPrevGroupRowSet(rowKey);
        final long rowPos = bucketRowSet.find(rowKey);

        final long size = bucketRowSet.size();
        final long start = ClampUtil.clampLong(0, size, rowPos + startPos);
        final long end = ClampUtil.clampLong(0, size, rowPos + endPos);

        // Determine the slice of the groupRowSetSource from start to end.
        final RowSet rowSetSlice = bucketRowSet.subSetByPositionRange(start, end);
        return makePrevVector(rowSetSlice);
    }

    @Override
    public void fillChunk(@NotNull final FillContext context, @NotNull final WritableChunk<? super Values> destination,
            @NotNull final RowSequence rowSequence) {
        AggregateSlicedFillContext ctx = (AggregateSlicedFillContext) context;

        final LongChunk<OrderedRowKeys> keyChunk = rowSequence.asRowKeyChunk();
        final ObjectChunk<RowSet, ? extends Values> groupRowSetChunk = groupRowSetSource
                .getChunk(ctx.groupRowSetGetContext, rowSequence).asObjectChunk();
        final LongChunk<? extends Values> startChunk = startSource != null
                ? startSource.getChunk(ctx.startGetContext, rowSequence).asLongChunk()
                : null;
        final LongChunk<? extends Values> endChunk = endSource != null
                ? endSource.getChunk(ctx.endGetContext, rowSequence).asLongChunk()
                : null;

        final WritableObjectChunk<ObjectVector<COMPONENT_TYPE>, ? super Values> typedDestination =
                destination.asWritableObjectChunk();
        final int size = rowSequence.intSize();
        for (int di = 0; di < size; ++di) {
            final long startPos = startChunk != null ? startChunk.get(di) : startOffset;
            final long endPos = endChunk != null ? endChunk.get(di) : endOffset;

            if (startPos == NULL_LONG && endPos == NULL_LONG) {
                // null when both start/end are null.
                typedDestination.set(di, null);
            } else if (startPos == NULL_LONG) {
                // empty vector when only start is null
                typedDestination.set(di, ObjectVectorDirect.empty());
            } else {
                final long rowKey = keyChunk.get(di);
                final RowSet bucketRowSet = groupRowSetChunk.get(di);
                final long rowPos = bucketRowSet.find(rowKey);

                final long rowSetSize = bucketRowSet.size();
                final long start = ClampUtil.clampLong(0, rowSetSize, rowPos + startPos);
                final long end = ClampUtil.clampLong(0, rowSetSize, rowPos + endPos);

                // Determine the slice of the groupRowSetSource from start to end.
                final RowSet rowSetSlice = bucketRowSet.subSetByPositionRange(start, end);
                typedDestination.set(di, makeVector(rowSetSlice));
            }
        }
        typedDestination.setSize(size);
    }

    @Override
    public void fillPrevChunk(@NotNull final FillContext context,
            @NotNull final WritableChunk<? super Values> destination, @NotNull final RowSequence rowSequence) {
        AggregateSlicedFillContext ctx = (AggregateSlicedFillContext) context;

        final LongChunk<OrderedRowKeys> keyChunk = rowSequence.asRowKeyChunk();
        final ObjectChunk<RowSet, ? extends Values> groupRowSetPrevChunk = groupRowSetSource
                .getPrevChunk(ctx.groupRowSetGetContext, rowSequence).asObjectChunk();
        final LongChunk<? extends Values> startPrevChunk = startSource != null
                ? startSource.getPrevChunk(ctx.startGetContext, rowSequence).asLongChunk()
                : null;
        final LongChunk<? extends Values> endPrevChunk = endSource != null
                ? endSource.getPrevChunk(ctx.endGetContext, rowSequence).asLongChunk()
                : null;

        final WritableObjectChunk<ObjectVector<COMPONENT_TYPE>, ? super Values> typedDestination =
                destination.asWritableObjectChunk();
        final int size = rowSequence.intSize();
        for (int di = 0; di < size; ++di) {
            final long startPos = startPrevChunk != null ? startPrevChunk.get(di) : startOffset;
            final long endPos = endPrevChunk != null ? endPrevChunk.get(di) : endOffset;

            if (startPos == NULL_LONG && endPos == NULL_LONG) {
                // null when both start/end are null.
                typedDestination.set(di, null);
            } else if (startPos == NULL_LONG) {
                // empty vector when only start is null
                typedDestination.set(di, ObjectVectorDirect.empty());
            } else {
                final long rowKey = keyChunk.get(di);
                final RowSet groupRowSetPrev = groupRowSetPrevChunk.get(di);
                final RowSet groupRowSetToUse = groupRowSetPrev.isTracking()
                        ? groupRowSetPrev.trackingCast().prev()
                        : groupRowSetPrev;
                final long rowPos = groupRowSetToUse.find(rowKey);

                final long rowSetSize = groupRowSetToUse.size();
                final long start = ClampUtil.clampLong(0, rowSetSize, rowPos + startPos);
                final long end = ClampUtil.clampLong(0, rowSetSize, rowPos + endPos);

                // Determine the slice of the groupRowSetSource from start to end.
                final RowSet rowSetSlice = groupRowSetToUse.subSetByPositionRange(start, end);
                typedDestination.set(di, makePrevVector(rowSetSlice));
            }
        }
        typedDestination.setSize(size);
    }
}
