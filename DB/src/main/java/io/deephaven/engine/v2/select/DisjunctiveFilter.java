/*
 * Copyright (c) 2016-2021 Deephaven Data Labs and Patent Pending
 */

package io.deephaven.engine.v2.select;

import io.deephaven.base.verify.Assert;
import io.deephaven.engine.exceptions.QueryCancellationException;
import io.deephaven.engine.tables.Table;
import io.deephaven.engine.v2.utils.MutableRowSet;
import io.deephaven.engine.v2.utils.RowSet;
import io.deephaven.engine.v2.utils.TrackingMutableRowSet;
import io.deephaven.util.SafeCloseable;

import java.util.*;

public class DisjunctiveFilter extends ComposedFilter {
    private DisjunctiveFilter(SelectFilter[] componentFilters) {
        super(componentFilters);
    }

    public static SelectFilter makeDisjunctiveFilter(SelectFilter... componentFilters) {
        if (componentFilters.length == 1) {
            return componentFilters[0];
        }

        final List<SelectFilter> rawComponents = new ArrayList<>();
        for (int ii = 0; ii < componentFilters.length; ++ii) {
            if (componentFilters[ii] instanceof DisjunctiveFilter) {
                rawComponents.addAll(Arrays.asList(((DisjunctiveFilter) componentFilters[ii]).getComponentFilters()));
            } else {
                rawComponents.add(componentFilters[ii]);
            }
        }

        return new DisjunctiveFilter(rawComponents.toArray(SelectFilter.ZERO_LENGTH_SELECT_FILTER_ARRAY));
    }

    @Override
    public MutableRowSet filter(RowSet selection, RowSet fullSet, Table table, boolean usePrev) {
        MutableRowSet matched = null;
        try (MutableRowSet remaining = selection.clone()) {
            for (SelectFilter filter : componentFilters) {
                if (Thread.interrupted()) {
                    throw new QueryCancellationException("interrupted while filtering");
                }

                // If a previous clause has already matched a row, we do not need to re-evaluate it
                if (matched != null) {
                    remaining.remove(matched);
                }

                final MutableRowSet filterMatched = filter.filter(remaining, fullSet, table, usePrev);

                // All matched entries get put into the value
                if (matched == null) {
                    matched = filterMatched;
                } else {
                    try (final SafeCloseable ignored = filterMatched) {
                        matched.insert(filterMatched);
                    }
                }

                if (matched.size() == selection.size()) {
                    // Everything in the input set already belongs in the output set
                    break;
                }
            }
        }

        return matched == null ? selection.clone() : matched.clone();
    }

    @Override
    public DisjunctiveFilter copy() {
        return new DisjunctiveFilter(
                Arrays.stream(getComponentFilters()).map(SelectFilter::copy).toArray(SelectFilter[]::new));
    }

    @Override
    public String toString() {
        return "DisjunctiveFilter(" + Arrays.toString(componentFilters) + ')';
    }
}
