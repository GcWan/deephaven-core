package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.Chunk;
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
import io.deephaven.engine.tuple.generated.LongByteLongTuple;
import io.deephaven.util.BooleanUtils;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types DateTime, Boolean, and Long.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DateTimeBooleanReinterpretedDateTimeColumnTupleSource extends AbstractTupleSource<LongByteLongTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link DateTimeBooleanReinterpretedDateTimeColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<LongByteLongTuple, DateTime, Boolean, Long> FACTORY = new Factory();

    private final ColumnSource<DateTime> columnSource1;
    private final ColumnSource<Boolean> columnSource2;
    private final ColumnSource<Long> columnSource3;

    public DateTimeBooleanReinterpretedDateTimeColumnTupleSource(
            @NotNull final ColumnSource<DateTime> columnSource1,
            @NotNull final ColumnSource<Boolean> columnSource2,
            @NotNull final ColumnSource<Long> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final LongByteLongTuple createTuple(final long indexKey) {
        return new LongByteLongTuple(
                DateTimeUtil.nanos(columnSource1.get(indexKey)),
                BooleanUtils.booleanAsByte(columnSource2.getBoolean(indexKey)),
                columnSource3.getLong(indexKey)
        );
    }

    @Override
    public final LongByteLongTuple createPreviousTuple(final long indexKey) {
        return new LongByteLongTuple(
                DateTimeUtil.nanos(columnSource1.getPrev(indexKey)),
                BooleanUtils.booleanAsByte(columnSource2.getPrevBoolean(indexKey)),
                columnSource3.getPrevLong(indexKey)
        );
    }

    @Override
    public final LongByteLongTuple createTupleFromValues(@NotNull final Object... values) {
        return new LongByteLongTuple(
                DateTimeUtil.nanos((DateTime)values[0]),
                BooleanUtils.booleanAsByte((Boolean)values[1]),
                DateTimeUtil.nanos((DateTime)values[2])
        );
    }

    @Override
    public final LongByteLongTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new LongByteLongTuple(
                DateTimeUtil.nanos((DateTime)values[0]),
                BooleanUtils.booleanAsByte((Boolean)values[1]),
                TypeUtils.unbox((Long)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final LongByteLongTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationIndexKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) DateTimeUtil.nanosToTime(tuple.getFirstElement()));
            return;
        }
        if (elementIndex == 1) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) BooleanUtils.byteAsBoolean(tuple.getSecondElement()));
            return;
        }
        if (elementIndex == 2) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) DateTimeUtil.nanosToTime(tuple.getThirdElement()));
            return;
        }
        throw new IndexOutOfBoundsException("Invalid element index " + elementIndex + " for export");
    }

    @Override
    public final Object exportToExternalKey(@NotNull final LongByteLongTuple tuple) {
        return new SmartKey(
                DateTimeUtil.nanosToTime(tuple.getFirstElement()),
                BooleanUtils.byteAsBoolean(tuple.getSecondElement()),
                DateTimeUtil.nanosToTime(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final LongByteLongTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return DateTimeUtil.nanosToTime(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return BooleanUtils.byteAsBoolean(tuple.getSecondElement());
        }
        if (elementIndex == 2) {
            return DateTimeUtil.nanosToTime(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    public final Object exportElementReinterpreted(@NotNull final LongByteLongTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return DateTimeUtil.nanosToTime(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return BooleanUtils.byteAsBoolean(tuple.getSecondElement());
        }
        if (elementIndex == 2) {
            return TypeUtils.box(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    protected void convertChunks(@NotNull WritableChunk<? super Values> destination, int chunkSize, Chunk<Values> [] chunks) {
        WritableObjectChunk<LongByteLongTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        ObjectChunk<DateTime, Values> chunk1 = chunks[0].asObjectChunk();
        ObjectChunk<Boolean, Values> chunk2 = chunks[1].asObjectChunk();
        LongChunk<Values> chunk3 = chunks[2].asLongChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new LongByteLongTuple(DateTimeUtil.nanos(chunk1.get(ii)), BooleanUtils.booleanAsByte(chunk2.get(ii)), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link DateTimeBooleanReinterpretedDateTimeColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<LongByteLongTuple, DateTime, Boolean, Long> {

        private Factory() {
        }

        @Override
        public TupleSource<LongByteLongTuple> create(
                @NotNull final ColumnSource<DateTime> columnSource1,
                @NotNull final ColumnSource<Boolean> columnSource2,
                @NotNull final ColumnSource<Long> columnSource3
        ) {
            return new DateTimeBooleanReinterpretedDateTimeColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
