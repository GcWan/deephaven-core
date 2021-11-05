/* ---------------------------------------------------------------------------------------------------------------------
 * AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY - for any changes edit TestCharSegmentedSortedArray and regenerate
 * ------------------------------------------------------------------------------------------------------------------ */
package io.deephaven.engine.v2.ssa;

import io.deephaven.base.verify.AssertionFailure;
import io.deephaven.engine.tables.Table;
import io.deephaven.engine.tables.live.LiveTableMonitor;
import io.deephaven.engine.util.liveness.LivenessScope;
import io.deephaven.engine.util.liveness.LivenessScopeStack;
import io.deephaven.engine.v2.*;
import io.deephaven.engine.v2.sources.ColumnSource;
import io.deephaven.engine.v2.sources.chunk.Attributes;
import io.deephaven.engine.v2.sources.chunk.Attributes.RowKeys;
import io.deephaven.engine.v2.sources.chunk.Attributes.Values;
import io.deephaven.engine.v2.sources.chunk.IntChunk;
import io.deephaven.engine.v2.sources.chunk.LongChunk;
import io.deephaven.engine.v2.utils.RowSet;
import io.deephaven.engine.v2.utils.RowSetShiftData;
import io.deephaven.test.types.ParallelTest;
import io.deephaven.util.SafeCloseable;
import junit.framework.TestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.experimental.categories.Category;

import java.util.Random;

import static io.deephaven.engine.v2.TstUtils.*;

@Category(ParallelTest.class)
public class TestIntSegmentedSortedArray extends LiveTableTestCase {

    public void testInsertion() {
        final SsaTestHelpers.TestDescriptor desc = new SsaTestHelpers.TestDescriptor();
        for (int seed = 0; seed < 10; ++seed) {
            for (int tableSize = 100; tableSize <= 1000; tableSize *= 10) {
                for (int nodeSize = 8; nodeSize <= 2048; nodeSize *= 2) {
                    testUpdates(desc.reset(seed, tableSize, nodeSize), true, false);
                }
            }
        }
    }

    public void testRemove() {
        final SsaTestHelpers.TestDescriptor desc = new SsaTestHelpers.TestDescriptor();
        for (int seed = 0; seed < 20; ++seed) {
            for (int tableSize = 100; tableSize <= 10000; tableSize *= 10) {
                for (int nodeSize = 16; nodeSize <= 2048; nodeSize *= 2) {
                    testUpdates(desc.reset(seed, tableSize, nodeSize), false, true);
                }
            }
        }
    }

    public void testInsertAndRemove() {
        final SsaTestHelpers.TestDescriptor desc = new SsaTestHelpers.TestDescriptor();
        for (int seed = 0; seed < 10; ++seed) {
            for (int tableSize = 100; tableSize <= 10000; tableSize *= 10) {
                for (int nodeSize = 16; nodeSize <= 2048; nodeSize *= 2) {
                    testUpdates(desc.reset(seed, tableSize, nodeSize), true, true);
                }
            }
        }
    }

    public void testShifts() {
        final SsaTestHelpers.TestDescriptor desc = new SsaTestHelpers.TestDescriptor();
        for (int seed = 0; seed < 20; ++seed) {
            for (int tableSize = 10; tableSize <= 10000; tableSize *= 10) {
                for (int nodeSize = 16; nodeSize <= 2048; nodeSize *= 2) {
                    testShifts(desc.reset(seed, tableSize, nodeSize));
                }
            }
        }
    }

