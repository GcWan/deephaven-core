/*
 * Copyright (c) 2016-2021 Deephaven Data Labs and Patent Pending
 */

package io.deephaven.engine.v2;

import io.deephaven.base.cache.RetentionCache;
import io.deephaven.base.verify.Require;
import io.deephaven.engine.tables.Table;
import io.deephaven.engine.tables.utils.DateTimeUtils;
import io.deephaven.engine.util.liveness.Liveness;
import io.deephaven.engine.v2.utils.AsyncErrorLogger;
import io.deephaven.engine.v2.utils.AsyncClientErrorNotifier;
import io.deephaven.engine.v2.utils.UpdatePerformanceTracker;
import io.deephaven.util.Utils;
import io.deephaven.util.annotations.ReferentialIntegrity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * This class is used for ShiftAwareListeners that represent "leaf" nodes in the update propagation tree.
 *
 * It provides an optional retention cache, to prevent listeners from being garbage collected.
 *
 * For creating internally ticking table nodes, instead use {@link BaseTable.ListenerImpl}
 */
public abstract class InstrumentedListenerAdapter extends InstrumentedListener {

    private static final RetentionCache<InstrumentedListenerAdapter> RETENTION_CACHE = new RetentionCache<>();

    private final boolean retain;

    @ReferentialIntegrity
    protected final Table source;

    /**
     * Create an instrumented listener for source. No description is provided.
     *
     * @param source The source table this listener will subscribe to - needed for preserving referential integrity
     * @param retain Whether a hard reference to this listener should be maintained to prevent it from being collected.
     *        In most scenarios, it's better to specify {@code false} and keep a reference in the calling code.
     */
    public InstrumentedListenerAdapter(@NotNull final Table source, final boolean retain) {
        this(null, source, retain);
    }

    /**
     * @param description A description for the UpdatePerformanceTracker to append to its entry description
     * @param source The source table this listener will subscribe to - needed for preserving referential integrity
     * @param retain Whether a hard reference to this listener should be maintained to prevent it from being collected.
     *        In most scenarios, it's better to specify {@code false} and keep a reference in the calling code.
     */
    public InstrumentedListenerAdapter(@Nullable final String description, @NotNull final Table source,
            final boolean retain) {
        super(description);
        this.source = Require.neqNull(source, "source");
        if (this.retain = retain) {
            RETENTION_CACHE.retain(this);
            if (Liveness.DEBUG_MODE_ENABLED) {
                Liveness.log.info().append("LivenessDebug: ShiftObliviousInstrumentedListenerAdapter ")
                        .append(Utils.REFERENT_FORMATTER, this)
                        .append(" created with retention enabled").endl();
            }
        }
        manage(source);
    }

    @Override
    public abstract void onUpdate(Update upstream);

    /**
     * Called when the source table produces an error
     *
     * @param originalException the original throwable that caused this error
     * @param sourceEntry the performance tracker entry that was active when the error occurred
     */
    @Override
    public void onFailureInternal(Throwable originalException, UpdatePerformanceTracker.Entry sourceEntry) {
        try {
            AsyncErrorLogger.log(DateTimeUtils.currentTime(), sourceEntry, sourceEntry, originalException);
            AsyncClientErrorNotifier.reportError(originalException);
        } catch (IOException e) {
            throw new RuntimeException("Exception in " + sourceEntry.toString(), originalException);
        }
    }

    @Override
    public boolean canExecute(final long step) {
        return source.satisfied(step);
    }

    @Override
    protected void destroy() {
        source.removeUpdateListener(this);
        if (retain) {
            RETENTION_CACHE.forget(this);
        }
    }
}
