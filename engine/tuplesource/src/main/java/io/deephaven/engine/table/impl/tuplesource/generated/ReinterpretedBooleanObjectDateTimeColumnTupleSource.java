package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.ByteChunk;
import io.deephaven.engine.chunk.Chunk;
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
import io.deephaven.engine.tuple.generated.ByteObjectLongTuple;
import io.deephaven.util.BooleanUtils;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types Byte, Object, and DateTime.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ReinterpretedBooleanObjectDateTimeColumnTupleSource extends AbstractTupleSource<ByteObjectLongTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link ReinterpretedBooleanObjectDateTimeColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<ByteObjectLongTuple, Byte, Object, DateTime> FACTORY = new Factory();

    private final ColumnSource<Byte> columnSource1;
    private final ColumnSource<Object> columnSource2;
    private final ColumnSource<DateTime> columnSource3;

    public ReinterpretedBooleanObjectDateTimeColumnTupleSource(
            @NotNull final ColumnSource<Byte> columnSource1,
            @NotNull final ColumnSource<Object> columnSource2,
            @NotNull final ColumnSource<DateTime> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final ByteObjectLongTuple createTuple(final long indexKey) {
        return new ByteObjectLongTuple(
                columnSource1.getByte(indexKey),
                columnSource2.get(indexKey),
                DateTimeUtil.nanos(columnSource3.get(indexKey))
        );
    }

    @Override
    public final ByteObjectLongTuple createPreviousTuple(final long indexKey) {
        return new ByteObjectLongTuple(
                columnSource1.getPrevByte(indexKey),
                columnSource2.getPrev(indexKey),
                DateTimeUtil.nanos(columnSource3.getPrev(indexKey))
        );
    }

    @Override
    public final ByteObjectLongTuple createTupleFromValues(@NotNull final Object... values) {
        return new ByteObjectLongTuple(
                BooleanUtils.booleanAsByte((Boolean)values[0]),
                values[1],
                DateTimeUtil.nanos((DateTime)values[2])
        );
    }

    @Override
    public final ByteObjectLongTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new ByteObjectLongTuple(
                TypeUtils.unbox((Byte)values[0]),
                values[1],
                DateTimeUtil.nanos((DateTime)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final ByteObjectLongTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationIndexKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) BooleanUtils.byteAsBoolean(tuple.getFirstElement()));
            return;
        }
        if (elementIndex == 1) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) tuple.getSecondElement());
            return;
        }
        if (elementIndex == 2) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) DateTimeUtil.nanosToTime(tuple.getThirdElement()));
            return;
        }
        throw new IndexOutOfBoundsException("Invalid element index " + elementIndex + " for export");
    }

    @Override
    public final Object exportToExternalKey(@NotNull final ByteObjectLongTuple tuple) {
        return new SmartKey(
                BooleanUtils.byteAsBoolean(tuple.getFirstElement()),
                tuple.getSecondElement(),
                DateTimeUtil.nanosToTime(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final ByteObjectLongTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return BooleanUtils.byteAsBoolean(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return tuple.getSecondElement();
        }
        if (elementIndex == 2) {
            return DateTimeUtil.nanosToTime(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    public final Object exportElementReinterpreted(@NotNull final ByteObjectLongTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return TypeUtils.box(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return tuple.getSecondElement();
        }
        if (elementIndex == 2) {
            return DateTimeUtil.nanosToTime(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    protected void convertChunks(@NotNull WritableChunk<? super Values> destination, int chunkSize, Chunk<Values> [] chunks) {
        WritableObjectChunk<ByteObjectLongTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        ByteChunk<Values> chunk1 = chunks[0].asByteChunk();
        ObjectChunk<Object, Values> chunk2 = chunks[1].asObjectChunk();
        ObjectChunk<DateTime, Values> chunk3 = chunks[2].asObjectChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new ByteObjectLongTuple(chunk1.get(ii), chunk2.get(ii), DateTimeUtil.nanos(chunk3.get(ii))));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link ReinterpretedBooleanObjectDateTimeColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<ByteObjectLongTuple, Byte, Object, DateTime> {

        private Factory() {
        }

        @Override
        public TupleSource<ByteObjectLongTuple> create(
                @NotNull final ColumnSource<Byte> columnSource1,
                @NotNull final ColumnSource<Object> columnSource2,
                @NotNull final ColumnSource<DateTime> columnSource3
        ) {
            return new ReinterpretedBooleanObjectDateTimeColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
