package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.CharChunk;
import io.deephaven.engine.chunk.Chunk;
import io.deephaven.engine.chunk.LongChunk;
import io.deephaven.engine.chunk.WritableChunk;
import io.deephaven.engine.chunk.WritableObjectChunk;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.TupleSource;
import io.deephaven.engine.table.WritableColumnSource;
import io.deephaven.engine.table.impl.tuplesource.AbstractTupleSource;
import io.deephaven.engine.table.impl.tuplesource.ThreeColumnTupleSourceFactory;
import io.deephaven.engine.time.DateTime;
import io.deephaven.engine.time.DateTimeUtil;
import io.deephaven.engine.tuple.generated.LongLongCharTuple;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types Long, Long, and Character.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ReinterpretedDateTimeLongCharacterColumnTupleSource extends AbstractTupleSource<LongLongCharTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link ReinterpretedDateTimeLongCharacterColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<LongLongCharTuple, Long, Long, Character> FACTORY = new Factory();

    private final ColumnSource<Long> columnSource1;
    private final ColumnSource<Long> columnSource2;
    private final ColumnSource<Character> columnSource3;

    public ReinterpretedDateTimeLongCharacterColumnTupleSource(
            @NotNull final ColumnSource<Long> columnSource1,
            @NotNull final ColumnSource<Long> columnSource2,
            @NotNull final ColumnSource<Character> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final LongLongCharTuple createTuple(final long indexKey) {
        return new LongLongCharTuple(
                columnSource1.getLong(indexKey),
                columnSource2.getLong(indexKey),
                columnSource3.getChar(indexKey)
        );
    }

    @Override
    public final LongLongCharTuple createPreviousTuple(final long indexKey) {
        return new LongLongCharTuple(
                columnSource1.getPrevLong(indexKey),
                columnSource2.getPrevLong(indexKey),
                columnSource3.getPrevChar(indexKey)
        );
    }

    @Override
    public final LongLongCharTuple createTupleFromValues(@NotNull final Object... values) {
        return new LongLongCharTuple(
                DateTimeUtil.nanos((DateTime)values[0]),
                TypeUtils.unbox((Long)values[1]),
                TypeUtils.unbox((Character)values[2])
        );
    }

    @Override
    public final LongLongCharTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new LongLongCharTuple(
                TypeUtils.unbox((Long)values[0]),
                TypeUtils.unbox((Long)values[1]),
                TypeUtils.unbox((Character)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final LongLongCharTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationIndexKey) {
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
    public final Object exportToExternalKey(@NotNull final LongLongCharTuple tuple) {
        return new SmartKey(
                DateTimeUtil.nanosToTime(tuple.getFirstElement()),
                TypeUtils.box(tuple.getSecondElement()),
                TypeUtils.box(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final LongLongCharTuple tuple, int elementIndex) {
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
    public final Object exportElementReinterpreted(@NotNull final LongLongCharTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return TypeUtils.box(tuple.getFirstElement());
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
        WritableObjectChunk<LongLongCharTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        LongChunk<Values> chunk1 = chunks[0].asLongChunk();
        LongChunk<Values> chunk2 = chunks[1].asLongChunk();
        CharChunk<Values> chunk3 = chunks[2].asCharChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new LongLongCharTuple(chunk1.get(ii), chunk2.get(ii), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link ReinterpretedDateTimeLongCharacterColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<LongLongCharTuple, Long, Long, Character> {

        private Factory() {
        }

        @Override
        public TupleSource<LongLongCharTuple> create(
                @NotNull final ColumnSource<Long> columnSource1,
                @NotNull final ColumnSource<Long> columnSource2,
                @NotNull final ColumnSource<Character> columnSource3
        ) {
            return new ReinterpretedDateTimeLongCharacterColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
