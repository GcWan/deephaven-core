package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.datastructures.util.SmartKey;
import io.deephaven.engine.chunk.Attributes.Values;
import io.deephaven.engine.chunk.CharChunk;
import io.deephaven.engine.chunk.Chunk;
import io.deephaven.engine.chunk.LongChunk;
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
import io.deephaven.engine.tuple.generated.CharLongShortTuple;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types Character, Long, and Short.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class CharacterReinterpretedDateTimeShortColumnTupleSource extends AbstractTupleSource<CharLongShortTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link CharacterReinterpretedDateTimeShortColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<CharLongShortTuple, Character, Long, Short> FACTORY = new Factory();

    private final ColumnSource<Character> columnSource1;
    private final ColumnSource<Long> columnSource2;
    private final ColumnSource<Short> columnSource3;

    public CharacterReinterpretedDateTimeShortColumnTupleSource(
            @NotNull final ColumnSource<Character> columnSource1,
            @NotNull final ColumnSource<Long> columnSource2,
            @NotNull final ColumnSource<Short> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final CharLongShortTuple createTuple(final long indexKey) {
        return new CharLongShortTuple(
                columnSource1.getChar(indexKey),
                columnSource2.getLong(indexKey),
                columnSource3.getShort(indexKey)
        );
    }

    @Override
    public final CharLongShortTuple createPreviousTuple(final long indexKey) {
        return new CharLongShortTuple(
                columnSource1.getPrevChar(indexKey),
                columnSource2.getPrevLong(indexKey),
                columnSource3.getPrevShort(indexKey)
        );
    }

    @Override
    public final CharLongShortTuple createTupleFromValues(@NotNull final Object... values) {
        return new CharLongShortTuple(
                TypeUtils.unbox((Character)values[0]),
                DateTimeUtil.nanos((DateTime)values[1]),
                TypeUtils.unbox((Short)values[2])
        );
    }

    @Override
    public final CharLongShortTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new CharLongShortTuple(
                TypeUtils.unbox((Character)values[0]),
                TypeUtils.unbox((Long)values[1]),
                TypeUtils.unbox((Short)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final CharLongShortTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationIndexKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationIndexKey, tuple.getFirstElement());
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
    public final Object exportToExternalKey(@NotNull final CharLongShortTuple tuple) {
        return new SmartKey(
                TypeUtils.box(tuple.getFirstElement()),
                DateTimeUtil.nanosToTime(tuple.getSecondElement()),
                TypeUtils.box(tuple.getThirdElement())
        );
    }

    @Override
    public final Object exportElement(@NotNull final CharLongShortTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return TypeUtils.box(tuple.getFirstElement());
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
    public final Object exportElementReinterpreted(@NotNull final CharLongShortTuple tuple, int elementIndex) {
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
        WritableObjectChunk<CharLongShortTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        CharChunk<Values> chunk1 = chunks[0].asCharChunk();
        LongChunk<Values> chunk2 = chunks[1].asLongChunk();
        ShortChunk<Values> chunk3 = chunks[2].asShortChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new CharLongShortTuple(chunk1.get(ii), chunk2.get(ii), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link CharacterReinterpretedDateTimeShortColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<CharLongShortTuple, Character, Long, Short> {

        private Factory() {
        }

        @Override
        public TupleSource<CharLongShortTuple> create(
                @NotNull final ColumnSource<Character> columnSource1,
                @NotNull final ColumnSource<Long> columnSource2,
                @NotNull final ColumnSource<Short> columnSource3
        ) {
            return new CharacterReinterpretedDateTimeShortColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
