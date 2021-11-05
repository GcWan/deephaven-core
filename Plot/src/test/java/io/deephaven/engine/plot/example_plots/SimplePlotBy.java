/*
 * Copyright (c) 2016-2021 Deephaven Data Labs and Patent Pending
 */

package io.deephaven.engine.plot.example_plots;

import io.deephaven.engine.plot.Figure;
import io.deephaven.engine.plot.FigureFactory;
import io.deephaven.engine.plot.axistransformations.AxisTransforms;
import io.deephaven.engine.tables.Table;
import io.deephaven.engine.tables.utils.TableTools;


public class SimplePlotBy {

    public static void main(String[] args) {
        Number x = Integer.valueOf(5);
        final String[] cats = {"A", "B", "A", "B", "A", "B"};
        final int[] x1 = {1, 1, 2, 2, 3, 3};
        final double[] y1 = {2.0, 3.0, 4.0, 5.0, 3.0, 4.0};
        final Table t =
                TableTools.newTable(TableTools.col("USym", cats), TableTools.intCol("TrackingMutableRowSet", x1),
                        TableTools.doubleCol("Value", y1));

        Figure fig = FigureFactory.figure();
        Figure cht = fig.newChart(0)
                .chartTitle("Chart Title");
        Figure axs = cht.newAxes().xTransform(AxisTransforms.LOG)
                .xLabel("X")
                .yLabel("Y")
                .plotBy("Test1", t, "TrackingMutableRowSet", "Value", "USym").lineColor("black").pointsVisible(true)
                .show();

        ExamplePlotUtils.display(axs);
    }
}
