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
 * <p>3-Tuple (triple) key class composed of float, short, and char elements.
 * <p>Generated by {@link io.deephaven.engine.util.tuples.TupleCodeGenerator}.
 */
public class FloatShortCharTuple implements Comparable<FloatShortCharTuple>, Externalizable, StreamingExternalizable, CanonicalizableTuple<FloatShortCharTuple> {

    private static final long serialVersionUID = 1L;

    private float element1;
    private short element2;
    private char element3;

    private transient int cachedHashCode;

    public FloatShortCharTuple(
            final float element1,
            final short element2,
            final char element3
    ) {
        initialize(
                element1,
                element2,
                element3
        );
    }

    /** Public no-arg constructor for {@link Externalizable} support only. <em>Application code should not use this!</em> **/
    public FloatShortCharTuple() {
    }

    private void initialize(
            final float element1,
            final short element2,
            final char element3
    ) {
        this.element1 = element1;
        this.element2 = element2;
        this.element3 = element3;
        cachedHashCode = ((31 +
                Float.hashCode(element1)) * 31 +
                Short.hashCode(element2)) * 31 +
                Character.hashCode(element3);
    }

    public final float getFirstElement() {
        return element1;
    }

    public final short getSecondElement() {
        return element2;
    }

    public final char getThirdElement() {
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
        final FloatShortCharTuple typedOther = (FloatShortCharTuple) other;
        // @formatter:off
        return element1 == typedOther.element1 &&
               element2 == typedOther.element2 &&
               element3 == typedOther.element3;
        // @formatter:on
    }

    @Override
    public final int compareTo(@NotNull final FloatShortCharTuple other) {
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
        out.writeFloat(element1);
        out.writeShort(element2);
        out.writeChar(element3);
    }

    @Override
    public void readExternal(@NotNull final ObjectInput in) throws IOException, ClassNotFoundException {
        initialize(
                in.readFloat(),
                in.readShort(),
                in.readChar()
        );
    }

    @Override
    public void writeExternalStreaming(@NotNull final ObjectOutput out, @NotNull final TIntObjectMap<SerializationUtils.Writer> cachedWriters) throws IOException {
        out.writeFloat(element1);
        out.writeShort(element2);
        out.writeChar(element3);
    }

    @Override
    public void readExternalStreaming(@NotNull final ObjectInput in, @NotNull final TIntObjectMap<SerializationUtils.Reader> cachedReaders) throws Exception {
        initialize(
                in.readFloat(),
                in.readShort(),
                in.readChar()
        );
    }

    @Override
    public String toString() {
        return "FloatShortCharTuple{" +
                element1 + ", " +
                element2 + ", " +
                element3 + '}';
    }

    @Override
    public FloatShortCharTuple canonicalize(@NotNull final UnaryOperator<Object> canonicalizer) {
        return this;
    }
}
