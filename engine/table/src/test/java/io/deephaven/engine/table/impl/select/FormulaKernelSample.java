package io.deephaven.engine.table.impl.select;
// QueryLibrary internal version number: DEFAULT
import java.lang.*;
import java.util.*;
import io.deephaven.base.string.cache.CompressedString;
import io.deephaven.engine.chunk.Attributes;
import io.deephaven.engine.chunk.ByteChunk;
import io.deephaven.engine.chunk.CharChunk;
import io.deephaven.engine.chunk.Chunk;
import io.deephaven.engine.chunk.DoubleChunk;
import io.deephaven.engine.chunk.FloatChunk;
import io.deephaven.engine.chunk.IntChunk;
import io.deephaven.engine.chunk.LongChunk;
import io.deephaven.engine.chunk.ObjectChunk;
import io.deephaven.engine.chunk.ShortChunk;
import io.deephaven.engine.chunk.WritableByteChunk;
import io.deephaven.engine.chunk.WritableCharChunk;
import io.deephaven.engine.chunk.WritableChunk;
import io.deephaven.engine.chunk.WritableDoubleChunk;
import io.deephaven.engine.chunk.WritableFloatChunk;
import io.deephaven.engine.chunk.WritableIntChunk;
import io.deephaven.engine.chunk.WritableLongChunk;
import io.deephaven.engine.chunk.WritableObjectChunk;
import io.deephaven.engine.chunk.WritableShortChunk;
import io.deephaven.engine.rowset.RowSequence;
import io.deephaven.engine.rowset.RowSet;
import io.deephaven.engine.rowset.RowSetBuilderRandom;
import io.deephaven.engine.rowset.RowSetBuilderSequential;
import io.deephaven.engine.rowset.RowSetFactory;
import io.deephaven.engine.rowset.TrackingRowSet;
import io.deephaven.engine.rowset.TrackingWritableRowSet;
import io.deephaven.engine.rowset.WritableRowSet;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.Context;
import io.deephaven.engine.table.DataColumn;
import io.deephaven.engine.table.Table;
import static io.deephaven.engine.table.impl.select.ConditionFilter.FilterKernel;
import io.deephaven.engine.table.lang.QueryScopeParam;
import io.deephaven.engine.time.DateTime;
import io.deephaven.engine.time.DateTimeUtils;
import io.deephaven.engine.time.Period;
import io.deephaven.util.datastructures.LongSizedDataStructure;
import io.deephaven.util.type.ArrayTypeUtils;
import io.deephaven.util.type.TypeUtils;
import java.lang.reflect.Array;
import java.util.concurrent.ConcurrentHashMap;
import org.joda.time.LocalTime;
import static io.deephaven.base.string.cache.CompressedString.*;
import static io.deephaven.engine.chunk.Attributes.*;
import static io.deephaven.engine.function.BinSearch.*;
import static io.deephaven.engine.function.BooleanPrimitives.*;
import static io.deephaven.engine.function.ByteNumericPrimitives.*;
import static io.deephaven.engine.function.BytePrimitives.*;
import static io.deephaven.engine.function.Casting.*;
import static io.deephaven.engine.function.CharacterPrimitives.*;
import static io.deephaven.engine.function.ComparePrimitives.*;
import static io.deephaven.engine.function.DoubleFpPrimitives.*;
import static io.deephaven.engine.function.DoubleNumericPrimitives.*;
import static io.deephaven.engine.function.DoublePrimitives.*;
import static io.deephaven.engine.function.FloatFpPrimitives.*;
import static io.deephaven.engine.function.FloatNumericPrimitives.*;
import static io.deephaven.engine.function.FloatPrimitives.*;
import static io.deephaven.engine.function.IntegerNumericPrimitives.*;
import static io.deephaven.engine.function.IntegerPrimitives.*;
import static io.deephaven.engine.function.LongNumericPrimitives.*;
import static io.deephaven.engine.function.LongPrimitives.*;
import static io.deephaven.engine.function.ObjectPrimitives.*;
import static io.deephaven.engine.function.PrimitiveParseUtil.*;
import static io.deephaven.engine.function.ShortNumericPrimitives.*;
import static io.deephaven.engine.function.ShortPrimitives.*;
import static io.deephaven.engine.table.impl.lang.QueryLanguageFunctionUtils.*;
import static io.deephaven.engine.table.impl.verify.TableAssertions.*;
import static io.deephaven.engine.time.DateTimeUtils.*;
import static io.deephaven.engine.time.TimeZone.*;
import static io.deephaven.engine.time.calendar.StaticCalendarMethods.*;
import static io.deephaven.engine.util.ColorUtilImpl.*;
import static io.deephaven.gui.color.Color.*;
import static io.deephaven.util.QueryConstants.*;

public class FormulaKernelSample implements io.deephaven.engine.table.impl.select.formula.FormulaKernel {
    public static final io.deephaven.engine.table.impl.select.formula.FormulaKernelFactory __FORMULA_KERNEL_FACTORY = FormulaKernelSample::new;

    private final io.deephaven.engine.vector.LongVector II_;
    private final java.lang.Integer q;

    public FormulaKernelSample(io.deephaven.engine.vector.Vector[] __vectors,
            io.deephaven.engine.table.lang.QueryScopeParam[] __params) {
        II_ = (io.deephaven.engine.vector.LongVector)__vectors[0];
        q = (java.lang.Integer)__params[0].getValue();
    }

    @Override
    public FormulaFillContext makeFillContext(final int __chunkCapacity) {
        return new FormulaFillContext(__chunkCapacity);
    }

    @Override
    public void applyFormulaChunk(io.deephaven.engine.table.impl.select.Formula.FillContext __context,
            final WritableChunk<? super Attributes.Values> __destination,
            Chunk<? extends Attributes.Values>[] __sources) {
        final WritableLongChunk<? super Attributes.Values> __typedDestination = __destination.asWritableLongChunk();
        final LongChunk<? extends Attributes.Values> __chunk__col__II = __sources[0].asLongChunk();
        final LongChunk<? extends Attributes.Values> __chunk__col__ii = __sources[1].asLongChunk();
        final IntChunk<? extends Attributes.Values> __chunk__col__I = __sources[2].asIntChunk();
        final IntChunk<? extends Attributes.Values> __chunk__col__i = __sources[3].asIntChunk();
        final int __size = __typedDestination.size();
        for (int __chunkPos = 0; __chunkPos < __size; ++__chunkPos) {
            __typedDestination.set(__chunkPos, applyFormulaPerItem(__chunk__col__II.get(__chunkPos), __chunk__col__ii.get(__chunkPos), __chunk__col__I.get(__chunkPos), __chunk__col__i.get(__chunkPos)));
        }
    }

    private long applyFormulaPerItem(long II, long ii, int I, int i) {
        try {
            return plus(plus(multiply(I, II), multiply(q.intValue(), ii)), II_.get(minus(i, 1)));
        } catch (java.lang.Exception __e) {
            throw new io.deephaven.engine.table.impl.select.FormulaEvaluationException("In formula: " + "plus(plus(multiply(I, II), multiply(q.intValue(), ii)), II_.get(minus(i, 1)))", __e);
        }
    }

    private class FormulaFillContext implements io.deephaven.engine.table.impl.select.Formula.FillContext {
        FormulaFillContext(int __chunkCapacity) {
        }
    }

}