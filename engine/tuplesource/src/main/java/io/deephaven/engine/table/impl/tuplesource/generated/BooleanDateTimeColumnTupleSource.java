package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.Chunk;
import io.deephaven.engine.chunk.ObjectChunk;
import io.deephaven.engine.chunk.WritableChunk;
import io.deephaven.engine.chunk.WritableObjectChunk;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.TupleSource;
import io.deephaven.engine.table.WritableColumnSource;
import io.deephaven.engine.table.impl.tuplesource.AbstractTupleSource;
import io.deephaven.engine.table.impl.tuplesource.TwoColumnTupleSourceFactory;
import io.deephaven.engine.time.DateTime;
import io.deephaven.engine.time.DateTimeUtil;
import io.deephaven.engine.tuple.generated.ByteLongTuple;
import io.deephaven.util.BooleanUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types Boolean and DateTime.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class BooleanDateTimeColumnTupleSource extends AbstractTupleSource<ByteLongTuple> {

    /** {@link TwoColumnTupleSourceFactory} instance to create instances of {@link BooleanDateTimeColumnTupleSource}. **/
    public static final TwoColumnTupleSourceFactory<ByteLongTuple, Boolean, DateTime> FACTORY = new Factory();

    private final ColumnSource<Boolean> columnSource1;
    private final ColumnSource<DateTime> columnSource2;

    public BooleanDateTimeColumnTupleSource(
            @NotNull final ColumnSource<Boolean> columnSource1,
            @NotNull final ColumnSource<DateTime> columnSource2
    ) {
        super(columnSource1, columnSource2);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
    }

    @Override
    public final ByteLongTuple createTuple(final long indexKey) {
        return new ByteLongTuple(
                BooleanUtils.booleanAsByte(columnSource1.getBoolean(indexKey)),
                DateTimeUtil.nanos(columnSource2.get(indexKey))
        );
    }

    @Override
    public final ByteLongTuple createPreviousTuple(final long indexKey) {
        return new ByteLongTuple(
                BooleanUtils.booleanAsByte(columnSource1.getPrevBoolean(indexKey)),
                DateTimeUtil.nanos(columnSource2.getPrev(indexKey))
        );
    }

    @Override
    public final ByteLongTuple createTupleFromValues(@NotNull final Object... values) {
        return new ByteLongTuple(
                BooleanUtils.booleanAsByte((Boolean)values[0]),
                DateTimeUtil.nanos((DateTime)values[1])
        );
    }

    @Override
    public final ByteLongTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new ByteLongTuple(
                BooleanUtils.booleanAsByte((Boolean)values[0]),
                DateTimeUtil.nanos((DateTime)values[1])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final ByteLongTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationIndexKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) BooleanUtils.byteAsBoolean(tuple.getFirstElement()));
            return;
        }
        if (elementIndex == 1) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) DateTimeUtil.nanosToTime(tuple.getSecondElement()));
            return;
        }
        throw new IndexOutOfBoundsException("Invalid element index " + elementIndex + " for export");
    }

    @Override
    public final Object exportToExternalKey(@NotNull final ByteLongTuple tuple) {
        return new SmartKey(
                BooleanUtils.byteAsBoolean(tuple.getFirstElement()),
                DateTimeUtil.nanosToTime(tuple.getSecondElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final ByteLongTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return BooleanUtils.byteAsBoolean(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return DateTimeUtil.nanosToTime(tuple.getSecondElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 2 element tuple: " + elementIndex);
    }

    @Override
    public final Object exportElementReinterpreted(@NotNull final ByteLongTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return BooleanUtils.byteAsBoolean(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return DateTimeUtil.nanosToTime(tuple.getSecondElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 2 element tuple: " + elementIndex);
    }

    protected void convertChunks(@NotNull WritableChunk<? super Values> destination, int chunkSize, Chunk<Values> [] chunks) {
        WritableObjectChunk<ByteLongTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        ObjectChunk<Boolean, Values> chunk1 = chunks[0].asObjectChunk();
        ObjectChunk<DateTime, Values> chunk2 = chunks[1].asObjectChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new ByteLongTuple(BooleanUtils.booleanAsByte(chunk1.get(ii)), DateTimeUtil.nanos(chunk2.get(ii))));
        }
        destination.setSize(chunkSize);
    }

    /** {@link TwoColumnTupleSourceFactory} for instances of {@link BooleanDateTimeColumnTupleSource}. **/
    private static final class Factory implements TwoColumnTupleSourceFactory<ByteLongTuple, Boolean, DateTime> {

        private Factory() {
        }

        @Override
        public TupleSource<ByteLongTuple> create(
                @NotNull final ColumnSource<Boolean> columnSource1,
                @NotNull final ColumnSource<DateTime> columnSource2
        ) {
            return new BooleanDateTimeColumnTupleSource(
                    columnSource1,
                    columnSource2
            );
        }
    }
}
