package io.deephaven.engine.v2.join.dupexpand;

import io.deephaven.compilertools.ReplicatePrimitiveCode;
import io.deephaven.compilertools.ReplicateUtilities;
import io.deephaven.engine.v2.sort.ReplicateSortKernel;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class ReplicateDupExpandKernel {
    public static void main(String[] args) throws IOException {
        final String charClassPath = "DB/src/main/java/io/deephaven/engine/v2/join/dupexpand/CharDupExpandKernel.java";
        ReplicatePrimitiveCode.charToAll(charClassPath);
        fixupObjectDupCompact(ReplicatePrimitiveCode.charToObject(charClassPath));
    }

    private static void fixupObjectDupCompact(String objectPath) throws IOException {
        final File objectFile = new File(objectPath);
        final List<String> lines = FileUtils.readLines(objectFile, Charset.defaultCharset());
        FileUtils.writeLines(objectFile,
                ReplicateSortKernel.fixupObjectComparisons(ReplicateUtilities.fixupChunkAttributes(lines)));
    }
}
