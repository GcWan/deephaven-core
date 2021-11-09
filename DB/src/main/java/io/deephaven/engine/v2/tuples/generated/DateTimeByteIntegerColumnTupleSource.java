package io.deephaven.engine.v2.tuples.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.tables.utils.DateTime;
import io.deephaven.engine.tables.utils.DateTimeUtils;
import io.deephaven.engine.util.tuples.generated.LongByteIntTuple;
import io.deephaven.engine.v2.sources.ColumnSource;
import io.deephaven.engine.v2.sources.WritableSource;
import io.deephaven.engine.v2.sources.chunk.Attributes;
import io.deephaven.engine.v2.sources.chunk.ByteChunk;
import io.deephaven.engine.v2.sources.chunk.Chunk;
import io.deephaven.engine.v2.sources.chunk.IntChunk;
import io.deephaven.engine.v2.sources.chunk.ObjectChunk;
import io.deephaven.engine.v2.sources.chunk.WritableChunk;
import io.deephaven.engine.v2.sources.chunk.WritableObjectChunk;
import io.deephaven.engine.v2.tuples.AbstractTupleSource;
import io.deephaven.engine.v2.tuples.ThreeColumnTupleSourceFactory;
import io.deephaven.engine.v2.tuples.TupleSource;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types DateTime, Byte, and Integer.
 * <p>Generated by {@link io.deephaven.engine.v2.tuples.TupleSourceCodeGenerator}.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DateTimeByteIntegerColumnTupleSource extends AbstractTupleSource<LongByteIntTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link DateTimeByteIntegerColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<LongByteIntTuple, DateTime, Byte, Integer> FACTORY = new Factory();

    private final ColumnSource<DateTime> columnSource1;
    private final ColumnSource<Byte> columnSource2;
    private final ColumnSource<Integer> columnSource3;

    public DateTimeByteIntegerColumnTupleSource(
            @NotNull final ColumnSource<DateTime> columnSource1,
            @NotNull final ColumnSource<Byte> columnSource2,
            @NotNull final ColumnSource<Integer> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final LongByteIntTuple createTuple(final long indexKey) {
        return new LongByteIntTuple(
                DateTimeUtils.nanos(columnSource1.get(indexKey)),
                columnSource2.getByte(indexKey),
                columnSource3.getInt(indexKey)
        );
    }

    @Override
    public final LongByteIntTuple createPreviousTuple(final long indexKey) {
        return new LongByteIntTuple(
                DateTimeUtils.nanos(columnSource1.getPrev(indexKey)),
                columnSource2.getPrevByte(indexKey),
                columnSource3.getPrevInt(indexKey)
        );
    }

    @Override
    public final LongByteIntTuple createTupleFromValues(@NotNull final Object... values) {
        return new LongByteIntTuple(
                DateTimeUtils.nanos((DateTime)values[0]),
                TypeUtils.unbox((Byte)values[1]),
                TypeUtils.unbox((Integer)values[2])
        );
    }

    @Override
    public final LongByteIntTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new LongByteIntTuple(
                DateTimeUtils.nanos((DateTime)values[0]),
                TypeUtils.unbox((Byte)values[1]),
                TypeUtils.unbox((Integer)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final LongByteIntTuple tuple, final int elementIndex, @NotNull final WritableSource<ELEMENT_TYPE> writableSource, final long destinationIndexKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationIndexKey, (ELEMENT_TYPE) DateTimeUtils.nanosToTime(tuple.getFirstElement()));
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
        throw new IndexOutOfBoundsException("Invalid element rowSet " + elementIndex + " for export");
    }

    @Override
    public final Object exportToExternalKey(@NotNull final LongByteIntTuple tuple) {
        return new SmartKey(
                DateTimeUtils.nanosToTime(tuple.getFirstElement()),
                TypeUtils.box(tuple.getSecondElement()),
                TypeUtils.box(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final LongByteIntTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return DateTimeUtils.nanosToTime(tuple.getFirstElement());
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
    public final Object exportElementReinterpreted(@NotNull final LongByteIntTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return DateTimeUtils.nanosToTime(tuple.getFirstElement());
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
    protected void convertChunks(@NotNull WritableChunk<? super Attributes.Values> destination, int chunkSize, Chunk<Attributes.Values> [] chunks) {
        WritableObjectChunk<LongByteIntTuple, ? super Attributes.Values> destinationObjectChunk = destination.asWritableObjectChunk();
        ObjectChunk<DateTime, Attributes.Values> chunk1 = chunks[0].asObjectChunk();
        ByteChunk<Attributes.Values> chunk2 = chunks[1].asByteChunk();
        IntChunk<Attributes.Values> chunk3 = chunks[2].asIntChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new LongByteIntTuple(DateTimeUtils.nanos(chunk1.get(ii)), chunk2.get(ii), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link DateTimeByteIntegerColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<LongByteIntTuple, DateTime, Byte, Integer> {

        private Factory() {
        }

        @Override
        public TupleSource<LongByteIntTuple> create(
                @NotNull final ColumnSource<DateTime> columnSource1,
                @NotNull final ColumnSource<Byte> columnSource2,
                @NotNull final ColumnSource<Integer> columnSource3
        ) {
            return new DateTimeByteIntegerColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
