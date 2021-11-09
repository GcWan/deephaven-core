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
 * <p>3-Tuple (triple) key class composed of short, int, and long elements.
 * <p>Generated by {@link io.deephaven.engine.util.tuples.TupleCodeGenerator}.
 */
public class ShortIntLongTuple implements Comparable<ShortIntLongTuple>, Externalizable, StreamingExternalizable, CanonicalizableTuple<ShortIntLongTuple> {

    private static final long serialVersionUID = 1L;

    private short element1;
    private int element2;
    private long element3;

    private transient int cachedHashCode;

    public ShortIntLongTuple(
            final short element1,
            final int element2,
            final long element3
    ) {
        initialize(
                element1,
                element2,
                element3
        );
    }

    /** Public no-arg constructor for {@link Externalizable} support only. <em>Application code should not use this!</em> **/
    public ShortIntLongTuple() {
    }

    private void initialize(
            final short element1,
            final int element2,
            final long element3
    ) {
        this.element1 = element1;
        this.element2 = element2;
        this.element3 = element3;
        cachedHashCode = ((31 +
                Short.hashCode(element1)) * 31 +
                Integer.hashCode(element2)) * 31 +
                Long.hashCode(element3);
    }

    public final short getFirstElement() {
        return element1;
    }

    public final int getSecondElement() {
        return element2;
    }

    public final long getThirdElement() {
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
        final ShortIntLongTuple typedOther = (ShortIntLongTuple) other;
        // @formatter:off
        return element1 == typedOther.element1 &&
               element2 == typedOther.element2 &&
               element3 == typedOther.element3;
        // @formatter:on
    }

    @Override
    public final int compareTo(@NotNull final ShortIntLongTuple other) {
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
        out.writeShort(element1);
        out.writeInt(element2);
        out.writeLong(element3);
    }

    @Override
    public void readExternal(@NotNull final ObjectInput in) throws IOException, ClassNotFoundException {
        initialize(
                in.readShort(),
                in.readInt(),
                in.readLong()
        );
    }

    @Override
    public void writeExternalStreaming(@NotNull final ObjectOutput out, @NotNull final TIntObjectMap<SerializationUtils.Writer> cachedWriters) throws IOException {
        out.writeShort(element1);
        out.writeInt(element2);
        out.writeLong(element3);
    }

    @Override
    public void readExternalStreaming(@NotNull final ObjectInput in, @NotNull final TIntObjectMap<SerializationUtils.Reader> cachedReaders) throws Exception {
        initialize(
                in.readShort(),
                in.readInt(),
                in.readLong()
        );
    }

    @Override
    public String toString() {
        return "ShortIntLongTuple{" +
                element1 + ", " +
                element2 + ", " +
                element3 + '}';
    }

    @Override
    public ShortIntLongTuple canonicalize(@NotNull final UnaryOperator<Object> canonicalizer) {
        return this;
    }
}
