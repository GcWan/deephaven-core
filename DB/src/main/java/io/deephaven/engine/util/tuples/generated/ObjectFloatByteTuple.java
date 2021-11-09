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
 * <p>3-Tuple (triple) key class composed of Object, float, and byte elements.
 * <p>Generated by {@link io.deephaven.engine.util.tuples.TupleCodeGenerator}.
 */
public class ObjectFloatByteTuple implements Comparable<ObjectFloatByteTuple>, Externalizable, StreamingExternalizable, CanonicalizableTuple<ObjectFloatByteTuple> {

    private static final long serialVersionUID = 1L;

    private Object element1;
    private float element2;
    private byte element3;

    private transient int cachedHashCode;

    public ObjectFloatByteTuple(
            final Object element1,
            final float element2,
            final byte element3
    ) {
        initialize(
                element1,
                element2,
                element3
        );
    }

    /** Public no-arg constructor for {@link Externalizable} support only. <em>Application code should not use this!</em> **/
    public ObjectFloatByteTuple() {
    }

    private void initialize(
            final Object element1,
            final float element2,
            final byte element3
    ) {
        this.element1 = element1;
        this.element2 = element2;
        this.element3 = element3;
        cachedHashCode = ((31 +
                Objects.hashCode(element1)) * 31 +
                Float.hashCode(element2)) * 31 +
                Byte.hashCode(element3);
    }

    public final Object getFirstElement() {
        return element1;
    }

    public final float getSecondElement() {
        return element2;
    }

    public final byte getThirdElement() {
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
        final ObjectFloatByteTuple typedOther = (ObjectFloatByteTuple) other;
        // @formatter:off
        return Objects.equals(element1, typedOther.element1) &&
               element2 == typedOther.element2 &&
               element3 == typedOther.element3;
        // @formatter:on
    }

    @Override
    public final int compareTo(@NotNull final ObjectFloatByteTuple other) {
        if (this == other) {
            return 0;
        }
        int comparison;
        // @formatter:off
        return 0 != (comparison = LanguageFunctionUtil.compareTo((Comparable)element1, (Comparable)other.element1)) ? comparison :
               0 != (comparison = LanguageFunctionUtil.compareTo(element2, other.element2)) ? comparison :
               LanguageFunctionUtil.compareTo(element3, other.element3);
        // @formatter:on
    }

    @Override
    public void writeExternal(@NotNull final ObjectOutput out) throws IOException {
        out.writeObject(element1);
        out.writeFloat(element2);
        out.writeByte(element3);
    }

    @Override
    public void readExternal(@NotNull final ObjectInput in) throws IOException, ClassNotFoundException {
        initialize(
                in.readObject(),
                in.readFloat(),
                in.readByte()
        );
    }

    @Override
    public void writeExternalStreaming(@NotNull final ObjectOutput out, @NotNull final TIntObjectMap<SerializationUtils.Writer> cachedWriters) throws IOException {
        StreamingExternalizable.writeObjectElement(out, cachedWriters, 0, element1);
        out.writeFloat(element2);
        out.writeByte(element3);
    }

    @Override
    public void readExternalStreaming(@NotNull final ObjectInput in, @NotNull final TIntObjectMap<SerializationUtils.Reader> cachedReaders) throws Exception {
        initialize(
                StreamingExternalizable.readObjectElement(in, cachedReaders, 0),
                in.readFloat(),
                in.readByte()
        );
    }

    @Override
    public String toString() {
        return "ObjectFloatByteTuple{" +
                element1 + ", " +
                element2 + ", " +
                element3 + '}';
    }

    @Override
    public ObjectFloatByteTuple canonicalize(@NotNull final UnaryOperator<Object> canonicalizer) {
        final Object canonicalizedElement1 = canonicalizer.apply(element1);
        return canonicalizedElement1 == element1
                ? this : new ObjectFloatByteTuple(canonicalizedElement1, element2, element3);
    }
}
