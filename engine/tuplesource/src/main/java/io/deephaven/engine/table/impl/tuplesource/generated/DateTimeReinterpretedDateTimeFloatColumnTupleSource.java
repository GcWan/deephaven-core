package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.Chunk;
import io.deephaven.engine.chunk.FloatChunk;
import io.deephaven.engine.chunk.LongChunk;
import io.deephaven.engine.chunk.ObjectChunk;
import io.deephaven.engine.chunk.WritableChunk;
import io.deephaven.engine.chunk.WritableObjectChunk;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.TupleSource;
import io.deephaven.engine.table.WritableColumnSource;
import io.deephaven.engine.table.impl.tuplesource.AbstractTupleSource;
import io.deephaven.engine.table.impl.tuplesource.ThreeColumnTupleSourceFactory;
import io.deephaven.engine.time.DateTime;
import io.deephaven.engine.time.DateTimeUtil;
import io.deephaven.engine.tuple.generated.LongLongFloatTuple;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types DateTime, Long, and Float.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DateTimeReinterpretedDateTimeFloatColumnTupleSource extends AbstractTupleSource<LongLongFloatTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link DateTimeReinterpretedDateTimeFloatColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<LongLongFloatTuple, DateTime, Long, Float> FACTORY = new Factory();

    private final ColumnSource<DateTime> columnSource1;
    private final ColumnSource<Long> columnSource2;
    private final ColumnSource<Float> columnSource3;

    public DateTimeReinterpretedDateTimeFloatColumnTupleSource(
            @NotNull final ColumnSource<DateTime> columnSource1,
            @NotNull final ColumnSource<Long> columnSource2,
            @NotNull final ColumnSource<Float> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final LongLongFloatTuple createTuple(final long indexKey) {
        return new LongLongFloatTuple(
                DateTimeUtil.nanos(columnSource1.get(indexKey)),
                columnSource2.getLong(indexKey),
                columnSource3.getFloat(indexKey)
        );
    }

    @Override
    public final LongLongFloatTuple createPreviousTuple(final long indexKey) {
        return new LongLongFloatTuple(
                DateTimeUtil.nanos(columnSource1.getPrev(indexKey)),
                columnSource2.getPrevLong(indexKey),
                columnSource3.getPrevFloat(indexKey)
        );
    }

    @Override
    public final LongLongFloatTuple createTupleFromValues(@NotNull final Object... values) {
        return new LongLongFloatTuple(
                DateTimeUtil.nanos((DateTime)values[0]),
                DateTimeUtil.nanos((DateTime)values[1]),
                TypeUtils.unbox((Float)values[2])
        );
    }

    @Override
    public final LongLongFloatTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new LongLongFloatTuple(
                DateTimeUtil.nanos((DateTime)values[0]),
                TypeUtils.unbox((Long)values[1]),
                TypeUtils.unbox((Float)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final LongLongFloatTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationIndexKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) DateTimeUtil.nanosToTime(tuple.getFirstElement()));
            return;
        }
        if (elementIndex == 1) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) DateTimeUtil.nanosToTime(tuple.getSecondElement()));
            return;
        }
        if (elementIndex == 2) {
            writableSource.set(destinationIndexKey, tuple.getThirdElement());
            return;
        }
        throw new IndexOutOfBoundsException("Invalid element index " + elementIndex + " for export");
    }

    @Override
    public final Object exportToExternalKey(@NotNull final LongLongFloatTuple tuple) {
        return new SmartKey(
                DateTimeUtil.nanosToTime(tuple.getFirstElement()),
                DateTimeUtil.nanosToTime(tuple.getSecondElement()),
                TypeUtils.box(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final LongLongFloatTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return DateTimeUtil.nanosToTime(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return DateTimeUtil.nanosToTime(tuple.getSecondElement());
        }
        if (elementIndex == 2) {
            return TypeUtils.box(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    public final Object exportElementReinterpreted(@NotNull final LongLongFloatTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return DateTimeUtil.nanosToTime(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return TypeUtils.box(tuple.getSecondElement());
        }
        if (elementIndex == 2) {
            return TypeUtils.box(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    protected void convertChunks(@NotNull WritableChunk<? super Values> destination, int chunkSize, Chunk<Values> [] chunks) {
        WritableObjectChunk<LongLongFloatTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        ObjectChunk<DateTime, Values> chunk1 = chunks[0].asObjectChunk();
        LongChunk<Values> chunk2 = chunks[1].asLongChunk();
        FloatChunk<Values> chunk3 = chunks[2].asFloatChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new LongLongFloatTuple(DateTimeUtil.nanos(chunk1.get(ii)), chunk2.get(ii), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link DateTimeReinterpretedDateTimeFloatColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<LongLongFloatTuple, DateTime, Long, Float> {

        private Factory() {
        }

        @Override
        public TupleSource<LongLongFloatTuple> create(
                @NotNull final ColumnSource<DateTime> columnSource1,
                @NotNull final ColumnSource<Long> columnSource2,
                @NotNull final ColumnSource<Float> columnSource3
        ) {
            return new DateTimeReinterpretedDateTimeFloatColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
