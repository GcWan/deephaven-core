package io.deephaven.engine.v2.select.analyzers;

import io.deephaven.base.verify.Assert;
import io.deephaven.engine.tables.utils.DBDateTime;
import io.deephaven.engine.v2.ModifiedColumnSet;
import io.deephaven.engine.v2.Listener;
import io.deephaven.engine.v2.select.DbArrayChunkAdapter;
import io.deephaven.engine.v2.select.SelectColumn;
import io.deephaven.engine.v2.sources.WritableChunkSink;
import io.deephaven.engine.v2.sources.WritableSource;
import io.deephaven.engine.v2.sources.chunk.Attributes;
import io.deephaven.engine.v2.sources.chunk.ChunkSource;
import io.deephaven.engine.v2.sources.chunk.WritableChunk;
import io.deephaven.engine.v2.utils.ChunkUtils;
import io.deephaven.engine.structures.RowSequence;
import io.deephaven.engine.v2.utils.RowSet;

import java.util.function.LongToIntFunction;

final public class SelectColumnLayer extends SelectOrViewColumnLayer {
    /**
     * The same reference as super.columnSource, but as a WritableSource
     */
    private final WritableSource writableSource;
    private final boolean isRedirected;

    /**
     * A memoized copy of selectColumn's data view. Use {@link SelectColumnLayer#getChunkSource()} to access.
     */
    private ChunkSource<Attributes.Values> chunkSource;

    SelectColumnLayer(SelectAndViewAnalyzer inner, String name, SelectColumn sc,
            WritableSource ws, WritableSource underlying,
            String[] deps, ModifiedColumnSet mcsBuilder, boolean isRedirected) {
        super(inner, name, sc, ws, underlying, deps, mcsBuilder);
        this.writableSource = ws;
        this.isRedirected = isRedirected;
    }

    private ChunkSource<Attributes.Values> getChunkSource() {
        if (chunkSource == null) {
            // noinspection unchecked
            chunkSource = selectColumn.getDataView();
            if (selectColumnHoldsDbArray) {
                chunkSource = new DbArrayChunkAdapter<>(chunkSource);
            }
        }
        return chunkSource;
    }

    @Override
    public void applyUpdate(final Listener.Update upstream, final RowSet toClear,
            final UpdateHelper helper) {
        final int PAGE_SIZE = 4096;
        final LongToIntFunction contextSize = (long size) -> size > PAGE_SIZE ? PAGE_SIZE : (int) size;

        if (isRedirected && upstream.removed.isNonempty()) {
            clearObjectsAtThisLevel(upstream.removed);
        }

        // recurse so that dependent intermediate columns are already updated
        inner.applyUpdate(upstream, toClear, helper);

        final boolean modifiesAffectUs =
                upstream.modified.isNonempty() && upstream.modifiedColumnSet.containsAny(myModifiedColumnSet);

        // We include modifies in our shifted sets if we are not going to process them separately.
        final RowSet preMoveKeys = helper.getPreShifted(!modifiesAffectUs);
        final RowSet postMoveKeys = helper.getPostShifted(!modifiesAffectUs);

        final long lastKey = Math.max(postMoveKeys.isEmpty() ? -1 : postMoveKeys.lastRowKey(),
                upstream.added.isEmpty() ? -1 : upstream.added.lastRowKey());
        if (lastKey != -1) {
            writableSource.ensureCapacity(lastKey + 1);
        }

        // Note that applyUpdate is called during initialization. If the table begins empty, we still want to force that
        // an initial call to getDataView() (via getChunkSource()) or else the formula will only be computed later when
        // data begins to flow; start-of-day is likely a bad time to find formula errors for our customers.
        final ChunkSource<Attributes.Values> chunkSource = getChunkSource();

        final boolean needGetContext = upstream.added.isNonempty() || modifiesAffectUs;
        final boolean needDestContext = preMoveKeys.isNonempty() || needGetContext;
        final int chunkSourceContextSize =
                contextSize.applyAsInt(Math.max(upstream.added.size(), upstream.modified.size()));
        final int destContextSize = contextSize.applyAsInt(Math.max(preMoveKeys.size(), chunkSourceContextSize));

        try (final WritableChunkSink.FillFromContext destContext =
                needDestContext ? writableSource.makeFillFromContext(destContextSize) : null;
                final ChunkSource.GetContext chunkSourceContext =
                        needGetContext ? chunkSource.makeGetContext(chunkSourceContextSize) : null) {

            // apply shifts!
            if (!isRedirected && preMoveKeys.isNonempty()) {
                assert destContext != null;
                // note: we cannot use a get context here as destination is identical to source
                final int shiftContextSize = contextSize.applyAsInt(preMoveKeys.size());
                try (final ChunkSource.FillContext srcContext = writableSource.makeFillContext(shiftContextSize);
                        final WritableChunk<Attributes.Values> chunk =
                                writableSource.getChunkType().makeWritableChunk(shiftContextSize);
                        final RowSequence.Iterator srcIter = preMoveKeys.getRowSequenceIterator();
                        final RowSequence.Iterator destIter = postMoveKeys.getRowSequenceIterator()) {

                    while (srcIter.hasMore()) {
                        final RowSequence srcKeys = srcIter.getNextRowSequenceWithLength(PAGE_SIZE);
                        final RowSequence destKeys = destIter.getNextRowSequenceWithLength(PAGE_SIZE);
                        Assert.eq(srcKeys.size(), "srcKeys.size()", destKeys.size(), "destKeys.size()");
                        writableSource.fillPrevChunk(srcContext, chunk, srcKeys);
                        writableSource.fillFromChunk(destContext, chunk, destKeys);
                    }
                }
            }

            // apply adds!
            if (upstream.added.isNonempty()) {
                assert destContext != null;
                assert chunkSourceContext != null;
                try (final RowSequence.Iterator keyIter = upstream.added.getRowSequenceIterator()) {
                    while (keyIter.hasMore()) {
                        final RowSequence keys = keyIter.getNextRowSequenceWithLength(PAGE_SIZE);
                        writableSource.fillFromChunk(destContext, chunkSource.getChunk(chunkSourceContext, keys), keys);
                    }
                }
            }

            // apply modifies!
            if (modifiesAffectUs) {
                assert chunkSourceContext != null;
                try (final RowSequence.Iterator keyIter = upstream.modified.getRowSequenceIterator()) {
                    while (keyIter.hasMore()) {
                        final RowSequence keys = keyIter.getNextRowSequenceWithLength(PAGE_SIZE);
                        writableSource.fillFromChunk(destContext, chunkSource.getChunk(chunkSourceContext, keys), keys);
                    }
                }
            }
        }

        if (!isRedirected) {
            clearObjectsAtThisLevel(toClear);
        }
    }

    private void clearObjectsAtThisLevel(RowSet keys) {
        // Only bother doing this if we're holding on to references.
        if (!writableSource.getType().isPrimitive() && (writableSource.getType() != DBDateTime.class)) {
            ChunkUtils.fillWithNullValue(writableSource, keys);
        }
    }
}
