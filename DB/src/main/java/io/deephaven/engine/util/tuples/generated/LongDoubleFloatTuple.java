package io.deephaven.engine.util.tuples.generated;

import io.deephaven.engine.tables.lang.LanguageFunctionUtil;
import io.deephaven.engine.util.serialization.SerializationUtils;
import io.deephaven.engine.util.serialization.StreamingExternalizable;
import io.deephaven.engine.util.tuples.CanonicalizableTuple;
import gnu.trove.map.TIntObjectMap;
import org.jetbrains.annotations.NotNull;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.function.UnaryOperator;

/**
 * <p>3-Tuple (triple) key class composed of long, double, and float elements.
 * <p>Generated by {@link io.deephaven.engine.util.tuples.TupleCodeGenerator}.
 */
public class LongDoubleFloatTuple implements Comparable<LongDoubleFloatTuple>, Externalizable, StreamingExternalizable, CanonicalizableTuple<LongDoubleFloatTuple> {

    private static final long serialVersionUID = 1L;

    private long element1;
    private double element2;
    private float element3;

    private transient int cachedHashCode;

    public LongDoubleFloatTuple(
            final long element1,
            final double element2,
            final float element3
    ) {
        initialize(
                element1,
                element2,
                element3
        );
    }

    /** Public no-arg constructor for {@link Externalizable} support only. <em>Application code should not use this!</em> **/
    public LongDoubleFloatTuple() {
    }

    private void initialize(
            final long element1,
            final double element2,
            final float element3
    ) {
        this.element1 = element1;
        this.element2 = element2;
        this.element3 = element3;
        cachedHashCode = ((31 +
                Long.hashCode(element1)) * 31 +
                Double.hashCode(element2)) * 31 +
                Float.hashCode(element3);
    }

    public final long getFirstElement() {
        return element1;
    }

    public final double getSecondElement() {
        return element2;
    }

    public final float getThirdElement() {
        return element3;
    }

    @Override
    public final int hashCode() {
        return cachedHashCode;
    }

    @Override
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final LongDoubleFloatTuple typedOther = (LongDoubleFloatTuple) other;
        // @formatter:off
        return element1 == typedOther.element1 &&
               element2 == typedOther.element2 &&
               element3 == typedOther.element3;
        // @formatter:on
    }

    @Override
    public final int compareTo(@NotNull final LongDoubleFloatTuple other) {
        if (this == other) {
            return 0;
        }
        int comparison;
        // @formatter:off
        return 0 != (comparison = LanguageFunctionUtil.compareTo(element1, other.element1)) ? comparison :
               0 != (comparison = LanguageFunctionUtil.compareTo(element2, other.element2)) ? comparison :
               LanguageFunctionUtil.compareTo(element3, other.element3);
        // @formatter:on
    }

    @Override
    public void writeExternal(@NotNull final ObjectOutput out) throws IOException {
        out.writeLong(element1);
        out.writeDouble(element2);
        out.writeFloat(element3);
    }

    @Override
    public void readExternal(@NotNull final ObjectInput in) throws IOException, ClassNotFoundException {
        initialize(
                in.readLong(),
                in.readDouble(),
                in.readFloat()
        );
    }

    @Override
    public void writeExternalStreaming(@NotNull final ObjectOutput out, @NotNull final TIntObjectMap<SerializationUtils.Writer> cachedWriters) throws IOException {
        out.writeLong(element1);
        out.writeDouble(element2);
        out.writeFloat(element3);
    }

    @Override
    public void readExternalStreaming(@NotNull final ObjectInput in, @NotNull final TIntObjectMap<SerializationUtils.Reader> cachedReaders) throws Exception {
        initialize(
                in.readLong(),
                in.readDouble(),
                in.readFloat()
        );
    }

    @Override
    public String toString() {
        return "LongDoubleFloatTuple{" +
                element1 + ", " +
                element2 + ", " +
                element3 + '}';
    }

    @Override
    public LongDoubleFloatTuple canonicalize(@NotNull final UnaryOperator<Object> canonicalizer) {
        return this;
    }
}