    private void testShifts(@NotNull final SsaTestHelpers.TestDescriptor desc) {
        final Random random = new Random(desc.seed());
        final TstUtils.ColumnInfo[] columnInfo;
        final QueryTable table = getTable(desc.tableSize(), random, columnInfo = initColumnInfos(new String[]{"Value"},
                SsaTestHelpers.getGeneratorForInt()));

        final Table asInteger = SsaTestHelpers.prepareTestTableForInt(table);

        final IntSegmentedSortedArray ssa = new IntSegmentedSortedArray(desc.nodeSize());

        //noinspection unchecked
        final ColumnSource<Integer> valueSource = asInteger.getColumnSource("Value");

        checkSsaInitial(asInteger, ssa, valueSource, desc);

        try (final SafeCloseable ignored = LivenessScopeStack.open(new LivenessScope(true), true)) {
            final Listener asIntegerListener = new InstrumentedListenerAdapter(asInteger, false) {
                @Override
                public void onUpdate(Update upstream) {
                    try (final ColumnSource.GetContext checkContext = valueSource.makeGetContext(asInteger.getRowSet().getPrevRowSet().intSize())) {
                        final RowSet relevantIndices = asInteger.getRowSet().getPrevRowSet();
                        checkSsa(ssa, valueSource.getPrevChunk(checkContext, relevantIndices).asIntChunk(), relevantIndices.asRowKeyChunk(), desc);
                    }

                    final int size = Math.max(upstream.modified.intSize() + Math.max(upstream.added.intSize(), upstream.removed.intSize()), (int) upstream.shifted.getEffectiveSize());
                    try (final ColumnSource.GetContext getContext = valueSource.makeGetContext(size)) {
                        ssa.validate();

                        final RowSet takeout = upstream.removed.union(upstream.getModifiedPreShift());
                        if (takeout.isNonempty()) {
                            final IntChunk<? extends Values> valuesToRemove = valueSource.getPrevChunk(getContext, takeout).asIntChunk();
                            ssa.remove(valuesToRemove, takeout.asRowKeyChunk());
                        }

                        ssa.validate();

                        try (final ColumnSource.GetContext checkContext = valueSource.makeGetContext(asInteger.getRowSet().getPrevRowSet().intSize())) {
                            final RowSet relevantIndices = asInteger.getRowSet().getPrevRowSet().minus(takeout);
                            checkSsa(ssa, valueSource.getPrevChunk(checkContext, relevantIndices).asIntChunk(), relevantIndices.asRowKeyChunk(), desc);
                        }

                        if (upstream.shifted.nonempty()) {
                            final RowSetShiftData.Iterator sit = upstream.shifted.applyIterator();
                            while (sit.hasNext()) {
                                sit.next();
                                final RowSet rowSetToShift = table.getRowSet().getPrevRowSet().subSetByKeyRange(sit.beginRange(), sit.endRange()).minus(upstream.getModifiedPreShift()).minus(upstream.removed);
                                if (rowSetToShift.isEmpty()) {
                                    continue;
                                }

                                final IntChunk<? extends Values> shiftValues = valueSource.getPrevChunk(getContext, rowSetToShift).asIntChunk();

                                if (sit.polarityReversed()) {
                                    ssa.applyShiftReverse(shiftValues, rowSetToShift.asRowKeyChunk(), sit.shiftDelta());
                                } else {
                                    ssa.applyShift(shiftValues, rowSetToShift.asRowKeyChunk(), sit.shiftDelta());
                                }
                            }
                        }

                        ssa.validate();

                        final RowSet putin = upstream.added.union(upstream.modified);

                        try (final ColumnSource.GetContext checkContext = valueSource.makeGetContext(asInteger.intSize())) {
                            final RowSet relevantIndices = asInteger.getRowSet().minus(putin);
                            checkSsa(ssa, valueSource.getChunk(checkContext, relevantIndices).asIntChunk(), relevantIndices.asRowKeyChunk(), desc);
                        }

                        if (putin.isNonempty()) {
                            final IntChunk<? extends Values> valuesToInsert = valueSource.getChunk(getContext, putin).asIntChunk();
                            ssa.insert(valuesToInsert, putin.asRowKeyChunk());
                        }

                        ssa.validate();
                    }
                }
            };
            asInteger.listenForUpdates(asIntegerListener);

            while (desc.advance(50)) {
                System.out.println();
                LiveTableMonitor.DEFAULT.runWithinUnitTestCycle(() ->
                        GenerateTableUpdates.generateShiftAwareTableUpdates(GenerateTableUpdates.DEFAULT_PROFILE, desc.tableSize(), random, table, columnInfo));

                try (final ColumnSource.GetContext getContext = valueSource.makeGetContext(asInteger.intSize())) {
                    checkSsa(ssa, valueSource.getChunk(getContext, asInteger.getRowSet()).asIntChunk(), asInteger.getRowSet().asRowKeyChunk(), desc);
                }
            }
        }
    }

