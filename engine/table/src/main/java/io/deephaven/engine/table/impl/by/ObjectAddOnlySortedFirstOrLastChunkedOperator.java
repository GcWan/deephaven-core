/* ---------------------------------------------------------------------------------------------------------------------
 * AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY - for any changes edit CharAddOnlySortedFirstOrLastChunkedOperator and regenerate
 * ------------------------------------------------------------------------------------------------------------------ */
package io.deephaven.engine.table.impl.by;

import io.deephaven.engine.table.Table;
import io.deephaven.engine.table.MatchPair;
import io.deephaven.util.compare.ObjectComparisons;
import io.deephaven.engine.table.impl.sources.ObjectArraySource;
import io.deephaven.engine.chunk.Attributes.ChunkLengths;
import io.deephaven.engine.chunk.Attributes.ChunkPositions;
import io.deephaven.engine.chunk.Attributes.RowKeys;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.*;
import org.jetbrains.annotations.NotNull;

import static io.deephaven.util.QueryConstants.NULL_LONG;

/**
 * Chunked aggregation operator for sorted first/last-by using a Object sort-column on add-only tables.
 */
public class ObjectAddOnlySortedFirstOrLastChunkedOperator extends BaseAddOnlyFirstOrLastChunkedOperator {

    private final ObjectArraySource sortColumnValues;

    ObjectAddOnlySortedFirstOrLastChunkedOperator(
            final boolean isFirst,
            @NotNull final MatchPair[] resultPairs,
            @NotNull final Table originalTable,
            final String exposeRedirectionAs) {
        super(isFirst, resultPairs, originalTable, exposeRedirectionAs);
        // region sortColumnValues initialization
        sortColumnValues = new ObjectArraySource<>(Object.class);
        // endregion sortColumnValues initialization
    }

    @Override
    public void ensureCapacity(final long tableSize) {
        super.ensureCapacity(tableSize);
        sortColumnValues.ensureCapacity(tableSize, false);
    }

    @Override
    public void addChunk(final BucketedContext bucketedContext, // Unused
                         @NotNull final Chunk<? extends Values> values,
                         @NotNull final LongChunk<? extends RowKeys> inputRowKeys,
                         @NotNull final IntChunk<RowKeys> destinations,
                         @NotNull final IntChunk<ChunkPositions> startPositions,
                         @NotNull final IntChunk<ChunkLengths> length,
                         @NotNull final WritableBooleanChunk<Values> stateModified) {
        final ObjectChunk<Object, ? extends Values> typedValues = values.asObjectChunk();
        for (int ii = 0; ii < startPositions.size(); ++ii) {
            final int startPosition = startPositions.get(ii);
            final int runLength = length.get(ii);
            final long destination = destinations.get(startPosition);
            stateModified.set(ii, addChunk(typedValues, inputRowKeys, startPosition, runLength, destination));
        }
    }

    @Override
    public boolean addChunk(final SingletonContext singletonContext, // Unused
                            final int chunkSize,
                            @NotNull final Chunk<? extends Values> values,
                            @NotNull final LongChunk<? extends RowKeys> inputRowKeys,
                            final long destination) {
        return addChunk(values.asObjectChunk(), inputRowKeys, 0, inputRowKeys.size(), destination);
    }

    private boolean addChunk(@NotNull final ObjectChunk<Object, ? extends Values> values,
                             @NotNull final LongChunk<? extends RowKeys> indices,
                             final int start,
                             final int length,
                             final long destination) {
        if (length == 0) {
            return false;
        }
        final long initialIndex = redirections.getUnsafe(destination);
        final boolean newDestination = initialIndex == NULL_LONG;

        long bestIndex;
        Object bestValue;
        if (newDestination) {
            bestIndex = indices.get(start);
            bestValue = values.get(start);
        } else {
            bestIndex = initialIndex;
            bestValue = sortColumnValues.getUnsafe(destination);
        }
        for (int ii = newDestination ? 1 : 0; ii < length; ++ii) {
            final long index = indices.get(start + ii);
            final Object value = values.get(start + ii);
            final int comparison = ObjectComparisons.compare(value, bestValue);
            // @formatter:off
            final boolean better =
                    ( isFirst && (comparison < 0 || (comparison == 0 && index < bestIndex))) ||
                    (!isFirst && (comparison > 0 || (comparison == 0 && index > bestIndex)))  ;
            // @formatter:on
            if (better) {
                bestIndex = index;
                bestValue = value;
            }
        }
        if (bestIndex == initialIndex) {
            return false;
        }
        redirections.set(destination, bestIndex);
        sortColumnValues.set(destination, bestValue);
        return true;
    }
}
