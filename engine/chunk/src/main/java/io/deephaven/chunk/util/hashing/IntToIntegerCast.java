/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
/*
 * ---------------------------------------------------------------------------------------------------------------------
 * AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY - for any changes edit CharToIntegerCast and regenerate
 * ---------------------------------------------------------------------------------------------------------------------
 */
package io.deephaven.chunk.util.hashing;

import io.deephaven.chunk.*;
import io.deephaven.chunk.attributes.Any;

/**
 * Cast the values in the input chunk to an int.
 *
 * @param <T> the chunk's attribute
 */
public class IntToIntegerCast<T extends Any> implements ToIntFunctor<T> {
    private final WritableIntChunk<T> result;

    IntToIntegerCast(int size) {
        result = WritableIntChunk.makeWritableChunk(size);
    }

    @Override
    public IntChunk<? extends T> apply(Chunk<? extends T> input) {
        return cast(input.asIntChunk());
    }

    private IntChunk<T> cast(IntChunk<? extends T> input) {
        castInto(input, result);
        return result;
    }

    public static <T2 extends Any> void castInto(IntChunk<? extends T2> input, WritableIntChunk<T2> result) {
        for (int ii = 0; ii < input.size(); ++ii) {
            result.set(ii, (int)input.get(ii));
        }
        result.setSize(input.size());
    }

    @Override
    public void close() {
        result.close();
    }
}