    private void testUpdates(@NotNull final SsaTestHelpers.TestDescriptor desc, boolean allowAddition, boolean allowRemoval) {
        final Random random = new Random(desc.seed());
        final TstUtils.ColumnInfo[] columnInfo;
        final QueryTable table = getTable(desc.tableSize(), random, columnInfo = initColumnInfos(new String[]{"Value"},
                SsaTestHelpers.getGeneratorForInt()));

        final Table asInteger = SsaTestHelpers.prepareTestTableForInt(table);

        final IntSegmentedSortedArray ssa = new IntSegmentedSortedArray(desc.nodeSize());

        //noinspection unchecked
        final ColumnSource<Integer> valueSource = asInteger.getColumnSource("Value");

        checkSsaInitial(asInteger, ssa, valueSource, desc);

        try (final SafeCloseable ignored = LivenessScopeStack.open(new LivenessScope(true), true)) {
            final ShiftObliviousListener asIntegerListener = new ShiftObliviousInstrumentedListenerAdapter(asInteger, false) {
                @Override
                public void onUpdate(RowSet added, RowSet removed, RowSet modified) {
                    try (final ColumnSource.GetContext getContext = valueSource.makeGetContext(Math.max(added.intSize(), removed.intSize()))) {
                        if (removed.isNonempty()) {
                            final IntChunk<? extends Values> valuesToRemove = valueSource.getPrevChunk(getContext, removed).asIntChunk();
                            ssa.remove(valuesToRemove, removed.asRowKeyChunk());
                        }
                        if (added.isNonempty()) {
                            ssa.insert(valueSource.getChunk(getContext, added).asIntChunk(), added.asRowKeyChunk());
                        }
                    }
                }
            };
            asInteger.listenForUpdates(asIntegerListener);

            while (desc.advance(50)) {
                LiveTableMonitor.DEFAULT.runWithinUnitTestCycle(() -> {
                    final RowSet[] notify = GenerateTableUpdates.computeTableUpdates(desc.tableSize(), random, table, columnInfo, allowAddition, allowRemoval, false);
                    assertTrue(notify[2].isEmpty());
                    table.notifyListeners(notify[0], notify[1], notify[2]);
                });

                try (final ColumnSource.GetContext getContext = valueSource.makeGetContext(asInteger.intSize())) {
                    checkSsa(ssa, valueSource.getChunk(getContext, asInteger.getRowSet()).asIntChunk(), asInteger.getRowSet().asRowKeyChunk(), desc);
                }

                if (!allowAddition && table.size() == 0) {
                    break;
                }
            }
        }
    }

    private void checkSsaInitial(Table asInteger, IntSegmentedSortedArray ssa, ColumnSource<?> valueSource, @NotNull final SsaTestHelpers.TestDescriptor desc) {
        try (final ColumnSource.GetContext getContext = valueSource.makeGetContext(asInteger.intSize())) {
            final IntChunk<? extends Values> valueChunk = valueSource.getChunk(getContext, asInteger.getRowSet()).asIntChunk();
            final LongChunk<Attributes.OrderedRowKeys> tableIndexChunk = asInteger.getRowSet().asRowKeyChunk();

            ssa.insert(valueChunk, tableIndexChunk);

            checkSsa(ssa, valueChunk, tableIndexChunk, desc);
        }
    }

    private void checkSsa(IntSegmentedSortedArray ssa, IntChunk<? extends Values> valueChunk, LongChunk<? extends RowKeys> tableIndexChunk, @NotNull final SsaTestHelpers.TestDescriptor desc) {
        try {
            ssa.validate();
            IntSsaChecker.checkSsa(ssa, valueChunk, tableIndexChunk);
        } catch (AssertionFailure | SsaChecker.SsaCheckException e) {
            TestCase.fail("Check failed at " + desc + ": " + e.getMessage());
        }
    }
}
