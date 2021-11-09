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
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * <p>3-Tuple (triple) key class composed of long, short, and Object elements.
 * <p>Generated by {@link io.deephaven.engine.util.tuples.TupleCodeGenerator}.
 */
public class LongShortObjectTuple implements Comparable<LongShortObjectTuple>, Externalizable, StreamingExternalizable, CanonicalizableTuple<LongShortObjectTuple> {

    private static final long serialVersionUID = 1L;

    private long element1;
    private short element2;
    private Object element3;

    private transient int cachedHashCode;

    public LongShortObjectTuple(
            final long element1,
            final short element2,
            final Object element3
    ) {
        initialize(
                element1,
                element2,
                element3
        );
    }

    /** Public no-arg constructor for {@link Externalizable} support only. <em>Application code should not use this!</em> **/
    public LongShortObjectTuple() {
    }

    private void initialize(
            final long element1,
            final short element2,
            final Object element3
    ) {
        this.element1 = element1;
        this.element2 = element2;
        this.element3 = element3;
        cachedHashCode = ((31 +
                Long.hashCode(element1)) * 31 +
                Short.hashCode(element2)) * 31 +
                Objects.hashCode(element3);
    }

    public final long getFirstElement() {
        return element1;
    }

    public final short getSecondElement() {
        return element2;
    }

    public final Object getThirdElement() {
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
        final LongShortObjectTuple typedOther = (LongShortObjectTuple) other;
        // @formatter:off
        return element1 == typedOther.element1 &&
               element2 == typedOther.element2 &&
               Objects.equals(element3, typedOther.element3);
        // @formatter:on
    }

    @Override
    public final int compareTo(@NotNull final LongShortObjectTuple other) {
        if (this == other) {
            return 0;
        }
        int comparison;
        // @formatter:off
        return 0 != (comparison = LanguageFunctionUtil.compareTo(element1, other.element1)) ? comparison :
               0 != (comparison = LanguageFunctionUtil.compareTo(element2, other.element2)) ? comparison :
               LanguageFunctionUtil.compareTo((Comparable)element3, (Comparable)other.element3);
        // @formatter:on
    }

    @Override
    public void writeExternal(@NotNull final ObjectOutput out) throws IOException {
        out.writeLong(element1);
        out.writeShort(element2);
        out.writeObject(element3);
    }

    @Override
    public void readExternal(@NotNull final ObjectInput in) throws IOException, ClassNotFoundException {
        initialize(
                in.readLong(),
                in.readShort(),
                in.readObject()
        );
    }

    @Override
    public void writeExternalStreaming(@NotNull final ObjectOutput out, @NotNull final TIntObjectMap<SerializationUtils.Writer> cachedWriters) throws IOException {
        out.writeLong(element1);
        out.writeShort(element2);
        StreamingExternalizable.writeObjectElement(out, cachedWriters, 2, element3);
    }

    @Override
    public void readExternalStreaming(@NotNull final ObjectInput in, @NotNull final TIntObjectMap<SerializationUtils.Reader> cachedReaders) throws Exception {
        initialize(
                in.readLong(),
                in.readShort(),
                StreamingExternalizable.readObjectElement(in, cachedReaders, 2)
        );
    }

    @Override
    public String toString() {
        return "LongShortObjectTuple{" +
                element1 + ", " +
                element2 + ", " +
                element3 + '}';
    }

    @Override
    public LongShortObjectTuple canonicalize(@NotNull final UnaryOperator<Object> canonicalizer) {
        final Object canonicalizedElement3 = canonicalizer.apply(element3);
        return canonicalizedElement3 == element3
                ? this : new LongShortObjectTuple(element1, element2, canonicalizedElement3);
    }
}
