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
 * <p>2-Tuple (double) key class composed of short and double elements.
 * <p>Generated by {@link io.deephaven.engine.util.tuples.TupleCodeGenerator}.
 */
public class ShortDoubleTuple implements Comparable<ShortDoubleTuple>, Externalizable, StreamingExternalizable, CanonicalizableTuple<ShortDoubleTuple> {

    private static final long serialVersionUID = 1L;

    private short element1;
    private double element2;

    private transient int cachedHashCode;

    public ShortDoubleTuple(
            final short element1,
            final double element2
    ) {
        initialize(
                element1,
                element2
        );
    }

    /** Public no-arg constructor for {@link Externalizable} support only. <em>Application code should not use this!</em> **/
    public ShortDoubleTuple() {
    }

    private void initialize(
            final short element1,
            final double element2
    ) {
        this.element1 = element1;
        this.element2 = element2;
        cachedHashCode = (31 +
                Short.hashCode(element1)) * 31 +
                Double.hashCode(element2);
    }

    public final short getFirstElement() {
        return element1;
    }

    public final double getSecondElement() {
        return element2;
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
        final ShortDoubleTuple typedOther = (ShortDoubleTuple) other;
        // @formatter:off
        return element1 == typedOther.element1 &&
               element2 == typedOther.element2;
        // @formatter:on
    }

    @Override
    public final int compareTo(@NotNull final ShortDoubleTuple other) {
        if (this == other) {
            return 0;
        }
        int comparison;
        // @formatter:off
        return 0 != (comparison = LanguageFunctionUtil.compareTo(element1, other.element1)) ? comparison :
               LanguageFunctionUtil.compareTo(element2, other.element2);
        // @formatter:on
    }

    @Override
    public void writeExternal(@NotNull final ObjectOutput out) throws IOException {
        out.writeShort(element1);
        out.writeDouble(element2);
    }

    @Override
    public void readExternal(@NotNull final ObjectInput in) throws IOException, ClassNotFoundException {
        initialize(
                in.readShort(),
                in.readDouble()
        );
    }

    @Override
    public void writeExternalStreaming(@NotNull final ObjectOutput out, @NotNull final TIntObjectMap<SerializationUtils.Writer> cachedWriters) throws IOException {
        out.writeShort(element1);
        out.writeDouble(element2);
    }

    @Override
    public void readExternalStreaming(@NotNull final ObjectInput in, @NotNull final TIntObjectMap<SerializationUtils.Reader> cachedReaders) throws Exception {
        initialize(
                in.readShort(),
                in.readDouble()
        );
    }

    @Override
    public String toString() {
        return "ShortDoubleTuple{" +
                element1 + ", " +
                element2 + '}';
    }

    @Override
    public ShortDoubleTuple canonicalize(@NotNull final UnaryOperator<Object> canonicalizer) {
        return this;
    }
}
