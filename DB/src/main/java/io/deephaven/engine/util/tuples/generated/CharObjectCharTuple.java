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
 * <p>3-Tuple (triple) key class composed of char, Object, and char elements.
 * <p>Generated by {@link io.deephaven.engine.util.tuples.TupleCodeGenerator}.
 */
public class CharObjectCharTuple implements Comparable<CharObjectCharTuple>, Externalizable, StreamingExternalizable, CanonicalizableTuple<CharObjectCharTuple> {

    private static final long serialVersionUID = 1L;

    private char element1;
    private Object element2;
    private char element3;

    private transient int cachedHashCode;

    public CharObjectCharTuple(
            final char element1,
            final Object element2,
            final char element3
    ) {
        initialize(
                element1,
                element2,
                element3
        );
    }

    /** Public no-arg constructor for {@link Externalizable} support only. <em>Application code should not use this!</em> **/
    public CharObjectCharTuple() {
    }

    private void initialize(
            final char element1,
            final Object element2,
            final char element3
    ) {
        this.element1 = element1;
        this.element2 = element2;
        this.element3 = element3;
        cachedHashCode = ((31 +
                Character.hashCode(element1)) * 31 +
                Objects.hashCode(element2)) * 31 +
                Character.hashCode(element3);
    }

    public final char getFirstElement() {
        return element1;
    }

    public final Object getSecondElement() {
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
        final CharObjectCharTuple typedOther = (CharObjectCharTuple) other;
        // @formatter:off
        return element1 == typedOther.element1 &&
               Objects.equals(element2, typedOther.element2) &&
               element3 == typedOther.element3;
        // @formatter:on
    }

    @Override
    public final int compareTo(@NotNull final CharObjectCharTuple other) {
        if (this == other) {
            return 0;
        }
        int comparison;
        // @formatter:off
        return 0 != (comparison = LanguageFunctionUtil.compareTo(element1, other.element1)) ? comparison :
               0 != (comparison = LanguageFunctionUtil.compareTo((Comparable)element2, (Comparable)other.element2)) ? comparison :
               LanguageFunctionUtil.compareTo(element3, other.element3);
        // @formatter:on
    }

    @Override
    public void writeExternal(@NotNull final ObjectOutput out) throws IOException {
        out.writeChar(element1);
        out.writeObject(element2);
        out.writeChar(element3);
    }

    @Override
    public void readExternal(@NotNull final ObjectInput in) throws IOException, ClassNotFoundException {
        initialize(
                in.readChar(),
                in.readObject(),
                in.readChar()
        );
    }

    @Override
    public void writeExternalStreaming(@NotNull final ObjectOutput out, @NotNull final TIntObjectMap<SerializationUtils.Writer> cachedWriters) throws IOException {
        out.writeChar(element1);
        StreamingExternalizable.writeObjectElement(out, cachedWriters, 1, element2);
        out.writeChar(element3);
    }

    @Override
    public void readExternalStreaming(@NotNull final ObjectInput in, @NotNull final TIntObjectMap<SerializationUtils.Reader> cachedReaders) throws Exception {
        initialize(
                in.readChar(),
                StreamingExternalizable.readObjectElement(in, cachedReaders, 1),
                in.readChar()
        );
    }

    @Override
    public String toString() {
        return "CharObjectCharTuple{" +
                element1 + ", " +
                element2 + ", " +
                element3 + '}';
    }

    @Override
    public CharObjectCharTuple canonicalize(@NotNull final UnaryOperator<Object> canonicalizer) {
        final Object canonicalizedElement2 = canonicalizer.apply(element2);
        return canonicalizedElement2 == element2
                ? this : new CharObjectCharTuple(element1, canonicalizedElement2, element3);
    }
}
