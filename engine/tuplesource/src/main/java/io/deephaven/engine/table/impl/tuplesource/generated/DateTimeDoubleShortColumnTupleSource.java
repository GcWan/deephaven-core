package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.Chunk;
import io.deephaven.engine.chunk.DoubleChunk;
import io.deephaven.engine.chunk.ObjectChunk;
import io.deephaven.engine.chunk.ShortChunk;
import io.deephaven.engine.chunk.WritableChunk;
import io.deephaven.engine.chunk.WritableObjectChunk;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.TupleSource;
import io.deephaven.engine.table.WritableColumnSource;
import io.deephaven.engine.table.impl.tuplesource.AbstractTupleSource;
import io.deephaven.engine.table.impl.tuplesource.ThreeColumnTupleSourceFactory;
import io.deephaven.engine.time.DateTime;
import io.deephaven.engine.time.DateTimeUtil;
import io.deephaven.engine.tuple.generated.LongDoubleShortTuple;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types DateTime, Double, and Short.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DateTimeDoubleShortColumnTupleSource extends AbstractTupleSource<LongDoubleShortTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link DateTimeDoubleShortColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<LongDoubleShortTuple, DateTime, Double, Short> FACTORY = new Factory();

    private final ColumnSource<DateTime> columnSource1;
    private final ColumnSource<Double> columnSource2;
    private final ColumnSource<Short> columnSource3;

    public DateTimeDoubleShortColumnTupleSource(
            @NotNull final ColumnSource<DateTime> columnSource1,
            @NotNull final ColumnSource<Double> columnSource2,
            @NotNull final ColumnSource<Short> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final LongDoubleShortTuple createTuple(final long indexKey) {
        return new LongDoubleShortTuple(
                DateTimeUtil.nanos(columnSource1.get(indexKey)),
                columnSource2.getDouble(indexKey),
                columnSource3.getShort(indexKey)
        );
    }

    @Override
    public final LongDoubleShortTuple createPreviousTuple(final long indexKey) {
        return new LongDoubleShortTuple(
                DateTimeUtil.nanos(columnSource1.getPrev(indexKey)),
                columnSource2.getPrevDouble(indexKey),
                columnSource3.getPrevShort(indexKey)
        );
    }

    @Override
    public final LongDoubleShortTuple createTupleFromValues(@NotNull final Object... values) {
        return new LongDoubleShortTuple(
                DateTimeUtil.nanos((DateTime)values[0]),
                TypeUtils.unbox((Double)values[1]),
                TypeUtils.unbox((Short)values[2])
        );
    }

    @Override
    public final LongDoubleShortTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new LongDoubleShortTuple(
                DateTimeUtil.nanos((DateTime)values[0]),
                TypeUtils.unbox((Double)values[1]),
                TypeUtils.unbox((Short)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final LongDoubleShortTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationIndexKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) DateTimeUtil.nanosToTime(tuple.getFirstElement()));
            return;
        }
        if (elementIndex == 1) {
            writableSource.set(destinationIndexKey, tuple.getSecondElement());
            return;
        }
        if (elementIndex == 2) {
            writableSource.set(destinationIndexKey, tuple.getThirdElement());
            return;
        }
        throw new IndexOutOfBoundsException("Invalid element index " + elementIndex + " for export");
    }

    @Override
    public final Object exportToExternalKey(@NotNull final LongDoubleShortTuple tuple) {
        return new SmartKey(
                DateTimeUtil.nanosToTime(tuple.getFirstElement()),
                TypeUtils.box(tuple.getSecondElement()),
                TypeUtils.box(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final LongDoubleShortTuple tuple, int elementIndex) {
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
    public final Object exportElementReinterpreted(@NotNull final LongDoubleShortTuple tuple, int elementIndex) {
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
        WritableObjectChunk<LongDoubleShortTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        ObjectChunk<DateTime, Values> chunk1 = chunks[0].asObjectChunk();
        DoubleChunk<Values> chunk2 = chunks[1].asDoubleChunk();
        ShortChunk<Values> chunk3 = chunks[2].asShortChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new LongDoubleShortTuple(DateTimeUtil.nanos(chunk1.get(ii)), chunk2.get(ii), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link DateTimeDoubleShortColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<LongDoubleShortTuple, DateTime, Double, Short> {

        private Factory() {
        }

        @Override
        public TupleSource<LongDoubleShortTuple> create(
                @NotNull final ColumnSource<DateTime> columnSource1,
                @NotNull final ColumnSource<Double> columnSource2,
                @NotNull final ColumnSource<Short> columnSource3
        ) {
            return new DateTimeDoubleShortColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
